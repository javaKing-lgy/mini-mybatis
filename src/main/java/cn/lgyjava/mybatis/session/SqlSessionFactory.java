package cn.lgyjava.mybatis.session;

/** SqlSessionFactory工厂
 * @Author liuguanyi
 * @Date 2025/1/26 上午10:57
 **/
public interface SqlSessionFactory {
	SqlSession openSession();
}
