package com.douyu.live.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户缓存删除DTO(用于mq)
 * @author liuguanyi
 * * @date 2025/1/6
 */
@Data
public class UserCacheAsyncDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 删除用户缓存的code 用于区分
     */
    private Integer code;
    private String msg;
}
