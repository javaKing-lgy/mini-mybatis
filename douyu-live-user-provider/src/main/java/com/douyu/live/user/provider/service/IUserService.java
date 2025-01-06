package com.douyu.live.user.provider.service;

import com.douyu.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 * @author luiguanyi
 * * @date 2024/12/16
 */
public interface IUserService {

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    UserDTO getByUserId(Long userId);
    /**
     * 更新用户信息
     * @param userDTO
     * @return
     */
    boolean updateUserInfo(UserDTO userDTO);
    /**
     * 插入用户信息
     * @param userDTO
     * @return
     */
    boolean insertOne(UserDTO userDTO);
    /**
     * 批量查询用户信息
     * @param userIds
     * @return
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds);
}
