package cn.lgyjava.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 参数处理器
 * @author liuguanyi
 * * @date 2025/2/24
 */
public interface ParameterHandler {

    /**
     * 获取参数
     */
    Object getParameterObject();

    /**
     * 设置参数
     */
    void setParameters(PreparedStatement ps) throws SQLException;

}
