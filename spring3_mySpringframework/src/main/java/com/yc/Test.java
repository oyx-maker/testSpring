package com.yc;

import com.yc.springframework.context.MyAnnotationConfigApplicationContext;
import com.yc.springframework.context.MyApplicationContext;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-05 11:47
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext ac=new MyAnnotationConfigApplicationContext(MyAppConfig.class);
//        StudentBizImpl hw= (StudentBizImpl) ac.getBean("studentBizImpl");
//        hw.add("abc");
    }
}
