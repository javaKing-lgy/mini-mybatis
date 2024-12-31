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
     * 业务ID
     */
    private int id;


    /**
     * 在内存中记录的当前的值
     */
    private AtomicLong currentNum;
}
