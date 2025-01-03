package com.douyu.live.user.interfaces.rpc;


import com.douyu.live.user.constants.UserTagsEnum;

/**
 * 用户标签rpc
 * @author liuguanyi
 * * @date 2025/1/3
 */
public interface IUserTagRpc {
    /**
     * 设置标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);
    /**
     * 取消标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean cancelTag(Long userId,UserTagsEnum userTagsEnum);
    /**
     * 是否包含某个标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean containTag(Long userId,UserTagsEnum userTagsEnum);

}
