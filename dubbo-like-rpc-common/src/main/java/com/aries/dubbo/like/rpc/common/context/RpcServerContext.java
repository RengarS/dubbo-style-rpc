package com.aries.dubbo.like.rpc.common.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class RpcServerContext {
    private static Map<Class<?>, Object> CONTEXT = new HashMap<Class<?>, Object>();

    public static void setCONTEXT(Map<Class<?>, Object> map) {
        CONTEXT.putAll(map);
    }

    public static Object getBean(Class<?> clz) {
        return CONTEXT.get(clz);
    }
}
