package cn.lgyjava.mybatis.session.defaults;

import cn.lgyjava.mybatis.executor.Executor;
import cn.lgyjava.mybatis.mapping.Environment;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.SqlSession;
import cn.lgyjava.mybatis.session.SqlSessionFactory;
import cn.lgyjava.mybatis.session.TransactionIsolationLevel;
import cn.lgyjava.mybatis.transaction.Transaction;
import cn.lgyjava.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * 默认的简单工厂实现 ，处理开启SqlSession时 对DefaultSqlSession的创建
 * 以及传递 mapperRegistry 这样就可以在使用SqlSession 时获取每个代理类的映射器对象
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public class DefaultSqlSessionFactory implements SqlSessionFactory {


	private final Configuration configuration;

	public DefaultSqlSessionFactory(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public SqlSession openSession() {
		Transaction tx = null;
		try {
			final Environment environment = configuration.getEnvironment();
			TransactionFactory transactionFactory = environment.getTransactionFactory();
			tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
			// 创建执行器
			Executor executor = configuration.newExecutor(tx);
			return new DefaultSqlSession(configuration, executor);
		} catch (Exception e) {
			try {
				assert tx != null;
				tx.close();
			} catch (SQLException e1) {
				//ignore
			}
			throw new RuntimeException("Error opening session.  Cause: " + e);
		}
	}

}
