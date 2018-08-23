package com.aries.dubbo.like.rpc.client.proxy;

import com.aries.dubbo.like.rpc.client.MatchUtil;
import com.aries.dubbo.like.rpc.common.ResponseWrapper;
import com.aries.dubbo.like.rpc.common.client.ServerRequest;
import com.aries.dubbo.like.rpc.common.codec.ByteBufConst;
import com.aries.dubbo.like.rpc.common.seriaze.SeriazeHelper;
import com.aries.dubbo.like.rpc.common.server.ServerResponse;
import com.aries.dubbo.like.rpc.common.util.IDGenerator;
import com.aries.dubbo.like.rpc.service.util.discover.ServiceDiscoveryUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description: 为service 接口生成代理生成类
 */
@SuppressWarnings("unchecked")
public class ServiceProxy implements MethodInterceptor {

    public static <T> T newInstance(Class<T> clz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{clz});
        enhancer.setCallback(new ServiceProxy());
        return (T) enhancer.create();
    }

    /**
     * service 接口调用过程
     *
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //构造server request请求
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setId(IDGenerator.getId());
        serverRequest.setClz(o.getClass().getInterfaces()[0]);
        serverRequest.setMethodName(method.getName());
        serverRequest.setParamTypes(method.getParameterTypes());
        serverRequest.setParams(objects);
        //将server request序列化成byte[],并将byte[]和分隔符写入ByteBuf
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(SeriazeHelper.encodeServerRequest(serverRequest));
        byteBuf.writeBytes(ByteBufConst.Delimiter());
        //获取channel
        Channel channel = ServiceDiscoveryUtil.getServiceChannel(o.getClass().getInterfaces()[0]);
        if (channel == null) {
            throw new RuntimeException("interface:" + o.getClass().getInterfaces()[0].getName() +
                    " has no service provide!");
        }
        if (!channel.isWritable()) {
            throw new RuntimeException("interface:" + o.getClass().getInterfaces()[0].getName() +
                    " remote provider may be dead!");
        }
        channel.writeAndFlush(byteBuf);
        //获取远程调用结果。
        ResponseWrapper responseWrapper = MatchUtil.get(serverRequest.getId());
        ServerResponse serverResponse = responseWrapper.getServerResponse();
        if (serverResponse.getCode() != 200) {
            throw new RuntimeException("method:" + method.getName() +
                    "  invoke occurs an error:" + serverResponse.getErrorMessage());
        }
        return serverResponse;
    }
}
