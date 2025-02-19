package cn.lgyjava.mybatis.mapping;

import java.util.Map;

/**
 * 绑定的SQL 从SqlSource解析出来，将所有动态内容都处理完成的得到的SQL语句
 *
 * @author liuguanyi
 * * @date 2025/2/18
 */
public class BoundSql {
    // 最终要执行的SQL
    private String sql;
    // 参数映射
    private Map<Integer, String> parameterMappings;
    // 入参类型
    private String parameterType;
    // 返回类型
    private String resultType;

    public BoundSql(String sql, Map<Integer, String> parameterMappings, String parameterType, String resultType) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterType = parameterType;
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public Map<Integer, String> getParameterMappings() {
        return parameterMappings;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getResultType() {
        return resultType;
    }
}
