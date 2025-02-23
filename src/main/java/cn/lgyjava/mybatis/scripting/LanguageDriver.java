package cn.lgyjava.mybatis.scripting;

import cn.lgyjava.mybatis.mapping.SqlSource;
import cn.lgyjava.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * 脚本语言驱动器
 * @author liuguanyi
 * * @date 2025/2/23
 */
public interface LanguageDriver {

    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

}
