package cn.lgyjava.mybatis.mapping;

import cn.lgyjava.mybatis.session.Configuration;

import java.util.Map;

/** 映射语句类
 * 这个类是模拟Mybatis的MappedStatement类 用于表示一个映射语句
 * 映射语句通常定义在xml文件中
 * @author liuguanyi
 * * @date 2025/1/29
 */
public class MappedStatement {
    // MyBatis的全局配置信息 比如环境变量等等
    private Configuration configuration;
    // 映射语句的唯一标识
    private String id;
    // 表示的SQL类型
    private SqlCommandType sqlCommandType;
    // 传入的SQL语句的参数类型
    private String parameterType;
    // SQL语句执行后返回的结果类型
    private String resultType;
    // 实际的SQL语句
    private String sql;
    // 用于存储SQL中的占位符与java参数的映射关系
    private Map<Integer, String> parameter;

    MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, String parameterType, String resultType, String sql, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameter = parameter;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }

}
