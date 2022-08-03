package com.dizhongdi.serviceevent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.dizhongdi.serviceevent.mapper")
@ComponentScan("com.dizhongdi")
public class ServiceEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceEventApplication.class, args);
    }

}
