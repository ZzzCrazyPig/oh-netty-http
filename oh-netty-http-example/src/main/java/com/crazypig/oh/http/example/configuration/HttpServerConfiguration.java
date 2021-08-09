package com.crazypig.oh.http.example.configuration;

import com.crazypig.oh.http.core.JsonHttpServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
@Configuration
public class HttpServerConfiguration {

    @Bean
    public JsonHttpServer httpServer() {
        JsonHttpServer jsonHttpServer = new JsonHttpServer();
        return jsonHttpServer;
    }

}
