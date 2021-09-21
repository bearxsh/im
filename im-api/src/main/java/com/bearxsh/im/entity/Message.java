package com.bearxsh.im.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bearxsh
 * @date 2021/09/21
 */
@Data
public class Message implements Serializable {
    private String from;
    private String to;
    private String data;
    private LocalDateTime createTime;
}
