package com.douyu.live.user.provider.rpc;

import com.douyu.live.user.constants.UserTagsEnum;
import com.douyu.live.user.interfaces.rpc.IUserTagRpc;
import com.douyu.live.user.provider.service.IUserTagService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author liuguanyi
 * * @date 2025/1/3
 */
@DubboService
public class UserTagRpcImpl implements IUserTagRpc {
    @Resource
    private IUserTagService userTagService;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.setTag(userId, userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum
            userTagsEnum) {
        return userTagService.cancelTag(userId, userTagsEnum);
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.containTag(userId, userTagsEnum);
    }
}
