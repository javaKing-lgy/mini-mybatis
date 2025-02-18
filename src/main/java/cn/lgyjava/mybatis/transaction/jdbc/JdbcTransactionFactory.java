package cn.lgyjava.mybatis.transaction.jdbc;

import cn.lgyjava.mybatis.session.TransactionIsolationLevel;
import cn.lgyjava.mybatis.transaction.Transaction;
import cn.lgyjava.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * JdbcTransactionFactory
 * @author liuguanyi
 * * @date 2025/2/18
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }

}
