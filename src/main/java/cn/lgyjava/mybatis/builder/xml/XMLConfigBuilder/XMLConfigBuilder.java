package cn.lgyjava.mybatis.builder.xml.XMLConfigBuilder;

import cn.lgyjava.mybatis.builder.BaseBuilder;
import cn.lgyjava.mybatis.datasource.DataSourceFactory;
import cn.lgyjava.mybatis.io.Resources;
import cn.lgyjava.mybatis.mapping.BoundSql;
import cn.lgyjava.mybatis.mapping.Environment;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.mapping.SqlCommandType;
import cn.lgyjava.mybatis.session.Configuration;
import cn.lgyjava.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * XMLConfigBuilder 核心操作就是在初始化 Configuration 因为Configuration
 * 使用 解析XML 和存放 都是最近的操作 所以放在这里比较合适
 * 之后就是 具体的 parse 解析，并且把解析的信息 通过configuration 添加到Configuration中
 * 包括 添加的解析 SQL 注册Mapper 映射器
 * 解析配置整体包括 类型别名 插件 对象工厂 对象包装工厂 设置 环境 类型转换 映射器
 * @author liuguanyi
 * * @date 2025/1/27
 */
public class XMLConfigBuilder extends BaseBuilder{

    private Element root;

    public XMLConfigBuilder(Reader reader){
        // 1 调用父类初始化 Configuration
        super(new Configuration());
        // 2 dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        }catch (DocumentException e){
            e.printStackTrace();
        }
    }
    public Configuration parse(){
        try {

            // 解析数据源等等
            environmentsElement(root.element("environments"));

            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }
    private void mapperElement(Element mappers) throws Exception {
        // 读取所有的mapper元素列表
        List<Element> mapperList = mappers.elements("mapper");
        // 遍历mapper元素 对于每一个mapper元素 获取其resource值
        // 获取reader对象
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            //解析映射器xml文件
            Document document = saxReader.read(new InputSource(reader));

            Element root = document.getRootElement();
            //命名空间
            String namespace = root.attributeValue("namespace");

            // SELECT
            List<Element> selectNodes = root.elements("select");
            for (Element node : selectNodes) {
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                // ? 匹配
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }

                String msId = namespace + "." + id;
                String nodeName = node.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
                BoundSql boundSql = new BoundSql(sql, parameter, parameterType, resultType);
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

    /**
     * 解析环境标签 以及里面的信息
     * @param context
     * @throws Exception
     */
    private void environmentsElement(Element context) throws Exception {
        String environment = context.attributeValue("default");

        List<Element> environmentList = context.elements("environment");
        for (Element e : environmentList) {
            String id = e.attributeValue("id");
            if (environment.equals(id)) {
                // 事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();

                // 数据源
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type")).newInstance();
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }
}
