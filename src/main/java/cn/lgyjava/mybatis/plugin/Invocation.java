package cn.lgyjava.mybatis.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 调用信息
 * @author liuguanyi
 * * @date 2025/3/1
 */
public class Invocation {

    // 调用的对象
    private Object target;
    // 调用的方法
    private Method method;
    // 调用的参数
    private Object[] args;

    public Invocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    // 放行；调用执行
    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

}
