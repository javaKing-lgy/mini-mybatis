package com.douyu.live.user.provider.service;

import com.douyu.live.user.dto.UserDTO;

/**
 * @author luiguanyi
 * * @date 2024/12/16
 */
public interface IUserService {

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    public UserDTO getByUserId(Long userId);
}
