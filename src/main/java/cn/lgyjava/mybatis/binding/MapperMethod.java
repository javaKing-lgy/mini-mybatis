package cn.lgyjava.mybatis.binding;

import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.mapping.SqlCommandType;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * 这个类代表映射器接口中的一个方法。它内部封装了一个SqlCommand对象，该对象包含了执行这个方法所需要的SQL命令信息。
 * 构造函数接收映射器接口类(mapperInterface)、方法(method)和配置(configuration)作为参数，通过这些信息构建一个SqlCommand对象。
 * 映射器的方法
 * @author liuguanyi
 * * @date 2025/1/30
 */
public class MapperMethod {

    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (command.getType()) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(), args);
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;
    }

    /**
     * SQL 指令
     * 封装了执行SQL命令所需的信息 包括SQL命令的名称和类型 通过MapperStatement 对象获取
     */
    public static class SqlCommand {

        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(statementName);
            name = ms.getId();
            type = ms.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

}
