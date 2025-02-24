package cn.lgyjava.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * String 类型
 * @author liuguanyi
 * * @date 2025/2/24
 */
public class StringTypeHandler extends BaseTypeHandler<String>{

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

}
