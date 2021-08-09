package com.crazypig.oh.http.core.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-28
 */
public interface WebSocketProcessor {

    void process(String path, FullHttpRequest request, ChannelHandlerContext ctx);

    void beforeHandshake(FullHttpRequest request, ChannelHandlerContext ctx);

    WebSocketSession afterHandshakeCompleted(ChannelHandlerContext ctx);

    void onSessionEstablished(WebSocketSession session);

    void onMessage(WebSocketSession session, WebSocketFrame frame);

    void onSessionClosed(WebSocketSession session);

    void onException(WebSocketSession session, Throwable e);

    WebSocketSession sessionOf(Channel channel, boolean assertExists);

}
