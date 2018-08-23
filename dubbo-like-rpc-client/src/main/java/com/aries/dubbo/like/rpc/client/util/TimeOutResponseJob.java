package com.aries.dubbo.like.rpc.client.util;

import com.aries.dubbo.like.rpc.client.MatchUtil;
import com.aries.dubbo.like.rpc.common.ResponseWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description: 定时清除map中的过期元素，防止oom或者内存泄漏
 */
public class TimeOutResponseJob {
    static {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(() -> {
            ConcurrentHashMap<String, ResponseWrapper> map = MatchUtil.getMap();
            map.entrySet().removeIf(stringResponseWrapperEntry -> stringResponseWrapperEntry.getValue().isDone());
        }, 20, 10, TimeUnit.SECONDS);
    }
}
