package com.crazypig.oh.http.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class JsonHttpServerChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
        ch.pipeline().addLast(new HttpServerExpectContinueHandler());
        ch.pipeline().addLast(new JsonHttpServerHandler(JsonHttpServer.INSTANCE.getRequestExecutor()));
    }
}
