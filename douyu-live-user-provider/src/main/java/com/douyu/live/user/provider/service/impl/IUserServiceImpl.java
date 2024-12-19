package com.douyu.live.user.provider.service.impl;

import com.douyu.live.common.interfaces.utils.ConvertBeanUtils;
import com.douyu.live.user.dto.UserDTO;
import com.douyu.live.user.provider.dao.mapper.UserMapper;
import com.douyu.live.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author luiguanyi
 * * @date 2024/12/16
 */
@Service
public class IUserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDTO getByUserId(Long userId) {
        if(userId == null){
            return null;
        }
        return ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
    }
}
