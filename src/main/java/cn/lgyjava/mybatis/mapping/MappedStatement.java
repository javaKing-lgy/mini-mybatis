package cn.lgyjava.mybatis.mapping;

import cn.lgyjava.mybatis.session.Configuration;

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

    private BoundSql boundSql;

    MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
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

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

}
