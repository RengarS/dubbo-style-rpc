package com.aries.dubbo.like.rpc.common.client;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
@Data
public class ServerRequest {
    private String id;
    private Class<?> clz;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;
}
