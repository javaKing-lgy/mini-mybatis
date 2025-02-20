package cn.lgyjava.mybatis.reflection.wrapper;

import cn.lgyjava.mybatis.reflection.MetaObject;

/**
 * 对象包装器工厂
 * @author liuguanyi
 * * @date 2025/2/19
 */
public interface ObjectWrapperFactory {

    /**
     * 判断有没有包装器
     */
    boolean hasWrapperFor(Object object);

    /**
     * 得到包装器
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
