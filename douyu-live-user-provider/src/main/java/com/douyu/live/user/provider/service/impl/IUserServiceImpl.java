package com.douyu.live.user.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.douyu.live.common.interfaces.utils.ConvertBeanUtils;
import com.douyu.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.douyu.live.user.constants.CacheAsyncDeleteCode;
import com.douyu.live.user.dto.UserCacheAsyncDeleteDTO;
import com.douyu.live.user.dto.UserDTO;
import com.douyu.live.user.constants.TopicEnum;
import com.douyu.live.user.provider.dao.mapper.UserMapper;
import com.douyu.live.user.provider.dao.po.UserPO;
import com.douyu.live.user.provider.service.IUserService;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户service层实现类
 * @author luiguanyi
 * * @date 2024/12/16
 */
@Log4j
@Service
public class IUserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String,UserDTO> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;
    @Resource
    private MQProducer mqProducer;


    @Override
    public UserDTO getByUserId(Long userId) {
        if(userId == null){
            return null;
        }
        String key = userProviderCacheKeyBuilder.buildUserInfoKey(userId);
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if(userDTO != null){
            return userDTO;
        }
        userDTO =  ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        if (userDTO != null){
            redisTemplate.opsForValue().set(key, userDTO,30, TimeUnit.MINUTES);
        }
        return userDTO;
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null){
            return  false;
        }
        userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        // 使用延迟双删解决缓存一致性
        redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()));
        sendMessageWithDelay(userDTO);
        return true;
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null){
            return  false;
        }
        userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return false;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds) {
        if(userIds == null || userIds.isEmpty()){
            return Maps.newHashMap();
        }
        List<Long> userIdList = userIds.stream().filter(userId -> userId > 0).collect(Collectors.toList());
        // key list 为 redis 中含有的数据
        List<String> keyList = new ArrayList<>();
        // 先查询redis中的含有的数据
        userIdList.forEach(userId -> keyList.add(userProviderCacheKeyBuilder.buildUserInfoKey(userId)));
        // 在这里我们先要过滤出来自redis中为null的数据 之后我们就要就要查询redis中没有的数据了 如果 在redis中都查询到了我们就可以直接返回了
        List<UserDTO> userDTOList = redisTemplate.opsForValue().multiGet(keyList).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(userDTOList)&& userDTOList.size() == userIdList.size()){
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
        }
        List<Long> userIdNotInCacheList;
        if (!CollectionUtils.isEmpty(userDTOList)) {
            // 查询出在redis中的userid
            List<Long> userIdInCacheList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());
            // 进行剔除
            userIdNotInCacheList = userIdList.stream().filter(x -> !userIdInCacheList.contains(x)).collect(Collectors.toList());
        }else{
            userIdNotInCacheList = userIdList;
        }
        // 如果这个list不为null 我们就去查询数据库
        // 下面这种写法 我不建议 因为如果我们对数据库进行分表之后 这样他会遍历所有的表 之后进行union all 这样会非常消耗性能
//        if(!CollectionUtils.isEmpty(userIdNotInCacheList)){
//            List<UserDTO> userDTOList1 = userMapper.selectBatchIds(userIdNotInCacheList).stream().map(userPO -> ConvertBeanUtils.convert(userPO, UserDTO.class)).collect(Collectors.toList());
//            userDTOList.addAll(userDTOList1);
//            userDTOList.forEach(userDTO -> {
//                redisTemplate.opsForValue().set(userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()), userDTO);
//            });
//        }
        // 我更建议这种写法 使用多线程查询 替换掉 union all 这里使用100 是因为我分成了100 张表
        Map<Long, List<Long>> map = userIdNotInCacheList.stream().collect(Collectors.groupingBy(userid -> userid % 100));
        List<UserDTO> dbQueryResult = new CopyOnWriteArrayList<>();
        map.values().parallelStream().forEach(list -> dbQueryResult.addAll(ConvertBeanUtils.convertList(userMapper.selectBatchIds(list), UserDTO.class)));
        // 将新查询的结果放入到redis中
        if (!CollectionUtils.isEmpty(dbQueryResult)){
            Map<String, UserDTO> collect = dbQueryResult.stream().collect(Collectors.toMap(userDTO -> userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()), x -> x));
            redisTemplate.opsForValue().multiSet(collect);
            // 设置redis的过期时间 通过管道批量传输命令 减少网络io开销
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String redisKey : collect.keySet()) {
                        operations.expire((K) redisKey, createRandomExpireTime(), TimeUnit.MINUTES);
                    }
                    return null;
                }
            });
            userDTOList.addAll(dbQueryResult);
        }
        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId,x -> x));
    }

    /**
     * 随机生成过期时间
     * @return int
     */
    private int createRandomExpireTime(){
        int time = ThreadLocalRandom.current().nextInt(1000);
        return time+ 60 * 30;
    }
    /**
     * 使用延迟双删解决缓存一致性
     * @param userDTO
     */
    public void sendMessageWithDelay(UserDTO userDTO) {
        try {
            UserCacheAsyncDeleteDTO dto = new UserCacheAsyncDeleteDTO();
            dto.setCode(CacheAsyncDeleteCode.USER_INFO_DELETE.getCode());
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userDTO.getUserId());
            dto.setMsg(JSON.toJSONString(map));
            Message message = new Message();
            message.setTopic(TopicEnum.USER_UPDATE_CACHE.getTopic());
            message.setBody(JSON.toJSONString(dto).getBytes());
            message.setDelayTimeLevel(1);
            mqProducer.send(message);
        } catch (Exception e) {
            log.error("mq发送消息失败");
        }
    }
}
