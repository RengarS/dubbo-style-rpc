package com.aries.dubbo.like.rpc.common.util;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description: 生成请求id
 */
public class IDGenerator {

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
