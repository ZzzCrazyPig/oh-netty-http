package com.crazypig.oh.http.core.mvc;

import com.crazypig.oh.http.core.annotation.JsonApi;
import com.crazypig.oh.http.core.exception.NoRouteException;
import com.crazypig.oh.http.core.exception.RequestChainExecuteException;
import com.crazypig.oh.http.util.UrlPathUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToLongFunction;

/**
 * HTTP 请求路由分发处理主类
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
@Component
@Slf4j
public class DefaultRequestDispatcher implements RequestDispatcher, ApplicationContextAware, InitializingBean {

    private static DefaultRequestDispatcher INSTANCE;

    private Map<String, RequestHandle> requestHandleMap = new ConcurrentHashMap<>();

    @Setter
    private List<RequestRouter> requestRouters = new ArrayList<>();

    @Setter
    private ApiViewMapper apiViewMapper = new DefaultApiViewMapper();

    @Setter
    private RequestExceptionHandler exceptionHandler = new DefaultRequestExceptionHandler();

    private Map<String, RequestRouter> pathRouters = new TreeMap<>();

    private Map<String, RequestRouter> patternPathRouters = new TreeMap<>(Comparator.comparingLong(
            (ToLongFunction<String>) value -> value.length()).reversed());

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Getter
    private RequestExceptionHandler internalExceptionHandler = new DefaultRequestExceptionHandler();

    public static DefaultRequestDispatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        // 处理api注解
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(JsonApi.class);
        beanMap.values().stream().forEach(bean -> visitJsonApiBean(bean));

        // 如果什么都没有内置一个/**的路由负责处理所有的请求
        if (CollectionUtils.isEmpty(requestRouters)) {
            DefaultRequestRouter defaultRequestRouter = new DefaultRequestRouter();
            defaultRequestRouter.setPath("/**");
            requestRouters.add(defaultRequestRouter);
        }

        // 处理RequestRouter
        requestRouters.stream().forEach(router -> {
            String path = router.path();
            if (log.isInfoEnabled()) {
                log.info("{} register router {} of path : {}", this.getClass().getSimpleName(),
                        router.getClass().getSimpleName(), path);
            }
            registerRouter(path,  router);
        });

    }

    @Override
    public ApiView dispatch(ApiRequest req) {
        ApiView view = null;
        try {
            String path = req.path();
            RequestRouter reqRouter = pathRouters.get(path);
            reqRouter = (reqRouter == null ? matchPatternRouter(path) : reqRouter);
            // 找到对应的请求路由处理器
            if (reqRouter == null) {
                throw new NoRouteException("No route for path : " + path);
            }
            // 执行
            ApiModel model = executeWithChain(reqRouter, req);
            // model -> view
            view = apiViewMapper.map(model);
        }
        catch (RequestChainExecuteException e) {
            // 自定义的异常处理
            view = exceptionHandler.handleException(req, e.getRequestHandle(), e.getCause());
        }
        catch (Throwable e) {
            // 系统内的异常处理
            view = internalExceptionHandler.handleException(req, null, e);
        }
        return view;
    }



    @Override
    public void registerRouter(String path, RequestRouter reqRouter) {
        path = StringUtils.trim(path);
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Request Router path can not be empty");
        }
        if (pathMatcher.isPattern(path)) {
            putInto(path, reqRouter, patternPathRouters);
            return;
        }
        putInto(path, reqRouter, pathRouters);
    }

    protected void putInto(String path, RequestRouter reqRouter, Map<String, RequestRouter> routerMap) {
        if (routerMap.get(path) != null) {
            throw new IllegalArgumentException("Already exists Request Router path : " + path);
        }
        routerMap.put(path, reqRouter);
    }

    @Override
    public void registerApiViewMapper(ApiViewMapper apiViewMapper) {
        this.apiViewMapper = apiViewMapper;
    }


    protected ApiModel executeWithChain(RequestRouter router, ApiRequest req) throws RequestChainExecuteException {
        RequestChain reqChain = router.route(req, Collections.unmodifiableMap(requestHandleMap));
        ApiModel resp = reqChain.execute();
        return resp;
    }

    protected RequestRouter matchPatternRouter(String path) {
        Optional<String> pathOptional = patternPathRouters.keySet().stream().filter(pathPattern -> {
            boolean match = pathMatcher.match(pathPattern, path);
            return match;
        }).findFirst();

        if (pathOptional.isPresent()) {
            return patternPathRouters.get(pathOptional.get());
        }
        return null;
    }

    /**
     * 遍历并注册request handle
     * @param bean
     */
    private void visitJsonApiBean(Object bean) {

        JsonApi rootJsonApi = AnnotationUtils.getAnnotation(bean.getClass(), JsonApi.class);
        String rootPath = rootJsonApi == null ? null : rootJsonApi.value();

        ReflectionUtils.doWithMethods(bean.getClass(), method -> {

            JsonApi jsonApi = AnnotationUtils.findAnnotation(method, JsonApi.class);
            if (jsonApi == null) {
                return;
            }

            String path = jsonApi.value();
            String realPath = UrlPathUtils.concat(rootPath, path);
            DefaultRequestHandle target = new DefaultRequestHandle(bean, method);
            registerRequestHandle(realPath, target);


        }, method -> Modifier.isPublic(method.getModifiers()));

    }

    private void registerRequestHandle(String urlPath, RequestHandle target) {
        RequestHandle requestHandle = requestHandleMap.get(urlPath);
        if (requestHandle != null) {
            throw new IllegalStateException("Duplicated api : " + urlPath);
        }
        if (log.isInfoEnabled()) {
            log.info("{} register request handle of path : {}", this.getClass().getSimpleName(),
                    urlPath);
        }
        requestHandleMap.put(urlPath, target);
    }

}
