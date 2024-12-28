package com.douyu.live.user.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *  rocketmq 消费者配置
 * @author luiguanyi
 * * @date 2024/12/28
 */
@ConfigurationProperties(prefix="douyu.rmq.consumer")
@Configuration
@Data
public class RocketMQConsumerProperties {
    //rocketmq 的nameSever 地址
    private String nameSrv;
    //分组名称
    private String groupName;
}
