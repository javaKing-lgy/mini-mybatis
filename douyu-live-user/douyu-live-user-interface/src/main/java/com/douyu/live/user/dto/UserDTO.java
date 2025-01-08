package com.douyu.live.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author luiguanyi
 * * @date 2024/12/16
 */
@Data
public class UserDTO {
    private Long userId;
    private String nickName;
    private String trueName;
    private String avatar;
    private Integer sex;
    private Integer workCity;
    private Integer bornCity;
    private Date bornDate;
    private Date createTime;
    private Date updateTime;

}
