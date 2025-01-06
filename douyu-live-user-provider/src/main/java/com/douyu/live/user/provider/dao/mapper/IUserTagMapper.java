package com.douyu.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyu.live.user.provider.dao.po.UserTagPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用户标签mapper
 * @author liuguanyi
 * * @date 2025/1/3
 */
@Mapper
public interface IUserTagMapper extends BaseMapper<UserTagPO> {
    /**
     * 设置标签
     * @param userId 用户id
     * @param fieldName 字段名
     * @param tag 标签
     * @return
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} | #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = 0")
    int setTag(Long userId, String fieldName, long tag);

    /**
     * 取消标签
     * @param userId 用户id
     * @param fieldName 字段名
     * @param tag 标签
     * @return
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} &~ #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = 0 ")
    int cancelTag(Long userId, String fieldName, long tag);
    
    
    
}
