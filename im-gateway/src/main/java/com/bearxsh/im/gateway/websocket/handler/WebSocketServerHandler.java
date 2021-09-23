package com.bearxsh.im.gateway.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bearxsh.im.api.MessageService;
import com.bearxsh.im.entity.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * note：TextWebSocketFrame only can process text message not binary message.
 * @author Bearxsh
 * @date 2021/09/20
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final Map<String, Channel> clientMap = new ConcurrentHashMap<>();
    private static final String GATEWAY_KEY = "gateway";

    @Value("${dubbo.protocol.port}")
    private int port;
    @Autowired
    private RedisTemplate redisTemplate;
    @DubboReference
    private MessageService messageService;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String message = frame.text();
        log.info(LocalDateTime.now() + " receive a message: " + message);
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        switch (type) {
            case "ONLINE":
                String name = jsonObject.getString("name");
                handleOnlineMsg(name, ctx.channel());
                break;
            case "SEND":
                Message msg = new Message();
                msg.setFrom(jsonObject.getString("from"));
                msg.setTo(jsonObject.getString("to"));
                msg.setData(jsonObject.getString("data"));
                msg.setCreateTime(LocalDateTime.now());
                handleSendMsg(msg);
                break;
            default:
                log.error("error message type!");
                break;
        }
        ctx.writeAndFlush(new TextWebSocketFrame("ok."));
    }

    private void handleOnlineMsg(String name, Channel channel) throws UnknownHostException {
        clientMap.put(name, channel);
        log.info((String) redisTemplate.opsForHash().get(GATEWAY_KEY, name));
        String host = InetAddress.getLocalHost().getHostAddress();
        String address = host + ":" + port;
        redisTemplate.opsForHash().put(GATEWAY_KEY, name, address);
    }

    private void handleSendMsg(Message message) {
        messageService.handleSendMsg(message);
    }

}
