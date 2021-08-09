package com.crazypig.oh.http.core.websocket;

import com.crazypig.oh.http.core.mvc.ApiRequest;

import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-28
 */
public interface WebSocketHandshakeInterceptor {

    void beforeHandshake(ApiRequest request, Map<String, Object> data);

    void afterHandshakeCompleted(WebSocketSession session);

}
