package com.yc.biz;

import org.springframework.stereotype.Component;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-04 15:04
 */
@Component
public class HelloWorld {
    public HelloWorld()
    {
        System.out.println("无参构造方法");
    }
    public void hello()
    {
        System.out.println("hello world");
    }
}
