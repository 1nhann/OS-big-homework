package com.example.czxtks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class CzxtksApplication {

    public static void main(String[] args) {
        SpringApplication.run(CzxtksApplication.class, args);
    }

}
