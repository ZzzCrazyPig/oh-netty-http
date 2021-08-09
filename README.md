# [WIP] oh-netty-http

学习Netty过程中，基于Netty实现的HTTP API Server 和 WebSocket API Server.

## 子模块说明

- oh-netty-http-core：包含HTTP API Server 和 WebSocket API Server的实现
- oh-netty-http-example：基于`oh-netty-http-core`，使用SpringBoot运行HTTP API Server（包含了WebSocket API Server）

## Demo

运行 `com.crazypig.oh.http.example.main.HttpServerBootstrap`

### HTTP API Demo

使用Postmain工具对暴露的example接口进行测试:

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

`@JsonApi`注解用于指定需要暴露的API接口，`@JsonParam`注解用于指定暴露API接口的入参

## WebSocket API Server

基于Netty WebSocket编解码器的实现之上，实现基于path路由的WebSocket Server，可自定义path和对应的handler来进行WebSocket消息的处理
