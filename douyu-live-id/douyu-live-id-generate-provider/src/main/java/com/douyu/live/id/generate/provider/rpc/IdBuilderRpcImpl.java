package com.douyu.live.id.generate.provider.rpc;

import com.douyu.live.id.generate.interfaces.IdBuilderRpc;
import com.douyu.live.id.generate.provider.service.IdBuilderService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * id 生成 rpc 实现
 *
 * @author luiguanyi
 * * @date 2024/12/29
 */
@DubboService
public class IdBuilderRpcImpl implements IdBuilderRpc {

    @Resource
    private IdBuilderService idBuilderService;

    @Override
    public Long getSeqId(Integer id) {
        return idBuilderService.getSeqId(id);
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return idBuilderService.getUnSeqId(id);
    }
}
