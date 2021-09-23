package com.bearxsh.im.service.provider;

import com.bearxsh.im.api.MessageService;
import com.bearxsh.im.api.SendMessageService;
import com.bearxsh.im.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.cluster.router.address.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Bearxsh
 * @date 2021/09/21
 */
@DubboService
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RedisTemplate redisTemplate;

    @DubboReference(parameters = {"router","address"})
    private SendMessageService sendMessageService;

    @Override
    public void handleSendMsg(Message message) {
        // TODO 消息入库
        String to = message.getTo();
        String data = message.getData();

        String gatewayAddr = (String) redisTemplate.opsForHash().get("gateway", to);
        if (gatewayAddr == null) {
            log.error("can not find destination: {}", to);
            return;
        }
        log.info("destination: {}", gatewayAddr);
        String[] addr = gatewayAddr.split(":");
        Address address = new Address(addr[0], Integer.parseInt(addr[1]));
        RpcContext.getContext().setObjectAttachment("address", address);
        sendMessageService.senMessage(to, data);

    }
}
