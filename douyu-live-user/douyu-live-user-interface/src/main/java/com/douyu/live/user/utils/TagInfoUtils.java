package com.douyu.live.user.utils;

/**
 * 标签工具类
 *
 * @author liuguanyi
 * * @date 2025/1/5
 */
public class TagInfoUtils {
    public static boolean isContain(Long tagInfo, Long matchTag) {
        return tagInfo != null && matchTag != null && matchTag >0 && (tagInfo & matchTag) == matchTag;
    }
}
