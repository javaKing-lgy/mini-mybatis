package cn.lgyjava.mybatis.datasource.pooled;

import cn.lgyjava.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * 池化数据源工厂
 * @author liuguanyi
 * * @date 2025/2/18
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }

}
