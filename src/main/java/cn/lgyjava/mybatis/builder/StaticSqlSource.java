package cn.lgyjava.mybatis.builder;

import cn.lgyjava.mybatis.mapping.BoundSql;
import cn.lgyjava.mybatis.mapping.ParameterMapping;
import cn.lgyjava.mybatis.mapping.SqlSource;
import cn.lgyjava.mybatis.session.Configuration;

import java.util.List;

/**
 * 静态SQL
 * @author liuguanyi
 * * @date 2025/2/23
 */
public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }

}
