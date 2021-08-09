package com.crazypig.oh.http.core;

import com.crazypig.oh.http.core.mvc.*;
import com.crazypig.oh.http.core.websocket.DefaultWebSocketRegistry;
import com.crazypig.oh.http.core.websocket.WebSocketProcessor;
import com.crazypig.oh.http.core.websocket.WebSocketRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
public class JsonHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Executor requestExecutor;

    public JsonHttpServerHandler(Executor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpMethod httpMethod = request.method();
        HttpHeaders headers = request.headers();
        String requestUri = request.uri();
        log.info("{} : {}, headers: [{}]", httpMethod, requestUri, headers.toString());

        ApiRequest req = new DefaultApiRequest(request);
        String path = req.path();

        // ws handshake
        WebSocketRegistry webSocketRegistry = DefaultWebSocketRegistry.getInstance();
        if (webSocketRegistry.contain(path)) {
            WebSocketProcessor webSocketProcessor = webSocketRegistry.processorOf(path);
            webSocketProcessor.process(path, request, ctx);
            return;
        }

        // 派发到request线程池处理
        CompletableFuture<ApiView> f = CompletableFuture.supplyAsync(() -> {

            // 根据uri找到对应的handle mapping进行处理
            RequestDispatcher dispatcher = DefaultRequestDispatcher.getInstance();
            ApiView apiView = dispatcher.dispatch(req);
            return apiView;

        }, requestExecutor).exceptionally(ex -> {

            ApiView apiView = DefaultRequestDispatcher.getInstance().getInternalExceptionHandler()
                    .handleException(req, null, ex);
            return apiView;

        });

        // 回调处理
        f.thenAccept(apiView -> {

            ApiResponse apiResponse = new DefaultApiResponse(request, ctx);
            apiView.writeTo(apiResponse);

        });

    }

    private boolean matchWebsocketPath(String path) {
        return path.startsWith("/ws");
    }

    private static String getWebSocketLocation(FullHttpRequest req, String path) {
        String location =  req.headers().get(HttpHeaderNames.HOST) + path;
        return "ws://" + location;
    }

//    protected void response(FullHttpRequest request, JSONObject resp, ChannelHandlerContext ctx) {
//
//        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK,
//                Unpooled.copiedBuffer(resp.toJSONString().getBytes(CharsetUtil.UTF_8)));
//
//        response.headers()
//                .set(CONTENT_TYPE, APPLICATION_JSON)
//                .setInt(CONTENT_LENGTH, response.content().readableBytes());
//
//        boolean keepAlive = HttpUtil.isKeepAlive(request);
//
//        if (keepAlive) {
//            if (!request.protocolVersion().isKeepAliveDefault()) {
//                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//            }
//        } else {
//            // Tell the client we're going to close the connection.
//            response.headers().set(CONNECTION, CLOSE);
//        }
//
//        ChannelFuture f = ctx.writeAndFlush(response);
//
//        if (!keepAlive) {
//            f.addListener(ChannelFutureListener.CLOSE);
//        }
//
//    }

}
