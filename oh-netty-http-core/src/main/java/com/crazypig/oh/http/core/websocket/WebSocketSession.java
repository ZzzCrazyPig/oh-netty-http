package com.crazypig.oh.http.core.websocket;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-28
 */
public interface WebSocketSession {

    String id();

    <T> T getAttr(String key);

    <T> void putAttr(String key, T value);

    Object removeAttr(String key);

    void writeTextMessage(TextWebSocketFrame frame);

    void writeBinaryMessage(BinaryWebSocketFrame frame);

    void close(WebSocketCloseStatus closeStatus, String reason);

}
