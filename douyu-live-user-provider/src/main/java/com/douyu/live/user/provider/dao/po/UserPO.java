package com.douyu.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类
 * <p>
 * 该类映射到数据库的用户表，用于表示和操作用户相关的数据
 *
 * @author luiguanyi
 * @Date: 2024/12/16
 */
@TableName("t_user") // 指定与数据库表t_user对应
@Data // Lombok注解，自动生成getter和setter方法
public class UserPO {
    /**
     * 用户ID
     * <p>
     * 使用输入的ID类型，由外部提供ID值
     */
    @TableId(type = IdType.INPUT) // 设置主键类型为INPUT，即自定义输入
    private Long userId;

    /**
     * 用户昵称
     * <p>
     * 用户在平台上的显示名称，允许重复
     */
    private String nickName;

    /**
     * 用户真实姓名
     * <p>
     * 用户的真实姓名，用于实名认证等场景
     */
    private String trueName;

    /**
     * 用户头像URL
     * <p>
     * 存储用户头像的网络地址
     */
    private String avatar;

    /**
     * 用户性别
     * <p>
     * 通常使用数字表示性别，如0表示未知，1表示男，2表示女
     */
    private Integer sex;

    /**
     * 用户工作城市代码
     * <p>
     * 表示用户当前工作的城市，使用城市代码而不是城市名称，便于数据统计和处理
     */
    private Integer workCity;

    /**
     * 用户出生城市代码
     * <p>
     * 表示用户出生的城市，同样使用城市代码
     */
    private Integer bornCity;

    /**
     * 用户出生日期
     * <p>
     * 记录用户的出生日期，用于年龄计算或星座匹配等功能
     */
    private Date bornDate;

    /**
     * 用户记录创建时间
     * <p>
     * 自动记录用户信息被创建的时间，用于数据追踪和审计
     */
    private Date createTime;

    /**
     * 用户记录最后更新时间
     * <p>
     * 自动记录用户信息最后一次更新的时间，用于数据追踪和审计
     */
    private Date updateTime;
}
