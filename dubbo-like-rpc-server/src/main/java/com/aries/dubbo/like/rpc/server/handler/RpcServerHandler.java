package com.aries.dubbo.like.rpc.server.handler;

import com.aries.dubbo.like.rpc.common.client.ServerRequest;
import com.aries.dubbo.like.rpc.common.enums.ResponseCodeEnum;
import com.aries.dubbo.like.rpc.common.execute.MethodExecutor;
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
        //读取ByteBuf并将数组反序列化成server request对象
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ServerRequest serverRequest = SeriazeHelper.decodeServerRequest(bytes);
        //构建server response对象
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setId(serverRequest.getId());
        try {
            //执行
            Object result = MethodExecutor.getInstance().execute(serverRequest);
            serverResponse.setCode(ResponseCodeEnum.OK.getCode());
            serverResponse.setResponseData(result);
        } catch (Exception e) {
            serverResponse.setCode(ResponseCodeEnum.ERROR.getCode());
            serverResponse.setErrorMessage(e.getMessage());
        } finally {
            //返回
            ctx.channel().write(serverResponse);
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
