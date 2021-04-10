package com.yc.biz;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-04 15:12
 */
@Configuration
@ComponentScan(basePackages = "com.yc.biz")//将来要托管的bean要扫描的包以及子包
public class AppConfig {
}
