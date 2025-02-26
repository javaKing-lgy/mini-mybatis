package cn.lgyjava.mybatis.mapping;

import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.type.JdbcType;
import cn.lgyjava.mybatis.type.TypeHandler;

/**
 * 结果映射
 * @author liuguanyi
 * * @date 2025/2/26
 */
public class ResultMapping {

    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;

    ResultMapping() {
    }

    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();


    }

}
