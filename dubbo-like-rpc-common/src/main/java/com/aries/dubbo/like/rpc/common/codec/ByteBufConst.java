package com.aries.dubbo.like.rpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class ByteBufConst {
    private static ByteBuf delimiter = Unpooled.copiedBuffer("_$$".getBytes());

    public static ByteBuf Delimiter() {
        return delimiter;
    }
}
