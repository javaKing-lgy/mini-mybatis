package com.douyu.live.id.generate.provider.service.bo;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地id生成的一个具体策略
 * @author luiguanyi
 * * @date 2024/12/29
 */
@Data
public class LocalSeqIdBO {

    /**
     * mysql中对应的策略id
     */
    private Integer id;

    /**
     * 策略描述
     */
    private String desc;

    /**
     * 本地内存记录当前id值
     */
    private AtomicLong currentValue;

    /**
     * 本地内存记录id段的开始位置
     */
    private Long currentStart;

    /**
     * 本地内存记录id段的结束位置
     */
    private Long nextThreshold;

    /**
     * 步长
     */
    private Integer step;
}
