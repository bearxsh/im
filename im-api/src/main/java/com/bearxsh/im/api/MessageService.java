package com.bearxsh.im.api;

import com.bearxsh.im.entity.Message;

/**
 * @author Bearxsh
 * @date 2021/09/21
 */
public interface MessageService {
    void handleSendMsg(Message message);
}
