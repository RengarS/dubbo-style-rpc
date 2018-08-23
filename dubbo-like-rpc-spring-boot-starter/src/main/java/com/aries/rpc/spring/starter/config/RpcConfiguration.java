package com.aries.rpc.spring.starter.config;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Author: aries
 * Date: 2018/8/23
 * Description:
 */
@Data
public class RpcConfiguration {
    private String basePath;
    private String serverPort;
    private String zookeeperAddress;
}
