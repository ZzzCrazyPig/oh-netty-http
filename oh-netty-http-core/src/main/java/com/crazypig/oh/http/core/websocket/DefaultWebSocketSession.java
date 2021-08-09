package com.crazypig.oh.http.core.websocket;


import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-29
 */
public class DefaultWebSocketSession implements WebSocketSession {

    private Channel channel;

    public DefaultWebSocketSession(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String id() {
        return channel.id().asShortText();
    }

    @Override
    public <T> T getAttr(String key) {
        Attribute<T> attr = channel.attr(AttributeKey.valueOf(key));
        return attr.get();
    }

    @Override
    public <T> void putAttr(String key, T value) {
        channel.attr(AttributeKey.valueOf(key)).getAndSet(value);
    }

    @Override
    public Object removeAttr(String key) {
        return channel.attr(AttributeKey.valueOf(key)).getAndSet(null);
    }

    @Override
    public void writeTextMessage(TextWebSocketFrame frame) {
        this.channel.writeAndFlush(frame);
    }

    @Override
    public void writeBinaryMessage(BinaryWebSocketFrame frame) {
        this.channel.writeAndFlush(frame);
    }

    @Override
    public void close(WebSocketCloseStatus closeStatus, String reason) {
        channel.writeAndFlush(new CloseWebSocketFrame(closeStatus, reason));
    }
}
