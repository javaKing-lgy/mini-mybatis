package cn.lgyjava.mybatis.test;

import cn.lgyjava.mybatis.binding.MapperRegistry;
import cn.lgyjava.mybatis.session.SqlSession;
import cn.lgyjava.mybatis.session.SqlSessionFactory;
import cn.lgyjava.mybatis.session.defaults.DefaultSqlSessionFactory;
import cn.lgyjava.mybatis.test.dao.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/

public class CjTest {
	private Logger logger = LoggerFactory.getLogger(CjTest.class);

	/**
	 * 在单元测试中通过注册机扫描包路径注册映射器代理对象 并且把注册机传递给SqlSessionFactory
	 工厂 ，这样完成一个连接过程。
	 之后通过SqlSession获取对应的DAO类型的实现类，并且进行方法验证。
	 */
	@Test
	public void test_MapperProxyFactory(){
		// 1. 注册Mapper
		MapperRegistry registry = new MapperRegistry();
		registry.addMappers("cn.bugstack.mybatis.test.dao");
		// 2. 从 SqlSession 工厂获取 Session
		SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		// 3. 获取映射器对象
		IUserDao userDao = sqlSession.getMapper(IUserDao.class);
		// 4. 测试验证
		String res = userDao.queryUserName("10001");
		logger.info("测试结果：{}", res);

	}

}
