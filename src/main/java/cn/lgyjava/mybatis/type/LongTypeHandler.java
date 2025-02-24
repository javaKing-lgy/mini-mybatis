package cn.lgyjava.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Long 类型
 * @author liuguanyi
 * * @date 2025/2/24
 */
public class LongTypeHandler extends BaseTypeHandler<Long> {

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter);
    }

}
