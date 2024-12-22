package com.douyu.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * redis key 构建器
 * @author luiguanyi
 * * @date 2024/12/22
 */
public class RedisKeyBuilder {
    @Value("${spring.application.name}")
    private String applicationName;
    private static final String SPLIT_ITEM = ":";
    public String getSplitItem() {
        return SPLIT_ITEM;
    }
    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }
}
