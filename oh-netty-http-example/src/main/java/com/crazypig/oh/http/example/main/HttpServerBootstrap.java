package com.crazypig.oh.http.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.crazypig.oh.http.example.configuration",
        "com.crazypig.oh.http.example.web"
})
public class HttpServerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(HttpServerBootstrap.class, args);
    }

}
