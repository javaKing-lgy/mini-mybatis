package cn.lgyjava.mybatis.builder;

import cn.lgyjava.mybatis.mapping.ResultMap;
import cn.lgyjava.mybatis.mapping.ResultMapping;

import java.util.List;

/**
 * 结果映射解析器
 * @author liuguanyi
 * * @date 2025/2/28
 */
public class ResultMapResolver {

    private final MapperBuilderAssistant assistant;
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;

    public ResultMapResolver(MapperBuilderAssistant assistant, String id, Class<?> type, List<ResultMapping> resultMappings) {
        this.assistant = assistant;
        this.id = id;
        this.type = type;
        this.resultMappings = resultMappings;
    }

    public ResultMap resolve() {
        return assistant.addResultMap(this.id, this.type, this.resultMappings);
    }

}
