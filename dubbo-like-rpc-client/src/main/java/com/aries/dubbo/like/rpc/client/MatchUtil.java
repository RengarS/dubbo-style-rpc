package com.aries.dubbo.like.rpc.client;

import com.aries.dubbo.like.rpc.common.ResponseWrapper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class MatchUtil {

    private static final ConcurrentHashMap<String, ResponseWrapper> MAP = new ConcurrentHashMap<>(1024, 2.0F);

    public static ConcurrentHashMap<String, ResponseWrapper> getMap() {
        return MAP;
    }

    public static void add(String id) {
        MAP.put(id, new ResponseWrapper());
    }

    public static ResponseWrapper get(String id) {
        return MAP.get(id);
    }
}
