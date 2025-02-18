package cn.lgyjava.mybatis.transaction;

import cn.lgyjava.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 事务工厂
 * @author liuguanyi
 * * @date 2025/2/18
 * */
public interface TransactionFactory {

    /**
     * 根据 Connection 创建 Transaction
     * @return Transaction
     */
    Transaction newTransaction(Connection conn);

    /**
     * 根据数据源和事务隔离级别创建 Transaction
     * @return Transaction
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);

}
