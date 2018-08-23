package com.aries.dubbo.like.rpc.common.util;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class AddressChannelMap {
    private static Map<String, Channel> ADDRESS_CHANNEL_MAP = new HashMap<>();

    public static void add(String addr, Channel channel) {
        ADDRESS_CHANNEL_MAP.put(addr, channel);
    }

    public static Channel get(String addr) {
        return ADDRESS_CHANNEL_MAP.get(addr);
    }
}
