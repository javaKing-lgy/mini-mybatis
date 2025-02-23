package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.executor.Executor;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.SqlSession;

import java.util.List;

/**
 * 通过 DefaultSqlSession 实现类 对SqlSession 接口进行实现。
 * getMapper方法中 获取映射器对象是通过配置类进行获取的。
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class DefaultSqlSession implements SqlSession {

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
		MappedStatement ms = configuration.getMappedStatement(statement);
		List<T> list = executor.query(ms, parameter, Executor.NO_RESULT_HANDLER, ms.getSqlSource().getBoundSql(parameter));
		return list.get(0);
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
