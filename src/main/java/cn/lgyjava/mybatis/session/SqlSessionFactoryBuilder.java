package cn.lgyjava.mybatis.session;

import cn.lgyjava.mybatis.builder.xml.XMLConfigBuilder;
import cn.lgyjava.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * SqlSessionFactory建造者工厂
 * SqlSessionFactoryBuilder 是作为整个Mybatis 的入口雷 通过制定解析 XML 的IO 引导整个流程的启动
 * 从这个类开始新增加了XMLConfigBuilder 和 Configuration两个处理类 分别用于解析 XML 文件和生成 Configuration 对象
 * @author liuguanyi
 * * @date 2025/1/27
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(Reader reader){
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }
    public SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionFactory(configuration);
    }
}
