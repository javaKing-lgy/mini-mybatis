package com.douyu.live.user.provider.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuguanyi
 * * @date 2025/1/6
 */
@Data
public class UserTagDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标签记录字段
     */
    private Long tagInfo01;

    /**
     * 标签记录字段
     */
    private Long tagInfo02;

    /**
     * 标签记录字段
     */
    private Long tagInfo03;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
