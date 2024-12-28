package com.douyu.live.user.provider.config;

import com.alibaba.fastjson.JSON;
import com.douyu.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.douyu.live.user.dto.UserDTO;
import com.douyu.live.user.provider.constant.TopicEnum;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RocketMQ 的消费者bean 配置类
 *
 * @author luiguanyi
 * * @date 2024/12/28
 */

@Configuration
public class RocketMQConsumerConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);
    @Resource
    private RocketMQConsumerProperties consumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }

    public void initConsumer() {
        try {
            //初始化我们的 RocketMQ 消费者
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setNamesrvAddr(consumerProperties.getNameSrv());
            defaultMQPushConsumer.setConsumerGroup(consumerProperties.getGroupName());
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(TopicEnum.USER_UPDATE_CACHE.getTopic(), "*");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (message, context) -> {
                String msgStr = new String(message.get(0).getBody());
                UserDTO userDTO = JSON.parseObject(msgStr, UserDTO.class);
                if (userDTO == null || userDTO.getUserId() == null) {
                    LOGGER.error("用户id为空，参数异常，内容: {} ", msgStr);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //延迟消息的回调，处理相关的缓存二次删除
                redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()));
                LOGGER.error("延迟删除处理，userDTO is {}", userDTO);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("mq 消费者启动成功,nameSrv is {}", consumerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}


