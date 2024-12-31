package com.douyu.live.id.generate.provider.rpc;

import com.douyu.live.id.generate.interfaces.IdBuilderRpc;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * id 生成 rpc 实现
 * @author luiguanyi
 * * @date 2024/12/29
 */
@DubboService
public class IdBuilderRpcImpl implements IdBuilderRpc {
    @Override
    public Long getSeqId(Integer id) {
        return null;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return null;
    }
}
