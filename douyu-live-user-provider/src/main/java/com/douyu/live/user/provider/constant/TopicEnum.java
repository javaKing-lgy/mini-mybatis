package com.douyu.live.user.provider.constant;

import lombok.Getter;

/**
 * mq topic 枚举
 * @author luiguanyi
 * * @date 2024/12/28
 */
@Getter
public enum TopicEnum {
    USER_UPDATE_CACHE("user-update-cache");

    private final String topic;

    TopicEnum(String topic) {
        this.topic = topic;
    }

}

