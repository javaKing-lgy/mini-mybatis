package com.douyu.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Conditional;

/**
 * 用户信息缓存key
 * @author luiguanyi
 * * @date 2024/12/22
 */
@Configurable
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKeyBuilder extends RedisKeyBuilder{
    private static String USER_INFO_KEY = "userInfo";
    private static String USER_TAG_LOCK_KEY = "userTagLock";
    private static String USER_TAG_KEY = "userTag";
    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY +
                super.getSplitItem() + userId;
    }
    public String buildUserTagLockKey(Long userId) {
        return super.getPrefix() + USER_TAG_LOCK_KEY +
                super.getSplitItem() + userId;
    }
    public String buildUserTagKey(Long userId) {
        return super.getPrefix() + USER_TAG_KEY +
                super.getSplitItem() + userId;
    }
}
