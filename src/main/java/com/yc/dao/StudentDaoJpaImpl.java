package com.yc.dao;

import org.springframework.stereotype.Repository;

import java.util.Random;

/**
 * @program: testSpring
 * @description: wu
 * @author: oyx
 * @create: 2021-04-04 14:33
 */
@Repository
public class StudentDaoJpaImpl implements StudentDao{
    public StudentDaoJpaImpl()
    {

    }
    @Override
    public int add(String name)
    {
        System.out.println("JPA 添加学生:"+name);
        Random r=new Random();
        return r.nextInt();
    }
    @Override
    public void update(String name)
    {
        System.out.println("JPA 学生:"+name);
    }
}
