package cn.lgyjava.mybatis.plugin;

/**
 * 方法签名
 * @author liuguanyi
 * * @date 2025/3/1
 */
public @interface Signature {

    /**
     * 被拦截类
     */
    Class<?> type();

    /**
     * 被拦截类的方法
     */
    String method();

    /**
     * 被拦截类的方法的参数
     */
    Class<?>[] args();

}