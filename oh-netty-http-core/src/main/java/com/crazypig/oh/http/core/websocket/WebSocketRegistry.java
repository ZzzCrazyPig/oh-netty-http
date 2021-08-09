package com.crazypig.oh.http.core.websocket;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-27
 */
public interface WebSocketRegistry {

    boolean contain(String path);

    void register(String path,
                  WebSocketHandler handler,
                  WebSocketHandshakeInterceptor interceptor);

    WebSocketProcessor processorOf(String path);

}
