package com.csye6225.piggymemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PiggymemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PiggymemoApplication.class, args);
    }
}
