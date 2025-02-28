package cn.lgyjava.mybatis.executor.keygen;

import cn.lgyjava.mybatis.executor.Executor;
import cn.lgyjava.mybatis.mapping.MappedStatement;

import java.sql.Statement;

/**
 *不用键值生成器
 * @author liuguanyi
 * * @date 2025/2/28
 */
public class NoKeyGenerator implements KeyGenerator{

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Do Nothing
    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        // Do Nothing
    }

}
