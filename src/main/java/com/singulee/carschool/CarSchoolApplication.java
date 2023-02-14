package com.singulee.carschool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.singulee.carschool.pojo"})
public class CarSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarSchoolApplication.class, args);
    }

}
