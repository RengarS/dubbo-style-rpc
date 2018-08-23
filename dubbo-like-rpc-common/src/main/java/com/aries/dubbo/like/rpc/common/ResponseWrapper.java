package com.aries.dubbo.like.rpc.common;

import com.aries.dubbo.like.rpc.common.server.ServerResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class ResponseWrapper {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private ServerResponse serverResponse;
    @Getter
    private volatile boolean isDone = false;


    public ServerResponse getServerResponse() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("method:ResponseWrapper.getServerResponse occurs an error", e);
        }
        ServerResponse serverResponse = this.serverResponse;
        this.isDone = true;
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.countDownLatch.countDown();
        this.serverResponse = serverResponse;
    }

    public Boolean isOk() {
        return this.countDownLatch.getCount() == 0;
    }
}
