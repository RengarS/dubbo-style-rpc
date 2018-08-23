package com.aries.dubbo.like.rpc.common.execute;

import com.aries.dubbo.like.rpc.common.client.ServerRequest;
import com.aries.dubbo.like.rpc.common.context.RpcServerContext;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
public class MethodExecutor {

    public static MethodExecutor getInstance() {
        return InnerMethodExecutor.methodExecutor;
    }

    private static class InnerMethodExecutor {
        private static MethodExecutor methodExecutor = new MethodExecutor();
    }

    /**
     * 执行方法
     *
     * @param serverRequest
     * @return
     * @throws Exception
     */
    public Object execute(ServerRequest serverRequest) throws Exception {
        Object bean = this.getExecutor(serverRequest.getClz());
        Method method;
        if (bean != null) {
            method = bean.getClass().getDeclaredMethod(serverRequest.getMethodName(), serverRequest.getParamTypes());
        } else {
            throw new RuntimeException("no such method:" + serverRequest.getMethodName());
        }
        return method.invoke(bean, serverRequest.getParams());
    }

    /**
     * 从spring容器中根据clz获取bean
     *
     * @param clz
     * @return
     */
    private Object getExecutor(Class<?> clz) {
        return RpcServerContext.getBean(clz);
    }
}
