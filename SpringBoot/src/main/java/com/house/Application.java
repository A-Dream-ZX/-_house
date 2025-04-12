package com.house;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 房屋租赁系统主应用类
 * 
 * Spring Boot应用程序的入口点，负责启动整个应用
 * 
 * @SpringBootApplication: 复合注解，包含@Configuration, @EnableAutoConfiguration, @ComponentScan
 *   自动配置Spring Boot应用，开启组件扫描和各种默认配置
 * 
 * @MapperScan: 指定MyBatis Mapper接口所在的包路径，自动将其注册为Spring Bean
 *   这样就不需要在每个Mapper接口上添加@Mapper注解
 */
@SpringBootApplication
@MapperScan("com.house.mapper")
public class Application {

    /**
     * 应用程序主入口方法
     * 
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("启动成功");
    }

}
