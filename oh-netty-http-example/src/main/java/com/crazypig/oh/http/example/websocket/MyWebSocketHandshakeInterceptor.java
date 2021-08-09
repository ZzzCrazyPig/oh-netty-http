package com.crazypig.oh.http.example.websocket;

import com.crazypig.oh.http.core.mvc.ApiRequest;
import com.crazypig.oh.http.core.websocket.WebSocketHandshakeInterceptor;
import com.crazypig.oh.http.core.websocket.WebSocketSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-29
 */
@Slf4j
public class MyWebSocketHandshakeInterceptor implements WebSocketHandshakeInterceptor {

    @Override
    public void beforeHandshake(ApiRequest request, Map<String, Object> data) {
        log.info("beforeHandshake, uri : {}", request.uri());
        data.put("startTime", System.currentTimeMillis());
    }

    @Override
    public void afterHandshakeCompleted(WebSocketSession session) {
        log.info("afterHandshakeCompleted, id : {}, startAt : {}", session.id(), session.getAttr("startTime"));
    }
}
