package cn.lgyjava.mybatis.binding;


import cn.lgyjava.mybatis.session.SqlSession;
import cn.hutool.core.lang.ClassScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**MapperRegistry 提供包路径的扫描和映射器代理类注册机服务 完成接口对象的代理类注册处理
 * 核心就是提供了 ClassScanner.scanPackage 扫描包路径，调用了 addMapper 方法，
 * 给接口类创建 MapperProxyFactory 映射器代理类 ，并且写入到knownMappers的HashMap 缓存中
 * 另外就是这个类也提供了对应的getMapper 获取映射器代理类的方法
 *
 *
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class MapperRegistry {

	// 将已经添加的映射器代理
	private final Map<Class<?>,MapperProxyFactory<?>> knownMappers = new HashMap<>();

	public <T> T getMapper(Class<T> type, SqlSession sqlSession){
		final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
		if (mapperProxyFactory == null){
			throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
		}
		try{
			return mapperProxyFactory.newInstance(sqlSession);
		}catch (Exception e){
			throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
		}
	}
	public <T> void addMapper(Class<T> type){
		// Mapper 必须是接口才会注册
		if(type.isInterface()){
			if (hasMapper(type)){
				// 如果已经添加过，则不重复添加
				throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
			}
			// 注册映射器代理工厂
			knownMappers.put(type,new MapperProxyFactory<>(type));
		}
	}
	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}


	public void addMappers(String packageName){
		Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
		for(Class<?> mapperClass : mapperSet){
			addMapper(mapperClass);
		}
	}



}
