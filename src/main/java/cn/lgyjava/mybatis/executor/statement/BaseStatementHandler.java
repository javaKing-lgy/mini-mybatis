package cn.lgyjava.mybatis.executor.statement;

import cn.lgyjava.mybatis.executor.Executor;
import cn.lgyjava.mybatis.executor.parameter.ParameterHandler;
import cn.lgyjava.mybatis.executor.resultset.ResultSetHandler;
import cn.lgyjava.mybatis.mapping.BoundSql;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.ResultHandler;
import cn.lgyjava.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * statement处理器抽象类
 * @author liuguanyi
 * * @date 2025/2/19
 */
public abstract class BaseStatementHandler implements StatementHandler{

    protected final Configuration configuration;

    /**
     * SQL执行器
     */
    protected final Executor executor;

    /**
     * 映射语句
     */
    protected final MappedStatement mappedStatement;

    /**
     * 结果集处理器
     */
    protected final ResultSetHandler resultSetHandler;

    protected final ParameterHandler parameterHandler;


    /**
     * 绑定SQL
     */
    protected BoundSql boundSql;
    protected final RowBounds rowBounds;

    /**
     * 参数
     */
    protected final Object parameterObject;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, resultHandler, boundSql);
    }
    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化
            statement = instantiateStatement(connection);
            // 设置超时时间
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
