package cn.lgyjava.mybatis.binding;

import cn.lgyjava.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** 映射器代理类
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class MapperProxy<T> implements InvocationHandler , Serializable {

	private final static long serialVersionUID = -6424540398559729838L;
	private SqlSession sqlSession;
	private final Class<T> mapperInterface;

	public MapperProxy(Class<T> mapperInterface, SqlSession sqlSession) {
		this.mapperInterface = mapperInterface;
		this.sqlSession = sqlSession;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(Object.class.equals(method.getDeclaringClass())){
			return method.invoke(this,args);
		}else {
			return sqlSession.selectOne(method.getName(),args);
		}
	}
}
