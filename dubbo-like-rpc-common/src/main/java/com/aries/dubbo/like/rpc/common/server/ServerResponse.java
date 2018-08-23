package com.aries.dubbo.like.rpc.common.server;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
@SuppressWarnings("unchecked")
public class ServerResponse {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String errorMessage;
    @Setter
    private Object responseData;

    public <T> T getResponseData() {
        return (T) this.responseData;
    }
}
