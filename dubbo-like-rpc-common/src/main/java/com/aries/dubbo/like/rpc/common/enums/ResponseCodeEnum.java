package com.aries.dubbo.like.rpc.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public enum ResponseCodeEnum {
    OK(200),
    ERROR(500);

    ResponseCodeEnum(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
