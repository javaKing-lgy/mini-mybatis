package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.mapping.BoundSql;
import cn.lgyjava.mybatis.mapping.Environment;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过 DefaultSqlSession 实现类 对SqlSession 接口进行实现。
 * getMapper方法中 获取映射器对象是通过配置类进行获取的。
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
		try {
			// MappedStatement封装了SQL语句的所有重要信息
			MappedStatement mappedStatement = configuration.getMappedStatement(statement);
			// 环境类里有我们数据源信息
			Environment environment = configuration.getEnvironment();
			Connection connection = environment.getDataSource().getConnection();
			BoundSql boundSql = mappedStatement.getBoundSql();
			PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
			preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
			ResultSet resultSet = preparedStatement.executeQuery();

			List<T> objList = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
			return objList.get(0);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
		List<T> list = new ArrayList<>();
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			// 每次遍历行值
			while (resultSet.next()) {
				T obj = (T) clazz.newInstance();
				for (int i = 1; i <= columnCount; i++) {
					Object value = resultSet.getObject(i);
					String columnName = metaData.getColumnName(i);
					String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
					Method method;
					if (value instanceof Timestamp) {
						method = clazz.getMethod(setMethod, Date.class);
					} else {
						method = clazz.getMethod(setMethod, value.getClass());
					}
					method.invoke(obj, value);
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
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
