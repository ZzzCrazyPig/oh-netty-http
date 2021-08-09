package com.crazypig.oh.http.core.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToLongFunction;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-27
 */
@Component
@Slf4j
public class DefaultWebSocketRegistry implements WebSocketRegistry, InitializingBean {

    public static DefaultWebSocketRegistry INSTANCE;

    private Map<String, WebSocketProcessor> pathProcessorMap = new TreeMap<>();

    private Map<String, WebSocketProcessor> patternPathProcessorMap = new TreeMap<>(Comparator.comparingLong(
            (ToLongFunction<String>) value -> value.length()).reversed());

    private PathMatcher pathMatcher = new AntPathMatcher();


    public static DefaultWebSocketRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }

    @Override
    public void register(String path,
                         WebSocketHandler handler,
                         WebSocketHandshakeInterceptor interceptor) {

        if (pathMatcher.isPattern(path)) {
            register(path, handler, interceptor, patternPathProcessorMap);
            return;
        }

        register(path, handler, interceptor, pathProcessorMap);

    }

    private void register(String path, WebSocketHandler handler, WebSocketHandshakeInterceptor interceptor,
                          Map<String, WebSocketProcessor> processorMap) {
        if (processorMap.get(path) != null) {
            throw new IllegalStateException("Duplicated websocket mapping path : " + path);
        }
        processorMap.put(path, new DefaultWebSocketProcessor(handler, interceptor));
        log.info("Register websocket processor of path : {}", path);
    }

    @Override
    public boolean contain(String path) {
        boolean flag = pathProcessorMap.containsKey(path);
        if (flag) {
            return true;
        }
        for (String patternPath : patternPathProcessorMap.keySet()) {
            if (pathMatcher.match(patternPath, path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public WebSocketProcessor processorOf(String path) {
        WebSocketProcessor processor = pathProcessorMap.get(path);
        if (processor != null) {
            return processor;
        }
        for (String patternPath : patternPathProcessorMap.keySet()) {
            if (pathMatcher.match(patternPath, path)) {
                return patternPathProcessorMap.get(patternPath);
            }
        }
        return null;
    }


}
