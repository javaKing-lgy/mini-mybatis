package com.douyu.live.user.provider.service;

import com.douyu.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

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
    public boolean updateUserInfo(UserDTO userDTO);
    public boolean insertOne(UserDTO userDTO);
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds);
}
