package com.aries.dubbo.like.rpc.client.bootstrap;

import com.aries.dubbo.like.rpc.client.handler.RpcClientHandler;
import com.aries.dubbo.like.rpc.client.util.AddressChannelMap;
import com.aries.dubbo.like.rpc.common.codec.ByteBufConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
@Slf4j
public class ClientBootStrap {

    public static void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(2);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(10000, ByteBufConst.Delimiter()));

                            socketChannel.pipeline().addLast(new RpcClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            AddressChannelMap.add(host + port, future.channel());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("error to connect remote server:" + host + ":" + port, e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
