package cn.lgyjava.mybatis.reflection.invoker;

/**
 * 调用者
 * @author liuguanyi
 * * @date 2025/2/19
 */
public interface Invoker {
    /**
     * 调用 任何类型的反射调用 都包含对象和入参
     * @param target 对象
     * @param args 参数
     * @return {@link java.lang.Object}
     * @throws java.lang.Exception
     */
    Object invoke(Object target, Object[] args) throws Exception;

    Class<?> getType();

}
