package com.yc.dao;

import java.util.Random;

/**
 * @program: testSpring
 * @description: wu
 * @author: oyx
 * @create: 2021-04-04 14:28
 */
public class StudentDaoMyBatisImpl implements StudentDao{
    @Override
    public int add(String name)
    {
        System.out.println("mybatis 添加学生:"+name);
        Random r=new Random();
        return r.nextInt();
    }
    @Override
    public void update(String name)
    {
        System.out.println("mybatis更新 学生:"+name);
    }
}
