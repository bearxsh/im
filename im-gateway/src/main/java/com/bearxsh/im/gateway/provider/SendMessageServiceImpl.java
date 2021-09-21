package com.bearxsh.im.gateway.provider;

import com.bearxsh.im.api.SendMessageService;
import com.bearxsh.im.gateway.websocket.handler.WebSocketServerHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Bearxsh
 * @date 2021/09/21
 */
@DubboService
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    @Override
    public void senMessage(String to, String data) {
        Channel channel = WebSocketServerHandler.clientMap.get(to);
        if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(data));
        } else {
            log.error("destination user can not find!");
        }
    }
}
