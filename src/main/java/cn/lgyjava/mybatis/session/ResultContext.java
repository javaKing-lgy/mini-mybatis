package cn.lgyjava.mybatis.session;

/**
 * 结果上下文
 * @author liuguanyi
 * * @date 2025/2/26
 */
public interface ResultContext {

    /**
     * 获取结果
     */
    Object getResultObject();

    /**
     * 获取记录数
     */
    int getResultCount();

}
