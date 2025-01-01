package com.douyu.live.id.generate.provider.enums;

import lombok.Getter;

/**
 * @author liuguanyi
 * * @date 2025/1/1
 */
@Getter
public enum MySqlConfigType {
    SEQ_ID(1),
    UN_SEQ_ID(2);

    private final int type;

    MySqlConfigType(int type) {
        this.type = type;
    }

}
