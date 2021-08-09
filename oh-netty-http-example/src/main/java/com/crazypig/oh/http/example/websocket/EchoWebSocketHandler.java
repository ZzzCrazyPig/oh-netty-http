package com.crazypig.oh.http.example.websocket;

import com.crazypig.oh.http.core.websocket.WebSocketHandler;
import com.crazypig.oh.http.core.websocket.WebSocketSession;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-29
 */
@Slf4j
public class EchoWebSocketHandler implements WebSocketHandler {

    @Override
    public void onSessionEstablished(WebSocketSession session) {
        log.info("onSessionEstablished, id : {}", session.id());
    }

    @Override
    public void onTextMessage(WebSocketSession session, TextWebSocketFrame textMessage) {
        log.info("onTextMessage, id : {}, content : {}", session.id(), textMessage.text());
        session.writeTextMessage(textMessage);
    }

    @Override
    public void onBinaryMessage(WebSocketSession session, BinaryWebSocketFrame binaryMessage) {
        log.info("onBinaryMessage, id : {}, content : \n{}", session.id(), ByteBufUtil.hexDump(binaryMessage.content()));
        session.writeBinaryMessage(binaryMessage);
    }

    @Override
    public void onSessionClosed(WebSocketSession session) {
        log.info("onSessionClosed, id : {}", session.id());
    }

    @Override
    public void onException(WebSocketSession session, Throwable cause) {
        log.error("onException, id : " + session.id(), cause);
    }
}
