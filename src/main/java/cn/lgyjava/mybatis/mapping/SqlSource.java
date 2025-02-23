package cn.lgyjava.mybatis.mapping;

/**
 * sql源码
 * @author liuguanyi
 * * @date 2025/2/23
 */
public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
