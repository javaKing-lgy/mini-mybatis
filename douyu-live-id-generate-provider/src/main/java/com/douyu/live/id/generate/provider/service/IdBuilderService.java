package com.douyu.live.id.generate.provider.service;

/**
 * id生成服务
 * @author luiguanyi
 * * @date 2024/12/29
 */
public interface IdBuilderService {
    /**
     * 生成有序id
     *
     * @param code
     * @return
     */
    Long getSeqId(Integer code);
    /**
     * 生成无序id
     *
     * @param code
     * @return
     */
    Long getUnSeqId(Integer code);
}
