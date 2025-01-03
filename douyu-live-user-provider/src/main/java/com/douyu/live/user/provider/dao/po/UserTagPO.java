package com.douyu.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author liuguanyi
 * * @date 2025/1/3
 */
@Data
public class UserTagPO {
    /**
     * 用户id
     */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
     * 标签记录字段
     */
    @TableField(value = "tag_info_01")
    private Long tagInfo01;

    /**
     * 标签记录字段
     */
    @TableField(value = "tag_info_02")
    private Long tagInfo02;

    /**
     * 标签记录字段
     */
    @TableField(value = "tag_info_03")
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
