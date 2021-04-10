package com.yc;

import com.yc.biz.StudentBizImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-04 15:12
 */
@Configuration
@ComponentScan(basePackages = "com.yc")//将来要托管的bean要扫描的包以及子包
public class AppConfig {
    @Bean       //IOC   bean的容器是Map<String,Object>
    public StudentBizImpl studentBizImpl()
    {
        return new StudentBizImpl();
    }
}
