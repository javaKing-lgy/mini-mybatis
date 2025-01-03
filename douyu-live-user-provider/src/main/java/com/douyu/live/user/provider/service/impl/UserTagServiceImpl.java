package com.douyu.live.user.provider.service.impl;


import com.douyu.live.user.constants.UserTagsEnum;
import com.douyu.live.user.provider.dao.mapper.IUserTagMapper;
import com.douyu.live.user.provider.service.IUserTagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author liuguanyi
 * * @date 2025/1/3
 */
@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private IUserTagMapper userTagMapper;


    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }
}
