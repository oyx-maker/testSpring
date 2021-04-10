package com.yc.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-09 19:34
 */
//切面类   你要增强的功能放这里
    @Aspect
    @Component//IOC注解  以实现Spring托管的功能
public class LogAspect {
        //切入点的声明
    @Pointcut("execution(* com.yc.biz.StudentBizImpl.add*(..))")//切入点表达式：在哪些方法上加注释
    private void add()
    {

    }
    @Pointcut("execution(* com.yc.biz.StudentBizImpl.update*(..))")
    private void update()
    {

    }


    @Pointcut("execution(* com.yc.biz.StudentBizImpl.find*(..))")
    private void find()
    {

    }

    @Pointcut("add() || update()")
    private void addAndUpdate()
    {


    }

    //切入点表达式的做法：？代表出现0次或者1次
    //modifiers-pattern：修饰衔
    //ret-type-pattern:返回类型
    //declaring-type-pattern：
    //name-pattern：名字模型
    //throws-pattern
    //execution(modifiers-pattern? ret-type-pattern  declaring-type-pattern？name-pattern(param throws pattern))

    @Before("com.yc.aspect.LogAspect.addAndUpdate()")
    public void log()
    {
        System.out.println("=======================前置增强的日志==============================");
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dstr=sdf.format(date);
        System.out.println("时间："+dstr);
        System.out.println("=======================前置增强的日志结束==============================");
    }

}
