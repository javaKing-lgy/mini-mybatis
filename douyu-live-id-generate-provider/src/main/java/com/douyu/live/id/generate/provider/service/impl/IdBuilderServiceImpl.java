package com.douyu.live.id.generate.provider.service.impl;

import com.douyu.live.id.generate.provider.dao.mapper.IdBuilderMapper;
import com.douyu.live.id.generate.provider.dao.po.IdBuilderPO;
import com.douyu.live.id.generate.provider.enums.MySqlConfigType;
import com.douyu.live.id.generate.provider.service.IdBuilderService;
import com.douyu.live.id.generate.provider.service.bo.LocalSeqIdBO;
import com.douyu.live.id.generate.provider.service.bo.LocalUnSeqIdBO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * id生成服务实现
 *
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

    private static Map<Integer, LocalSeqIdBO> localSeqIdMap = new ConcurrentHashMap<>();

    private static Map<Integer, LocalUnSeqIdBO> localUnSeqIdMap = new ConcurrentHashMap<>();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), r -> {
        Thread thread = new Thread(r);
        // 打印日志 方便我们查看
        thread.setName("id-builder-thread-" + ThreadLocalRandom.current().nextInt(1000));
        return thread;
    });

    private static Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            log.error("[getSeqId] id  is error");
            return null;
        }
        LocalSeqIdBO localSeqIdBO = localSeqIdMap.get(id);
        if (localSeqIdBO == null) {
            log.error("[getSeqId] localSeqIdBo is null ,id is {}", id);
            return null;
        }
        refreshLocalSeqIdMap(localSeqIdBO);
        long returnId = localSeqIdBO.getCurrentValue().incrementAndGet();
        // 进行一个范围校验
        if (returnId > localSeqIdBO.getNextThreshold()) {
            log.error("[getSeqId] localSeqIdBo is over limit ,id is {}", id);
            return null;
        }
        return returnId;
    }

    @Override
    public Long getUnSeqId(Integer id) {
        if (id == null) {
            log.error("[getUnSeqId] id  is error");
            return null;
        }
        LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdMap.get(id);
        if (localUnSeqIdBO == null) {
            log.error("[getUnSeqId] localUnSeqIdBo is null ,id is {}", id);
            return null;
        }
        refreshLocalUnSeqIdMap(localUnSeqIdBO);
        Long returnId = localUnSeqIdBO.getIdQueue().poll();
        if (returnId == null) {
            log.error("[getUnSeqId] localUnSeqIdBo is over limit ,id is {}", id);
            return null;
        }

        return returnId;
    }

    /**
     * 刷新本地无序id段
     *
     * @param localUnSeqIdBO
     */
    private void refreshLocalUnSeqIdMap(LocalUnSeqIdBO localUnSeqIdBO) {
        long begin = localUnSeqIdBO.getCurrentStart();
        long end = localUnSeqIdBO.getNextThreshold();
        long remainSize = localUnSeqIdBO.getIdQueue().size();
        if ((end - begin) * (1.00 - updateRate) > remainSize) {
            Semaphore semaphore = semaphoreMap.get(localUnSeqIdBO.getId());
            if (semaphore == null) {
                log.error("semaphore is null id is {}", localUnSeqIdBO.getId());
                return;
            }
            boolean acquire = semaphore.tryAcquire();
            if (acquire) {
                log.info("[refreshLocalUnSeqIdMap] refresh localUnSeqIdBO ,id is {}", localUnSeqIdBO.getId());
                executor.execute(() -> {
                    try {
                        IdBuilderPO idBuilderPO = idBuilderMapper.selectById(localUnSeqIdBO.getId());
                        tryUpdateMysqlRecord(idBuilderPO);
                        log.info("[refreshLocalSeqIdMap] refresh localUnSeqIdBO success ,id is {}", localUnSeqIdBO.getId());
                    } catch (Exception e) {
                        log.error("[refreshLocalSeqIdMap] refresh localUnSeqIdBO error ,id is {}", localUnSeqIdBO.getId());
                        return;
                    } finally {
                        semaphore.release();
                    }
                });
            }
        }

    }

    /**
     * 根据设置的提前刷新阈值进行刷新，刷新本地id段
     *
     * @param localSeqIdBO
     */
    private void refreshLocalSeqIdMap(LocalSeqIdBO localSeqIdBO) {
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        if (localSeqIdBO.getCurrentValue().get() - localSeqIdBO.getCurrentStart() > step * updateRate) {
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
            if (acquire) {
                log.info("[refreshLocalSeqIdMap] refresh localSeqIdBO ,id is {}", localSeqIdBO.getId());
                executor.execute(() -> {
                    try {
                        IdBuilderPO idBuilderPO = idBuilderMapper.selectById(localSeqIdBO.getId());
                        tryUpdateMysqlRecord(idBuilderPO);
                        log.info("[refreshLocalSeqIdMap] refresh localSeqIdBO success ,id is {}", localSeqIdBO.getId());
                    } catch (Exception e) {
                        log.error("[refreshLocalSeqIdMap] refresh localSeqIdBO error ,id is {}", localSeqIdBO.getId());
                        return;
                    } finally {
                        semaphore.release();
                    }
                });
            }
        }
    }


    /**
     * 初始化我们的localSeqIdMap
     */
    @PostConstruct
    public void init() {
        List<IdBuilderPO> idBuilderPOS = idBuilderMapper.selectAll();
        idBuilderPOS.forEach((idBuilderPO) -> {
            tryUpdateMysqlRecord(idBuilderPO);
            semaphoreMap.put(idBuilderPO.getId(), new Semaphore(1));
        });
    }

    /**
     * 尝试更新mysql记录
     *
     * @param idBuilderPO
     */
    private void tryUpdateMysqlRecord(IdBuilderPO idBuilderPO) {
        Integer updateResult = idBuilderMapper.updateCurrentThreshold(idBuilderPO.getNextThreshold() + idBuilderPO.getStep(), idBuilderPO.getNextThreshold(), idBuilderPO.getId(), idBuilderPO.getVersion());
        if (updateResult > 0) {
            localIdBOMap(idBuilderPO);
            return;
        }
        // 这里是更新时 产生冲突 更新失败 重试三次
        for (int i = 0; i < 3; i++) {
            idBuilderPO = idBuilderMapper.selectById(idBuilderPO.getId());
            updateResult = idBuilderMapper.updateCurrentThreshold(idBuilderPO.getNextThreshold() + idBuilderPO.getStep(), idBuilderPO.getNextThreshold(), idBuilderPO.getId(), idBuilderPO.getVersion());
            if (updateResult > 0) {
                LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
                localSeqIdBO.setId(idBuilderPO.getId());
                localSeqIdBO.setCurrentValue(new AtomicLong(idBuilderPO.getCurrentStart()));
                localSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
                localSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
                localSeqIdMap.put(idBuilderPO.getId(), localSeqIdBO);
                return;
            }
        }
        throw new RuntimeException("表id段占用失败 id is " + idBuilderPO.getId());
    }

    /**
     * 将mysql记录转换为本地记录 放到本地的map中
     *
     * @param idBuilderPO
     */
    private void localIdBOMap(IdBuilderPO idBuilderPO) {
        if (idBuilderPO.getIsSeq() == MySqlConfigType.SEQ_ID.getType()) {
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            localSeqIdBO.setId(idBuilderPO.getId());
            localSeqIdBO.setCurrentValue(new AtomicLong(idBuilderPO.getCurrentStart()));
            localSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
            localSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
            localSeqIdMap.put(idBuilderPO.getId(), localSeqIdBO);
        } else {
            LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
            localUnSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
            localUnSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
            localUnSeqIdBO.setId(idBuilderPO.getId());
            long begin = localUnSeqIdBO.getCurrentStart();
            long end = localUnSeqIdBO.getNextThreshold();
            List<Long> idList = new ArrayList<>();
            for (long i = begin; i < end; i++) {
                idList.add(i);
            }
            // 打乱本地的id段
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>(idList);
            localUnSeqIdBO.setIdQueue(idQueue);
            localUnSeqIdMap.put(idBuilderPO.getId(), localUnSeqIdBO);
        }
    }

}
