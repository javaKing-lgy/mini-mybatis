package com.douyu.live.user.interfaces.rpc;

import com.douyu.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface IUserRpc {
    /**
     * 测试方法，用于执行测试以验证功能的正确性
     */
    void test();

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID，唯一标识一个用户
     * @return UserDTO对象，包含用户详细信息如果用户不存在，则返回null
     */
    UserDTO getUserById(Long userId);

    /**
     * 更新用户信息
     * @param userDTO 包含更新后用户信息的UserDTO对象
     * @return 更新操作的成功与否true表示成功，false表示失败
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 插入一个新的用户记录
     * @param userDTO 包含新用户信息的UserDTO对象
     * @return 插入操作的成功与否true表示成功，false表示失败
     */
    boolean insertOne(UserDTO userDTO);

    /**
     * 批量查询用户信息通过用户ID列表
     * @param userIds 用户ID列表，用于指定需要查询的用户
     * @return 包含用户信息的映射表，键为用户ID，值为对应的UserDTO对象如果用户不存在，则不包含在返回的映射表中
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds);
}
