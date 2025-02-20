package cn.lgyjava.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * getter 方法调用者
 * @author liuguanyi
 * * @date 2025/2/19
 */
public class GetFieldInvoker implements Invoker{
    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }


    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
