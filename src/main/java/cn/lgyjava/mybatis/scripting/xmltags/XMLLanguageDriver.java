package cn.lgyjava.mybatis.scripting.xmltags;

import cn.lgyjava.mybatis.mapping.SqlSource;
import cn.lgyjava.mybatis.scripting.LanguageDriver;
import cn.lgyjava.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * XML 语言驱动器
 * @author liuguanyi
 * * @date 2025/2/23
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
}
