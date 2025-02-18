package cn.lgyjava.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据源工厂
 * @author liuguanyi
 * * @date 2025/2/18
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();

}
