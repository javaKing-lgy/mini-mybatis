package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.binding.MapperRegistry;
import cn.lgyjava.mybatis.session.SqlSession;
import cn.lgyjava.mybatis.session.SqlSessionFactory;

/**
 * 默认的简单工厂实现 ，处理开启SqlSession时 对DefaultSqlSession的创建
 * 以及传递 mapperRegistry 这样就可以在使用SqlSession 时获取每个代理类的映射器对象
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class DefaultSqlSessionFactory implements SqlSessionFactory {

	private final MapperRegistry mapperRegistry;

	public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
		this.mapperRegistry = mapperRegistry;
	}
	@Override
	public SqlSession openSession() {
		return new DefaultSqlSession(mapperRegistry);
	}
}
