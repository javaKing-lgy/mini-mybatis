package cn.lgyjava.mybatis.builder;

import cn.lgyjava.mybatis.session.Configuration;

/**
 * 构建器的基类 建造者模式
 * @author liuguanyi
 * * @date 2025/1/29
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}

