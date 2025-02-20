package cn.lgyjava.mybatis.reflection.wrapper;

import cn.lgyjava.mybatis.reflection.MetaObject;

/**
 * 默认对象包装器
 * @author liuguanyi
 * * @date 2025/2/19
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }

}
