package edu.xpu.cs.lovexian.app.appadmin.utils;

import lombok.Getter;

/**
 * Description:设备状态枚举表
 * User: 马鹏森 2020-09-18-16:51
 */
@Getter
public enum StatusEnum {


    NORMAL_STATE(0,"状态正常"),
    ABNORMAL_STATE(1,"状态不正常")
    ;


    private Integer code;
    private String msg;

    StatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
