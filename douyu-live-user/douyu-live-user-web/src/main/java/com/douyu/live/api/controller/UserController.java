package com.douyu.live.api.controller;

import com.douyu.live.user.dto.UserDTO;
import com.douyu.live.user.interfaces.rpc.IUserRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId) {
        return userRpc.getUserById(userId);
    }
    @GetMapping("/updateUserInfo")
    public boolean updateUserInfo(UserDTO userDTO) {
        return userRpc.updateUserInfo(userDTO);
    }
    @GetMapping("/insertOne")
    public boolean insertOne(UserDTO userDTO) {
        return userRpc.insertOne(userDTO);
    }
    @GetMapping("/batchQueryUserInfo")
    public Map<Long,UserDTO> batchQueryUserInfo(String userIdStr) {
        String[] idStr = userIdStr.split(",");
        List<Long> userIdList = new ArrayList<>();
        for (String userId : idStr) {
            userIdList.add(Long.valueOf(userId));
        }
        return userRpc.batchQueryUserInfo(userIdList);
    }

}
