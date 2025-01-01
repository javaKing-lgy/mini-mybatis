package com.douyu.live.id.generate.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyu.live.id.generate.provider.dao.po.IdBuilderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * id 生成器配置表持久层
 * @author luiguanyi
 * * @date 2024/12/31
 */
@Mapper
public interface IdBuilderMapper extends BaseMapper<IdBuilderPO> {

    /**
     * 更新当前阈值 开始 以及版本
     */
    @Update("UPDATE t_id_generate_config set next_threshold=#{nextThreshold},current_start=#{currentStart},version=version+1 " +
            "where id=#{id} and version=#{version}")
    Integer updateCurrentThreshold(@Param("nextThreshold") long nextThreshold, @Param("currentStart") long currentStart,
                                   @Param("id") int id, @Param("version") int version);

    /**
     * 查询所有配置
     */
    @Select("select * from t_id_generate_config")
    List<IdBuilderPO> selectAll();
}
