package cn.lgyjava.mybatis.builder;

import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.type.TypeAliasRegistry;

/**
 * 构建器的基类 建造者模式
 * @author liuguanyi
 * * @date 2025/1/29
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}

