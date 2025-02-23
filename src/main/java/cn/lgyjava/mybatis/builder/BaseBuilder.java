package cn.lgyjava.mybatis.builder;

import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.type.TypeAliasRegistry;
import cn.lgyjava.mybatis.type.TypeHandlerRegistry;

/**
 * 构建器的基类 建造者模式
 * @author liuguanyi
 * * @date 2025/1/29
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }

}

