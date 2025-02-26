package cn.lgyjava.mybatis.session;

import java.util.List;

/**
 * 在 SqlSession 中定义用来执行SQL 获取映射器对象以及后续管理事务操作的标准接口。
 * 通常情况下 我们应用程序中使用 的 MyBatis 的api 就是在这个接口定义的方法
 *
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public interface SqlSession {

	/**
	 * Retrieve a single row mapped from the statement key
	 * 根据指定的SqlID获取一条记录的封装对象
	 *
	 * @param <T>       the returned object type 封装之后的对象类型
	 * @param statement sqlID
	 * @return Mapped object 封装之后的对象
	 */
	<T> T selectOne(String statement);

	/**
	 * Retrieve a single row mapped from the statement key and parameter.
	 * 根据指定的SqlID获取一条记录的封装对象，只不过这个方法容许我们可以给sql传递一些参数
	 * 一般在实际使用中，这个参数传递的是pojo，或者Map或者ImmutableMap
	 *
	 * @param <T>       the returned object type
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @return Mapped object
	 */
	<T> T selectOne(String statement, Object parameter);

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * 获取多条记录，这个方法容许我们可以传递一些参数
	 *
	 * @param <E>       the returned list element type
	 * @param statement Unique identifier matching the statement to use.
	 * @param parameter A parameter object to pass to the statement.
	 * @return List of mapped object
	 */
	<E> List<E> selectList(String statement, Object parameter);

	/**
	 * Execute an insert statement with the given parameter object. Any generated
	 * autoincrement values or selectKey entries will modify the given parameter
	 * object properties. Only the number of rows affected will be returned.
	 * 插入记录，容许传入参数。
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the insert. 注意返回的是受影响的行数
	 */
	int insert(String statement, Object parameter);

	/**
	 * Execute an update statement. The number of rows affected will be returned.
	 * 更新记录
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the update. 返回的是受影响的行数
	 */
	int update(String statement, Object parameter);

	/**
	 * Execute a delete statement. The number of rows affected will be returned.
	 * 删除记录
	 *
	 * @param statement Unique identifier matching the statement to execute.
	 * @param parameter A parameter object to pass to the statement.
	 * @return int The number of rows affected by the delete. 返回的是受影响的行数
	 */
	Object delete(String statement, Object parameter);

	/**
	 * 以下是事务控制方法 commit,rollback
	 * Flushes batch statements and commits database connection.
	 * Note that database connection will not be committed if no updates/deletes/inserts were called.
	 */
	void commit();

	/**
	 * Retrieves current configuration
	 * 得到配置
	 *
	 * @return Configuration
	 */
	Configuration getConfiguration();

	/**
	 * Retrieves a mapper.
	 * 得到映射器，这个巧妙的使用了泛型，使得类型安全
	 *
	 * @param <T>  the mapper type
	 * @param type Mapper interface class
	 * @return a mapper bound to this SqlSession
	 */
	<T> T getMapper(Class<T> type);
}
