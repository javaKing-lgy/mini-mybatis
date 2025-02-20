package cn.lgyjava.mybatis.reflection;

import cn.lgyjava.mybatis.reflection.factory.DefaultObjectFactory;
import cn.lgyjava.mybatis.reflection.factory.ObjectFactory;
import cn.lgyjava.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.lgyjava.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * system原对象
 * @author liuguanyi
 * * @date 2025/2/19
 */
public class SystemMetaObject {

    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    /**
     * 空对象
     */
    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }

}
