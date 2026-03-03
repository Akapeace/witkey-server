package com.witkey.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.witkey.*"})
public class WitkeyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WitkeyApiApplication.class, args);
    }

}
