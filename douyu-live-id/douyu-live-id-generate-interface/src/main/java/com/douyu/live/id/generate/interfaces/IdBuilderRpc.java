package com.douyu.live.id.generate.interfaces;

/**
 * id 生成接口
 * @author luiguanyi
 * * @date 2024/12/29
 */
public interface IdBuilderRpc {

    /**
     * 获取有序ID
     * @param id
     * @return
     */
    Long getSeqId(Integer id);

    /**
     * 获取无序ID
     * @param id
     * @return
     */
    Long getUnSeqId(Integer id);
}
