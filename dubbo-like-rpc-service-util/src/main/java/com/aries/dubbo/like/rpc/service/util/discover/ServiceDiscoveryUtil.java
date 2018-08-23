package com.aries.dubbo.like.rpc.service.util.discover;

import com.aries.dubbo.like.rpc.common.util.AddressChannelMap;
import io.netty.channel.Channel;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description: 服务发现工具类
 */
public class ServiceDiscoveryUtil {

    public static Channel getServiceChannel(Class clz) {
        String host = getHost(clz);
        Channel channel = AddressChannelMap.get(host);
        //如果连接不可用，重试一次
        if (channel == null || !channel.isWritable()) {
            host = getHost(clz);
        }
        return null;
    }

    private static String getHost(Class<?> clz) {
        //此处应做负载均衡。
        return "";
    }
}
