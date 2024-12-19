package com.douyu.live.user.interfaces.rpc;

import com.douyu.live.user.dto.UserDTO;

public interface IUserRpc {
    public void test();
    public UserDTO getUserById(Long userId);
}
