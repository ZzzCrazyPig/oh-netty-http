package com.crazypig.oh.http.core.websocket;

import com.crazypig.oh.http.core.JsonHttpServerHandler;
import com.crazypig.oh.http.core.mvc.ApiRequest;
import com.crazypig.oh.http.core.mvc.DefaultApiRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-28
 */
public class DefaultWebSocketProcessor implements WebSocketProcessor {

    private AttributeKey<WebSocketSession> sessionKey = AttributeKey.valueOf("_WS_SESSION");

    private WebSocketHandshakeInterceptor handshakeInterceptor;

    private WebSocketHandler handler;

    public DefaultWebSocketProcessor(WebSocketHandler handler, WebSocketHandshakeInterceptor handshakeInterceptor) {
        this.handler = handler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void process(String path, FullHttpRequest request, ChannelHandlerContext ctx) {

        ChannelPipeline cp = ctx.channel().pipeline();

        cp.remove(HttpObjectAggregator.class);
        cp.remove(JsonHttpServerHandler.class);

        // websocket协议处理器
        cp.addLast(new WebSocketServerProtocolHandler(path));
        cp.addLast(new WebSocketServerHandler(this));

        beforeHandshake(request, ctx);
        ctx.pipeline().fireChannelRead(request.retain());
    }

    @Override
    public void beforeHandshake(FullHttpRequest request, ChannelHandlerContext ctx) {
        if (handshakeInterceptor == null) {
            return;
        }
        ApiRequest apiRequest = new DefaultApiRequest(request);
        Map<String, Object> data = new HashMap<>();
        handshakeInterceptor.beforeHandshake(apiRequest, data);
        if (!data.isEmpty()) {
            data.forEach((k, v) -> {
                ctx.channel().attr(AttributeKey.valueOf(k)).set(v);
            });
        }
    }

    @Override
    public WebSocketSession afterHandshakeCompleted(ChannelHandlerContext ctx) {
        WebSocketSession session = new DefaultWebSocketSession(ctx.channel());
        ctx.channel().attr(sessionKey).set(session);
        handshakeInterceptor.afterHandshakeCompleted(session);
        return session;
    }

    @Override
    public void onSessionEstablished(WebSocketSession session) {
        handler.onSessionEstablished(session);
    }

    @Override
    public void onMessage(WebSocketSession session, WebSocketFrame frame) {


        if (frame instanceof TextWebSocketFrame) {
            handler.onTextMessage(session, ((TextWebSocketFrame) frame).copy());
            return;
        }
        if (frame instanceof BinaryWebSocketFrame) {
            handler.onBinaryMessage(session, ((BinaryWebSocketFrame) frame).copy());
            return;
        }

    }

    @Override
    public void onSessionClosed(WebSocketSession session) {
        handler.onSessionClosed(session);
    }

    @Override
    public void onException(WebSocketSession session, Throwable e) {
        handler.onException(session, e);
    }

    @Override
    public WebSocketSession sessionOf(Channel channel, boolean assertExists) {
        WebSocketSession session = channel.attr(sessionKey).get();
        if (assertExists) {
            Assert.notNull(session, "session can not be null");
        }
        return session;
    }
}
