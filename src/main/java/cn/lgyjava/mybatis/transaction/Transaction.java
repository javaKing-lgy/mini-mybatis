package cn.lgyjava.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/** 事务接口
 * @author liuguanyi
 * * @date 2025/2/18
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
