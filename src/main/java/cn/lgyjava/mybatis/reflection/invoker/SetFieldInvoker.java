package cn.lgyjava.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * setter 方法调用者
 * @author liuguanyi
 * * @date 2025/2/19
 */
public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

}