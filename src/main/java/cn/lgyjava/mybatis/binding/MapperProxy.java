package cn.lgyjava.mybatis.binding;

import cn.lgyjava.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/** 映射器代理类
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class MapperProxy<T> implements InvocationHandler , Serializable {

	private final static long serialVersionUID = -6424540398559729838L;
	private SqlSession sqlSession;
	private final Class<T> mapperInterface;
	private final Map<Method, MapperMethod> methodCache;
	public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
		this.sqlSession = sqlSession;
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(Object.class.equals(method.getDeclaringClass())){
			return method.invoke(this,args);
		}else {
			final MapperMethod mapperMethod = cachedMapperMethod(method);
			return mapperMethod.execute(sqlSession, args);
		}
	}
	/**
	 * 去缓存中找MapperMethod
	 */
	private MapperMethod cachedMapperMethod(Method method) {
		MapperMethod mapperMethod = methodCache.get(method);
		if (mapperMethod == null) {
			//找不到才去new
			mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
			methodCache.put(method, mapperMethod);
		}
		return mapperMethod;
	}
}
