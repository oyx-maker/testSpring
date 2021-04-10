package com.yc.bean;

import com.yc.springframework.stereotype.MyComponent;
import com.yc.springframework.stereotype.MyPostConstruct;
import com.yc.springframework.stereotype.MyPreDestroy;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-05 11:44
 */
@MyComponent
public class HelloWorld {
    @MyPostConstruct
    public void setUp()
    {
        System.out.println("MyPostConstruct");
    }

    @MyPreDestroy
    public void destory()
    {
        System.out.println("MyPreDestroy");
    }
    public HelloWorld()
    {
        System.out.println("hello world 构造");
    }

    public void show()
    {
        System.out.println("show");
    }






}
