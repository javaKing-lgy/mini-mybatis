package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.SqlSession;

/**
 * 通过 DefaultSqlSession 实现类 对SqlSession 接口进行实现。
 * getMapper方法中 获取映射器对象是通过配置类进行获取的。
 * 在selectOne 中是一段简单的内容返回 目前还没有与数据库进行关联
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class DefaultSqlSession implements SqlSession {

	private Configuration configuration;

	public DefaultSqlSession(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public <T> T selectOne(String statement) {
		return (T) ("你被代理了！" + statement);
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		MappedStatement mappedStatement = configuration.getMappedStatement(statement);
		return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
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
