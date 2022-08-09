package com.dizhongdi.servicedzd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dizhongdi"})
@MapperScan("com.dizhongdi.servicedzd.mapper")
public class ServiceDzdApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDzdApplication.class, args);
    }

}
