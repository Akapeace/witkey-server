package com.witkey.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.witkey.*"})
public class WitkeyWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WitkeyWebApplication.class, args);
    }

}
