package com.douyu.live.id.generate.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * id生成器配置表
 * @author luiguanyi
 * * @date 2024/12/31
 */
@TableName("t_id_generate_config")
@Data
public class IdBuilderPO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * id 备注描述
     */
    private String remark;
    /**
     * 初始化值
     */
    private long initNum;
    /**
     * 步长
     */
    private int step;
    /**
     * 是否是有序的id
     */
    private int isSeq;
    /**
     * 当前id 所在阶段的开始值
     */
    private long currentStart;
    /**
     * 当前id 所在阶段的阈值
     */
    private long nextThreshold;
    /**
     * 业务代码前缀
     */
    private String idPrefix;
    /**
     * 乐观锁版本号
     */
    private int version;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
