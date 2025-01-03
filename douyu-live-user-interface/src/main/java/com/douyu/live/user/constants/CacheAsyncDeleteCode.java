package com.douyu.live.user.constants;

import lombok.Getter;

/**
 * @author liuguanyi
 * * @date 2025/1/3
 */
@Getter
enum CacheAsyncDeleteCode {

    USER_INFO_DELETE(0, "用户基础信息删除"),
    USER_TAG_DELETE(1, "用户标签删除");

    int code;
    String desc;

    CacheAsyncDeleteCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
