package com.aries.dubbo.like.rpc.client.handler;

import com.aries.dubbo.like.rpc.client.MatchUtil;
import com.aries.dubbo.like.rpc.common.ResponseWrapper;
import com.aries.dubbo.like.rpc.common.codec.SeriazeHelper;
import com.aries.dubbo.like.rpc.common.server.ServerResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取ByteBuf数据
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        //反序列化
        ServerResponse serverResponse = SeriazeHelper.decodeServerResponse(bytes);
        //填充结果
        ResponseWrapper responseWrapper = MatchUtil.get(serverResponse.getId());
        responseWrapper.setServerResponse(serverResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
