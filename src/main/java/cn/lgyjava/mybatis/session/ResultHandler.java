package cn.lgyjava.mybatis.session;

/**
 * 结果处理器
 * @author liuguanyi
 * * @date 2025/2/19
 */
public interface ResultHandler {

    /**
     * 处理结果
     */
    void handleResult(ResultContext context);

}
