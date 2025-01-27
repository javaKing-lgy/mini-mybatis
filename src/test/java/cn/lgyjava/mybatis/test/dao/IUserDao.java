package cn.lgyjava.mybatis.test.dao;

/**
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public interface IUserDao {
	String queryUserName(String uId);

	Integer queryUserAge(String uId);
}
