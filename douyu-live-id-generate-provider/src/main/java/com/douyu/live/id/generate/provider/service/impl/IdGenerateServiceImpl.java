package com.douyu.live.id.generate.provider.service.impl;

import com.douyu.live.id.generate.provider.dao.mapper.IdBuilderMapper;
import com.douyu.live.id.generate.provider.dao.po.IdBuilderPO;
import com.douyu.live.id.generate.provider.service.IdGenerateService;
import com.douyu.live.id.generate.provider.service.bo.LocalSeqIdBO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * id生成服务实现
 * @author luiguanyi
 * * @date 2024/12/29
 */
@Service
@Slf4j
public class IdGenerateServiceImpl implements IdGenerateService {

    @Resource
    private IdBuilderMapper idBuilderMapper;

    private static final Map<Integer, LocalSeqIdBO> localSeqIdMap = new ConcurrentHashMap<>();



    @Override
    public Long getSeqId(Integer id) {
        if(id == null){
            log.error("[getSeqId] id  is error");
            return null;
        }
        LocalSeqIdBO localSeqIdBO = localSeqIdMap.get(id);
        if(localSeqIdBO == null){
            log.error("[getSeqId] localSeqIdBo is null ,id is {}",id);
            return null;
        }
        return localSeqIdBO.getCurrentNum().getAndIncrement();
    }

    @Override
    public Long getUnSeqId(Integer code) {
        return null;
    }

    /**
     * 初始化我们的localSeqIdMap
     */
    @PostConstruct
    public void init(){
        List<IdBuilderPO> idBuilderPOS = idBuilderMapper.selectAll();
        idBuilderPOS.forEach(idBuilderPO -> {
            // 进行更新数据库中当前id 的配置 就是修改表中当前的id段 往后面挪一个步长
            Integer updateResult = idBuilderMapper.updateCurrentThreshold(idBuilderPO.getNextThreshold(), idBuilderPO.getNextThreshold() + idBuilderPO.getStep(), idBuilderPO.getId(), idBuilderPO.getVersion());
            if (updateResult == 0){
                IdBuilderPO newIdBuilderPO = idBuilderMapper.selectById(idBuilderPO.getId());
            }
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            localSeqIdBO.setId(idBuilderPO.getId());
            localSeqIdBO.setCurrentNum(new AtomicLong(idBuilderPO.getCurrentStart()));
            localSeqIdMap.put(idBuilderPO.getId(),localSeqIdBO);
        });
    }
}
