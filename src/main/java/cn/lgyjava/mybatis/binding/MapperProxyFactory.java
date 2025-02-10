package cn.lgyjava.mybatis.binding;

import cn.lgyjava.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理类创建工厂
 * 主要就是 newProxyInstance
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class MapperProxyFactory<T> {
	private final Class<T> mapperInterface;
	private Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();


	public Map<Method, MapperMethod> getMethodCache() {
		return methodCache;
	}
	public MapperProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	public T newInstance(SqlSession sqlSession) {
		final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);

	}
}
