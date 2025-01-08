package com.douyu.live.id.generate.provider.service.bo;

import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 本地id生成的一个具体策略 无序id
 *
 * @author liuguanyi
 * * @date 2025/1/1
 */
@Data
public class LocalUnSeqIdBO {
    /**
     * MySQL 配置的 ID
     */
    private Integer id;

    /**
     * 对应分布式 ID 的配置说明
     */
    private String desc;

    /**
     * 链表记录 ID 值
     */
    private ConcurrentLinkedQueue<Long> idQueue;

    /**
     * 本地内存记录 ID 段的开始位置
     */
    private Long currentStart;

    /**
     * 本地内存记录 ID 段的结束位置
     */
    private Long nextThreshold;

    /**
     * 步长
     */
    private Integer step;
}
