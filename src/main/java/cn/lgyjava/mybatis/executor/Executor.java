package cn.lgyjava.mybatis.executor;

import cn.lgyjava.mybatis.mapping.BoundSql;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.session.ResultHandler;
import cn.lgyjava.mybatis.session.RowBounds;
import cn.lgyjava.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * SQL执行器
 * @author liuguanyi
 * * @date 2025/2/19
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    int update(MappedStatement ms, Object parameter) throws SQLException;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
