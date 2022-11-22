package com.rrzui.wiki.websocket;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @program: 粤嵌项目
 * @description: ws异步调用
 * @author: Ethercat
 * @create: 2022-11-22 14:21
 **/

@Service
public class WsASyncService {

    @Autowired
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message,String logId){
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }

}
