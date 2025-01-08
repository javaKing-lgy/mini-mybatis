package com.douyu.live.user.provider.service.impl;


import com.alibaba.fastjson.JSON;
import com.douyu.live.common.utils.ConvertBeanUtils;
import com.douyu.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.douyu.live.user.constants.CacheAsyncDeleteCode;
import com.douyu.live.user.constants.TopicEnum;
import com.douyu.live.user.constants.UserTagFieldNameConstants;
import com.douyu.live.user.constants.UserTagsEnum;
import com.douyu.live.user.dto.UserCacheAsyncDeleteDTO;
import com.douyu.live.user.provider.dao.mapper.IUserTagMapper;
import com.douyu.live.user.provider.dao.po.UserTagPO;
import com.douyu.live.user.provider.dto.UserTagDTO;
import com.douyu.live.user.provider.service.IUserTagService;
import com.douyu.live.user.utils.TagInfoUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户标签服务实现类
 * @author liuguanyi
 * * @date 2025/1/3
 */
@Service
@Slf4j
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private IUserTagMapper userTagMapper;

    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Resource
    private RedisTemplate<String, UserTagDTO> redisTemplate;

    @Resource
    private MQProducer mqProducer;
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        //首先进行更新update 也就是set方法
        boolean updateResult = userTagMapper.setTag(userId, userTagsEnum.getDesc(), userTagsEnum.getTag()) > 0;
        if (updateResult){
            deleteUserTagCache(userId);
            return true;
        }
        // 更新失败的情况 1.已经设置了这个标签 2. 这个表记录不存在
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        // 已经设置了这个标签
        if(userTagPO != null){
            return false;
        }
        // 这个表记录不存在
        // 使用分布式锁 todo 之后替换成 redission
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String key = userProviderCacheKeyBuilder.buildUserTagLockKey(userId);
        Boolean isSet = valueOps.setIfAbsent(key, "-1", Duration.ofSeconds(3));
        if (Boolean.FALSE.equals(isSet)) return false;
        userTagPO = new UserTagPO();
        userTagPO.setUserId(userId);
        userTagMapper.insert(userTagPO);
        updateResult = userTagMapper.setTag(userId, userTagsEnum.getDesc(), userTagsEnum.getTag()) > 0;
        stringRedisTemplate.delete(key);
        return updateResult;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean result = userTagMapper.cancelTag(userId, userTagsEnum.getDesc(), userTagsEnum.getTag()) > 0;
        if (!result){
            return false;
        }
        deleteUserTagCache(userId);
        return true;

    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagDTO userTagDTO = getUserTagCache(userId);
        if(userTagDTO == null){
            return false;
        }
        String fieldName = userTagsEnum.getFieldName();
        if(fieldName.equals(UserTagFieldNameConstants.TAG_INFO_01)){
            return TagInfoUtils.isContain(userTagDTO.getTagInfo01(), userTagsEnum.getTag());
        }else if(fieldName.equals(UserTagFieldNameConstants.TAG_INFO_02)){
            return TagInfoUtils.isContain(userTagDTO.getTagInfo02(), userTagsEnum.getTag());
        }else if(fieldName.equals(UserTagFieldNameConstants.TAG_INFO_03)){
            return TagInfoUtils.isContain(userTagDTO.getTagInfo03(), userTagsEnum.getTag());
        }
        return false;
    }

    /**
     * 获取用户标签缓存
     * @param userId
     * @return
     */
    private UserTagDTO getUserTagCache(Long userId){
        String key = userProviderCacheKeyBuilder.buildUserTagKey(userId);
        UserTagDTO userTagDTO = redisTemplate.opsForValue().get(key);
        if(userTagDTO != null){
            return userTagDTO;
        }
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if(userTagPO == null){
            return null;
        }
        userTagDTO = ConvertBeanUtils.convert(userTagPO, UserTagDTO.class);
        redisTemplate.opsForValue().set(key, userTagDTO);
        return userTagDTO;
    }

    /**
     *  删除用户标签缓存
     *  使用延迟双删
     * @param userId
     */
    private void deleteUserTagCache(Long userId){
        String key = userProviderCacheKeyBuilder.buildUserTagKey(userId);
        redisTemplate.delete(key);
        UserCacheAsyncDeleteDTO dto = new UserCacheAsyncDeleteDTO();
        dto.setCode(CacheAsyncDeleteCode.USER_TAG_DELETE.getCode());
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        dto.setMsg(JSON.toJSONString(map));
        Message message = new Message();
        message.setTopic(TopicEnum.USER_UPDATE_CACHE.getTopic());
        message.setBody(JSON.toJSONString(dto).getBytes());
        message.setDelayTimeLevel(1);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            log.error("mq发送消息失败");
        }
    }
}
