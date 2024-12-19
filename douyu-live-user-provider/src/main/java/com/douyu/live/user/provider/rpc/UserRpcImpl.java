package com.douyu.live.user.provider.rpc;

import com.douyu.live.user.dto.UserDTO;
import com.douyu.live.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.apache.calcite.runtime.Resources;
import org.apache.dubbo.config.annotation.DubboService;
import com.douyu.live.user.interfaces.rpc.IUserRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DubboService
public class UserRpcImpl implements IUserRpc {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRpcImpl.class);

    @Resource
    private IUserService userService;

    @Override
    public void test() {
        LOGGER.info("this is dubbo server test!");
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userService.getByUserId(userId);
    }
}
