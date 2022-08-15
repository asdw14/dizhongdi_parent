package com.dizhongdi.servicees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ClassName:ServiceEsApplication
 * Package:com.dizhongdi.servicees
 * Description:
 *
 * @Date: 2022/8/15 15:44
 * @Author:dizhongdi
 */
@SpringBootApplication
@ComponentScan("com.dizhongdi")
public class ServiceEsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceEsApplication.class,args);
    }
}
