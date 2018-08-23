package com.aries.dubbo.like.rpc.common.codec;

import com.aries.dubbo.like.rpc.common.client.ServerRequest;
import com.aries.dubbo.like.rpc.common.server.ServerResponse;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description: 序列化工具
 */
public class SeriazeHelper {
    private static final RuntimeSchema<ServerRequest> SERVER_REQUEST_SCHEMA = RuntimeSchema.createFrom(ServerRequest.class);
    private static final RuntimeSchema<ServerResponse> SERVER_RESPONSE_SCHEMA = RuntimeSchema.createFrom(ServerResponse.class);

    public static byte[] encodeServerRequest(ServerRequest serverRequest) {
        return ProtobufIOUtil
                .toByteArray(
                        serverRequest, SERVER_REQUEST_SCHEMA, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
                );
    }

    public static ServerRequest decodeServerRequest(byte[] bytes) {
        ServerRequest serverRequest = SERVER_REQUEST_SCHEMA.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, serverRequest, SERVER_REQUEST_SCHEMA);
        return serverRequest;
    }

    public static ServerResponse decodeServerResponse(byte[] bytes) {
        ServerResponse serverResponse = SERVER_RESPONSE_SCHEMA.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, serverResponse, SERVER_RESPONSE_SCHEMA);
        return serverResponse;
    }

    public static byte[] encodeServerResponse(ServerResponse serverResponse) {
        return ProtobufIOUtil.toByteArray(
                serverResponse, SERVER_RESPONSE_SCHEMA, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
        );
    }


}
