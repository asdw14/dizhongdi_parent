package com.dizhongdi.servicedzd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dizhongdi"})
@MapperScan("com.dizhongdi.servicedzd.mapper")
//Feign消费者
@EnableFeignClients
//Nacos
@EnableDiscoveryClient
public class ServiceDzdApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDzdApplication.class, args);
    }

}
