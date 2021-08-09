package com.crazypig.oh.http.core.websocket;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-28
 */
public interface WebSocketHandler {

    void onSessionEstablished(WebSocketSession session);

    void onTextMessage(WebSocketSession session, TextWebSocketFrame textMessage);

    void onBinaryMessage(WebSocketSession session, BinaryWebSocketFrame binaryMessage);

    void onSessionClosed(WebSocketSession session);

    void onException(WebSocketSession session, Throwable cause);

}
