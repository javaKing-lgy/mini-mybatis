package com.douyu.live.user.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * mq生产者配置类
 * @author luiguanyi
 * * @date 2024/12/25
 */
@ConfigurationProperties(prefix="douyu.mq.producer")
@Configuration
@Data
public class RocketMQProducerProperties {
    //rocketmq 的nameSever 地址
    private String nameSrv;
    //分组名称
    private String groupName;
    //消息重发次数
    private int retryTimes;
    //发送超时时间
    private int sendTimeOut;

}
