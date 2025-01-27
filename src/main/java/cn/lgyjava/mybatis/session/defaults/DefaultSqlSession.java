package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.binding.MapperRegistry;
import cn.lgyjava.mybatis.session.SqlSession;

/**
 * 通过 DefaultSqlSession 实现类 对SqlSession 接口进行实现。
 * getMapper方法中 获取映射器对象是通过MapperRegistry类进行获取的,后续这部分会被配置类进行替换。
 * 在selectOne 中是一段简单的内容返回 目前还没有与数据库进行关联
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class DefaultSqlSession implements SqlSession {

	/**
	 * 映射器注册机
	 */
	private MapperRegistry mapperRegistry;

	public DefaultSqlSession(MapperRegistry mapperRegistry) {
		this.mapperRegistry = mapperRegistry;
	}

	@Override
	public <T> T selectOne(String statement) {
		return (T) ("你被代理了！" + statement);
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return (T) ("你被代理了！" + "方法：" + statement + " 入参：" + parameter);
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		return mapperRegistry.getMapper(type, this);
	}

}
