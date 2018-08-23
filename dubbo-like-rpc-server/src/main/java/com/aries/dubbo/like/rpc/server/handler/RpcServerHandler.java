package com.aries.dubbo.like.rpc.server.handler;

import com.aries.dubbo.like.rpc.common.client.ServerRequest;
import com.aries.dubbo.like.rpc.common.execute.MethodExecutor;
import com.aries.dubbo.like.rpc.common.seriaze.SeriazeHelper;
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
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 收到客户端请求，请求处理后将结果封装成RpcResponse对象返回
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ServerRequest serverRequest = SeriazeHelper.decodeServerRequest(bytes);
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setId(serverRequest.getId());
        try {
            Object result = MethodExecutor.getInstance().execute(serverRequest);
            serverResponse.setCode(200);
            serverResponse.setResponseData(result);
        } catch (Exception e) {
            serverResponse.setCode(500);
            serverResponse.setErrorMessage(e.getMessage());
        } finally {
            ctx.channel().writeAndFlush(serverResponse);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
