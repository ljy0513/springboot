package com.study.tobySpringboot.controller;

import com.study.tobySpringboot.service.HelloService;

import java.util.Objects;

public class HelloController {
    public String hello (String name) {
        HelloService helloService = new HelloService();
        return helloService.sayHello(Objects.requireNonNull(name)); //Objects.requireNonNull 사용이유 : https://hudi.blog/java-requirenonnull/ 참고
        //return "Hello" + name;
    }
}
