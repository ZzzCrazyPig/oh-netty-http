package com.crazypig.oh.http.core.mvc;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-27
 */
public class DefaultApiResponse implements ApiResponse {

    private FullHttpRequest httpRequest;

    private ChannelHandlerContext ctx;

    private byte[] contentBytes;

    private Charset charset = CharsetUtil.UTF_8;

    private AsciiString contentType = APPLICATION_JSON;

    private Map<String, String> customHeaders = new LinkedHashMap<>();

    public DefaultApiResponse(FullHttpRequest httpRequest, ChannelHandlerContext ctx) {
        this.httpRequest = httpRequest;
        this.ctx = ctx;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = AsciiString.of(contentType);
    }

    @Override
    public void addHeaders(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        customHeaders.putAll(headers);
    }

    @Override
    public void addHeader(String name, String value) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        customHeaders.put(name, value);
    }

    @Override
    public void setContent(byte[] bytes, Charset charset) {
        this.contentBytes = bytes;
        this.charset = charset;
    }

    @Override
    public void writeAndFlush() {
        FullHttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), OK,
                Unpooled.copiedBuffer(contentBytes));

        response.headers()
                .set(CONTENT_TYPE, contentType)
                .setInt(CONTENT_LENGTH, response.content().readableBytes());

        customHeaders.keySet().stream().forEach(customHeader -> {

            if (CONTENT_TYPE.contentEqualsIgnoreCase(customHeader)) {
                return;
            }
            if (CONTENT_LENGTH.contentEqualsIgnoreCase(customHeader)) {
                return;
            }

            response.headers().set(customHeader, customHeaders.get(customHeader));

        });

        boolean keepAlive = HttpUtil.isKeepAlive(this.httpRequest);

        if (keepAlive) {
            if (!this.httpRequest.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
        } else {
            // Tell the client we're going to close the connection.
            response.headers().set(CONNECTION, CLOSE);
        }

        ChannelFuture f = ctx.writeAndFlush(response);

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
