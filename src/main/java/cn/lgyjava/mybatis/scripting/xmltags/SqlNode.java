package cn.lgyjava.mybatis.scripting.xmltags;

/**
 * SQL节点
 * @author liuguanyi
 * * @date 2025/2/23
 */
public interface SqlNode {

    boolean apply(DynamicContext context);

}
