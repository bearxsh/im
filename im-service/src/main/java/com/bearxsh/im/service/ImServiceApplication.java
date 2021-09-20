package com.bearxsh.im.service;

import com.bearxsh.im.api.GreetingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImServiceApplication {

    @DubboReference
    private GreetingService greetingService;

    public static void main(String[] args) {
        SpringApplication.run(ImServiceApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> System.out.println(greetingService.sayHi("fjkafj"));
    }
}
