package cn.lgyjava.mybatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 代理模式
 * @author liuguanyi
 * * @date 2025/3/1
 */
public class Plugin implements InvocationHandler {

    private Object target;
    private Interceptor interceptor;
    private Map<Class<?>, Set<Method>> signatureMap;

    private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取声明的方法列表
        Set<Method> methods = signatureMap.get(method.getDeclaringClass());
        // 过滤需要拦截的方法
        if (methods != null && methods.contains(method)) {
            // 调用 Interceptor#intercept 插入自己的反射逻辑
            return interceptor.intercept(new Invocation(target, method, args));
        }
        return method.invoke(target, args);
    }

    /**
     * 用代理把自定义插件行为包裹到目标方法中，也就是 Plugin.invoke 的过滤调用
     */
    public static Object wrap(Object target, Interceptor interceptor) {
        // 取得签名Map
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        // 取得要改变行为的类(ParameterHandler|ResultSetHandler|StatementHandler|Executor)，目前只添加了 StatementHandler
        Class<?> type = target.getClass();
        // 取得接口
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        // 创建代理(StatementHandler)
        if (interfaces.length > 0) {
            // Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
            return Proxy.newProxyInstance(
                    type.getClassLoader(),
                    interfaces,
                    new Plugin(target, interceptor, signatureMap));
        }
        return target;
    }

    /**
     * 获取方法签名组 Map
     */
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        // 取 Intercepts 注解，例子可参见 TestPlugin.java
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
        // 必须得有 Intercepts 注解，没有报错
        if (interceptsAnnotation == null) {
            throw new RuntimeException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
        }
        // value是数组型，Signature的数组
        Signature[] sigs = interceptsAnnotation.value();
        // 每个 class 类有多个可能有多个 Method 需要被拦截
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
        for (Signature sig : sigs) {
            Set<Method> methods = signatureMap.computeIfAbsent(sig.type(), k -> new HashSet<>());
            try {
                // 例如获取到方法；StatementHandler.prepare(Connection connection)、StatementHandler.parameterize(Statement statement)...
                Method method = sig.type().getMethod(sig.method(), sig.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
            }
        }
        return signatureMap;
    }

    /**
     * 取得接口
     */
    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                // 拦截 ParameterHandler|ResultSetHandler|StatementHandler|Executor
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

}
