package com.aries.dubbo.like.rpc.client.util;

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
}
