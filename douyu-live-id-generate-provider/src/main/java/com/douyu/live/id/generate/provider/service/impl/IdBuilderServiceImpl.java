package com.douyu.live.id.generate.provider.service.impl;

import com.douyu.live.id.generate.provider.dao.mapper.IdBuilderMapper;
import com.douyu.live.id.generate.provider.dao.po.IdBuilderPO;
import com.douyu.live.id.generate.provider.service.IdBuilderService;
import com.douyu.live.id.generate.provider.service.bo.LocalSeqIdBO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * id生成服务实现
 * @author luiguanyi
 * * @date 2024/12/29
 */
@Service
@Slf4j
public class IdBuilderServiceImpl implements IdBuilderService {

    @Resource
    private IdBuilderMapper idBuilderMapper;

    @Value("${localId.update}")
    private float updateRate;

    private static  Map<Integer, LocalSeqIdBO> localSeqIdMap = new ConcurrentHashMap<>();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), r -> {
        Thread thread = new Thread(r);
        // 打印日志 方便我们查看
        thread.setName("id-builder-thread-"+ ThreadLocalRandom.current().nextInt(1000));
        return thread;
    });

    private static  Map<Integer,Semaphore> semaphoreMap = new ConcurrentHashMap<>();

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
        refreshLocalSeqIdMap(localSeqIdBO);
        // 进行一个范围校验
        if(localSeqIdBO.getCurrentValue().get() > localSeqIdBO.getNextThreshold()){
            log.error("[getSeqId] localSeqIdBo is over limit ,id is {}",id);
            return null;
        }
        return localSeqIdBO.getCurrentValue().getAndIncrement();
    }

    @Override
    public Long getUnSeqId(Integer code) {
        return null;
    }

    /**
     * 根据设置的提前刷新阈值进行刷新，刷新本地id段
     * @param localSeqIdBO
     */
    private void refreshLocalSeqIdMap(LocalSeqIdBO localSeqIdBO){
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        if(localSeqIdBO.getCurrentValue().get() - localSeqIdBO.getCurrentStart() > step * updateRate){
            //同步刷新不推荐
//            IdBuilderPO idBuilderPO = idBuilderMapper.selectById(localSeqIdBO.getId());
//            tryUpdateMysqlRecord(idBuilderPO);
            // 推荐使用异步刷新
            Semaphore semaphore = semaphoreMap.get(localSeqIdBO.getId());
            if (semaphore == null) {
                log.error("semaphore is null id is {}", localSeqIdBO.getId());
                return;
            }
            boolean acquire = semaphore.tryAcquire();
            if(acquire) {
                log.info("同步开始");
                executor.execute(() -> {
                    IdBuilderPO idBuilderPO = idBuilderMapper.selectById(localSeqIdBO.getId());
                    tryUpdateMysqlRecord(idBuilderPO);
                    log.info("同步结束");
                });
            }
        }
    }



    /**
     * 初始化我们的localSeqIdMap
     */
    @PostConstruct
    public void init(){
        List<IdBuilderPO> idBuilderPOS = idBuilderMapper.selectAll();
        idBuilderPOS.forEach((idBuilderPO)->{
            tryUpdateMysqlRecord(idBuilderPO);
            semaphoreMap.put(idBuilderPO.getId(),new Semaphore(1));
        });
    }

    /**
     * 尝试更新mysql记录
     * @param idBuilderPO
     */
    private void tryUpdateMysqlRecord(IdBuilderPO idBuilderPO){
        Integer updateResult = idBuilderMapper.updateCurrentThreshold(idBuilderPO.getNextThreshold() + idBuilderPO.getStep(), idBuilderPO.getNextThreshold(),  idBuilderPO.getId(), idBuilderPO.getVersion());
        if (updateResult > 0){
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            localSeqIdBO.setId(idBuilderPO.getId());
            localSeqIdBO.setCurrentValue(new AtomicLong(idBuilderPO.getCurrentStart()));
            localSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
            localSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
            localSeqIdMap.put(idBuilderPO.getId(),localSeqIdBO);
            return;
        }
        // 这里是更新时 产生冲突 更新失败 重试三次
        for (int i = 0; i < 3; i++) {
            idBuilderPO = idBuilderMapper.selectById(idBuilderPO.getId());
            updateResult = idBuilderMapper.updateCurrentThreshold(idBuilderPO.getNextThreshold() + idBuilderPO.getStep(), idBuilderPO.getNextThreshold(), idBuilderPO.getId(), idBuilderPO.getVersion());
            if (updateResult > 0){
                LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
                localSeqIdBO.setId(idBuilderPO.getId());
                localSeqIdBO.setCurrentValue(new AtomicLong(idBuilderPO.getCurrentStart()));
                localSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
                localSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
                localSeqIdMap.put(idBuilderPO.getId(),localSeqIdBO);
                return;
            }
        }
        throw new RuntimeException("表id段占用失败 id is "+ idBuilderPO.getId());
    }
}
