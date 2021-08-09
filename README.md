# [WIP] oh-netty-http

学习Netty过程中，基于Netty实现的HTTP API Server 和 WebSocket API Server.

## 子模块说明

- oh-netty-http-core：包含HTTP API Server 和 WebSocket API Server的实现
- oh-netty-http-example：基于`oh-netty-http-core`，使用SpringBoot运行HTTP API Server（包含了WebSocket API Server）的示例程序

## Demo

运行 `com.crazypig.oh.http.example.main.HttpServerBootstrap`

### HTTP API Demo

使用Postman工具对暴露的example接口进行测试:

```http
POST http://${yourhost}:8080/demo/success
```

```json
{
    "a": "aValue",
    "b": "bValue"
}
```

得到返回:

```json
{
    "data": {
        "a": "aValue",
        "b": "bValue"
    },
    "success": true
}
```

### WebSocket API Demo

chrome浏览器安装Smart WebSocket Client进行测试

```ws
ws://${yourhost}:8080/ws
```

## HTTP API Server

基于MVC的思路实现的简单 HTTP API Server，支持JSON方式入参以及返回JSON格式的响应。

### API注解
- `@JsonApi`注解用于指定需要暴露的API接口
- `@JsonParam`注解用于指定暴露API接口的入参

### 请求处理逻辑

结合Netty提供的HTTP编解码处理器之上，我们可以得到一个完整的HttpRequest => `FullHttpRequest`，之后我们利用自定义的ChannelInboundHandler进行处理

1. `JsonHttpServerHandler`得到`FullHttpRequest`并解析得到`ApiRequest`
2. `RequestDispatcher`针对1中得到的`ApiRequest`进行dispatch
3. 一个`ApiRequest`对应一个URI path，根据path指定的`RequestRouter`来进行处理
4. `RequestRouter`由进一步将`ApiRequest`的URI Path来找到对应的`RequestChain`，一个`RequestChain`中包含拦截器和具体的执行目标（反射机制）
5. 通过`RequestChain`的execute方法得到请求结果后，经过`ApiViewMapper`映射得到对应的view
6. 最终在`JsonHttpServerHandler`执行`RequestDispatcher`的dispatch返回以后，得到view，最后则进行write数据返回

### 拦截器

见 `com.crazypig.oh.http.core.mvc.RequestInterceptor` 定义，主要可在请求之前，之后以及异常时进行处理

### 全局异常处理

见 `com.crazypig.oh.http.core.mvc.RequestExceptionHandler` 定义，可对请求的异常进行全局处理

## WebSocket API Server

基于Netty WebSocket编解码器的实现之上，实现基于path路由的WebSocket API Server，可自定义path和对应的handler来进行WebSocket消息的处理

自定义的path和对应的Handler通过`WebSocketRegistry`抽象定义中的`register`进行注册

### 请求处理逻辑

请求处理与HTTP API Server的主逻辑很像，主要是根据path定位到具体的`WebSocketProcessor`，一个processor封装了前面注册的handler，进而在接收到websocket消息以后，由processor委托给我们定义的handler进行处理。processor还依赖Netty对WebSocket协议的支持，封装了WebSocket的Upgrade以及消息的处理

1. `JsonHttpServerHandler`处理HTTP请求，会先判断URI path是否是WebSocket的path，是则选择对应的Processor进行process
2. `WebSocketProcessor`的process主要进行HTTP -> WebSocket 协议处理的转化(包含Upgrade)，并在pipeline里面设置为WebSocket的Handler
3. websocket链路经过升级以后，由`WebSocketServerHandler`来处理消息的接收，并由我们定义的handler来进行处理（响应）
