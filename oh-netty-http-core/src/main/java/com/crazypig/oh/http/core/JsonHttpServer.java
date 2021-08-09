package com.crazypig.oh.http.core;

import com.crazypig.oh.http.util.HostUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Slf4j
@Component
public class JsonHttpServer implements InitializingBean {

    static JsonHttpServer INSTANCE;

    @Value("${server.port:8080}")
    private int port;

    @Value("${server.executor.namePrefix:http-exec-}")
    private String executorNamePrefix;

    @Value("${server.executor.threadSize:200}")
    private int executorThreadSize;

    @Value("${server.executor.maxThreadSize:250}")
    private int executorMaxThreadSize;

    @Value("${server.executor.backlogSize:100}")
    private int executorBacklogSize;

    @Getter
    private Executor requestExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
        initRequestExecutor();
        this.start();
    }

    void initRequestExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix(this.executorNamePrefix);
        taskExecutor.setCorePoolSize(this.executorThreadSize);
        taskExecutor.setMaxPoolSize(this.executorMaxThreadSize);
        taskExecutor.setQueueCapacity(this.executorBacklogSize);
        taskExecutor.initialize();
        this.requestExecutor = taskExecutor;
    }

    void start() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new JsonHttpServerChannelInitializer())
                .bind(HostUtils.serverHost(), port)
                .sync();

        log.info("http server start at {}:{}", HostUtils.serverHost(), port);
    }

}
