package com.crazypig.oh.http.example.configuration;

import com.crazypig.oh.http.core.mvc.DefaultRequestDispatcher;
import com.crazypig.oh.http.core.mvc.DefaultRequestRouter;
import com.crazypig.oh.http.core.mvc.RequestInterceptor;
import com.crazypig.oh.http.core.mvc.RequestRouter;
import com.crazypig.oh.http.core.websocket.DefaultWebSocketRegistry;
import com.crazypig.oh.http.example.web.interceptor.LogRequestInterceptor;
import com.crazypig.oh.http.example.websocket.EchoWebSocketHandler;
import com.crazypig.oh.http.example.websocket.MyWebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
@Configuration
public class HttpServerDispatcherConfiguration {

    @Bean
    public DefaultWebSocketRegistry webSocketRegistry() {
        DefaultWebSocketRegistry webSocketRegistry = new DefaultWebSocketRegistry();
        webSocketRegistry.register("/ws/**", new EchoWebSocketHandler(),
                new MyWebSocketHandshakeInterceptor());
        return webSocketRegistry;
    }

    @Bean
    public DefaultRequestDispatcher requestDispatcher() {
        DefaultRequestDispatcher dispatcher = new DefaultRequestDispatcher();
        dispatcher.setRequestRouters(Arrays.asList(
                requestRouter(),
                requestRouterOfDemo()
        ));
        return dispatcher;
    }

    @Bean
    public RequestRouter requestRouter() {
        DefaultRequestRouter requestRouter = new DefaultRequestRouter();
        requestRouter.setPath("/**");
        return requestRouter;
    }

    public RequestRouter requestRouterOfDemo() {
        DefaultRequestRouter requestRouter = new DefaultRequestRouter();
        requestRouter.setPath("/demo/**");
        requestRouter.setInterceptors(
                Arrays.asList(
                        logInterceptor()
                )
        );
        return requestRouter;
    }

    @Bean
    public RequestInterceptor logInterceptor() {
        return new LogRequestInterceptor();
    }

}
