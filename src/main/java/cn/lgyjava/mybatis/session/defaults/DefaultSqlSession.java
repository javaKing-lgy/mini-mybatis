package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.executor.Executor;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.RowBounds;
import cn.lgyjava.mybatis.session.SqlSession;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 通过 DefaultSqlSession 实现类 对SqlSession 接口进行实现。
 * getMapper方法中 获取映射器对象是通过配置类进行获取的。
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class DefaultSqlSession implements SqlSession {

	private Logger logger = LoggerFactory.getLogger(DefaultSqlSession.class);

	private Configuration configuration;
	private Executor executor;

	public DefaultSqlSession(Configuration configuration, Executor executor) {
		this.configuration = configuration;
		this.executor = executor;
	}

	@Override
	public <T> T selectOne(String statement) {
		return this.selectOne(statement, null);
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		List<T> list = this.<T>selectList(statement, parameter);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
		} else {
			return null;
		}
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		logger.info("执行查询 statement：{} parameter：{}", statement, JSON.toJSONString(parameter));
		MappedStatement ms = configuration.getMappedStatement(statement);
		try {
			return executor.query(ms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, ms.getSqlSource().getBoundSql(parameter));
		} catch (SQLException e) {
			throw new RuntimeException("Error querying database.  Cause: " + e);
		}
	}

	@Override
	public int insert(String statement, Object parameter) {
		// 在 Mybatis 中 insert 调用的是 update
		return update(statement, parameter);
	}

	@Override
	public int update(String statement, Object parameter) {
		MappedStatement ms = configuration.getMappedStatement(statement);
		try {
			return executor.update(ms, parameter);
		} catch (SQLException e) {
			throw new RuntimeException("Error updating database.  Cause: " + e);
		}
	}

	@Override
	public Object delete(String statement, Object parameter) {
		return update(statement, parameter);
	}

	@Override
	public void commit() {
		try {
			executor.commit(true);
		} catch (SQLException e) {
			throw new RuntimeException("Error committing transaction.  Cause: " + e);
		}
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		return configuration.getMapper(type, this);
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

}
