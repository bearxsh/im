package com.bearxsh.im.gateway.provider;

import com.bearxsh.im.api.GreetingService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Bearxsh
 * @date 2021/09/20
 */
@DubboService
public class GreetingServiceImpl implements GreetingService {
    @Override
    public String sayHi(String name) {
        return "0920 " + name;
    }
}
