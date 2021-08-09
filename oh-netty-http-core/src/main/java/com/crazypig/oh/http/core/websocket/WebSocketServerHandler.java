package com.crazypig.oh.http.core.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-27
 */
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private WebSocketProcessor webSocketProcessor;

    public WebSocketServerHandler(WebSocketProcessor webSocketProcessor) {
        this.webSocketProcessor = webSocketProcessor;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketSession session = this.webSocketProcessor.afterHandshakeCompleted(ctx);
            try {
                this.webSocketProcessor.onSessionEstablished(session);
            }
            catch (Exception e) {
                this.webSocketProcessor.onException(session, e);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        WebSocketSession session = webSocketProcessor.sessionOf(ctx.channel(), true);
        try {
            this.webSocketProcessor.onMessage(session, msg);
        }
        catch (Exception e) {
            this.webSocketProcessor.onException(session, e);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        WebSocketSession session = this.webSocketProcessor.sessionOf(ctx.channel(), false);
        if (session != null) {
            try {
                this.webSocketProcessor.onSessionClosed(session);
            }
            catch (Exception e) {
                this.webSocketProcessor.onException(session, e);
            }
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        WebSocketSession session = this.webSocketProcessor.sessionOf(ctx.channel(), false);
        if (session != null) {
            this.webSocketProcessor.onException(session, cause);
        }
        super.exceptionCaught(ctx, cause);
    }
}
