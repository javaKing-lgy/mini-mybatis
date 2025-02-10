package cn.lgyjava.mybatis.mapping;


/** SQL指令类型
 * @author liuguanyi
 * * @date 2025/1/29
 */
public enum SqlCommandType {

    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查找
     */
    SELECT;

}
