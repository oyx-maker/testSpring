package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-05 09:17
 */
@Configuration
@ComponentScan(basePackages ={ "com.huwei","com.mimi"})
public class AppConfig {
    @Bean
    public Random r()
    {
        return new Random();
    }


}
