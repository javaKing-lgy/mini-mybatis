package cn.lgyjava.mybatis.reflection.invoker;

import java.lang.reflect.Method;

/**
 * 方法调用者
 * 提供方法反射调用处理，构造函数会传入对应的方法类型。
 * @author liuguanyi
 * * @date 2025/2/19
 */
public class MethodInvoker implements Invoker {
    private Class<?> type;
    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;
        if (method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
