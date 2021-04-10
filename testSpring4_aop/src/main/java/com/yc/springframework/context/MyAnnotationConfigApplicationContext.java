package com.yc.springframework.context;

import com.yc.springframework.stereotype.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-05 11:41
 */
public class MyAnnotationConfigApplicationContext implements MyApplicationContext{
    private Map<String,Object> beanMap=new HashMap<String, Object>();

    public MyAnnotationConfigApplicationContext(Class<?>... componentClasses)
    {
        try {
            Register(componentClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Register(Class<?>[] componentClasses) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ClassNotFoundException {
        if (componentClasses==null || componentClasses.length<=0)
        {
            throw new RuntimeException("没有指定配置类");
        }
        for (Class cl:componentClasses)
        {
        //只实现IOC  MyPostConstruct  MypreDestroy

            if(!cl.isAnnotationPresent(MyConfiguration.class))
            {
                continue;
            }
            String[] basePackages=getAnnoConfigBasePackages(cl);
            if(!cl.isAnnotationPresent(MyComponentScan.class))
            {
                MyComponentScan mcs= (MyComponentScan) cl.getAnnotation(MyComponentScan.class);
                if (mcs.basePackages() !=null && mcs.basePackages().length>0)
                {
                    basePackages=mcs.basePackages();
                }
            }
        Object obj=cl.newInstance();
        handleAtMyBean(cl,obj);
        for (String basePackage:basePackages)
        {
            ScanPackageandpackageClasses(basePackage);
        }
        }
    }

    private void handleDi(Map<String,Object> beanMap) throws InvocationTargetException, IllegalAccessException {
        Collection<Object> objectCollection=beanMap.values();
        for (Object obj:objectCollection)
        {
            Class cls=obj.getClass();
            Method[] ms=cls.getDeclaredMethods();
            for (Method m:ms)
            {
                if (m.isAnnotationPresent(MyAutowired.class) && m.getName().startsWith("set"))
                {
                    invokeAutowiredMethod(m,obj);
                }else if (m.isAnnotationPresent(MyResource.class) && m.getName().startsWith("set"))
                {
                    invokeResourceMethod(m,obj);
                }
            }
            Field[] fs=cls.getDeclaredFields();
            for (Field field:fs)
            {
                if (field.isAnnotationPresent(MyAutowired.class))
                {

                }else if (field.isAnnotationPresent(MyResource.class))
                {

                }
            }
        }
    }

    private void invokeAutowiredMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1.取出  m的参数属性值
      Class typeClass=m.getParameterTypes()[0];
        //2. 从beanMap中循环所有的OBJECT
        Set<String> keys=beanMap.keySet();
        for (String key:keys)
        {
            //4.如果是  从beanMap中取出
            Object o=beanMap.get(key);
            //3.判断 这些object是否为参数类型的实例  instanceof
            Class[] interfaces=o.getClass().getInterfaces();
            for (Class c:interfaces)
            {
                System.out.println(c.getName()+"\t"+typeClass);
                if(c==typeClass)
                {
                    m.invoke(obj,o);
                    break;
                }
            }

        }

    }

    private void invokeResourceMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1.取出  MyResource 中的name属性值   当成beanid
        MyResource mr=m.getAnnotation(MyResource.class);
        String beanId=mr.name();
        //2. 如果没有  则取出 m方法中的参数类型名  改成首字小写
        if (beanId==null || beanId.equalsIgnoreCase(""))
        {
            String pname=m.getParameterTypes()[0].getSimpleName();
            beanId=pname.substring(0,1).toLowerCase()+pname.substring(1);
        }
        //3.从beanMap取出
        Object o=beanMap.get(beanId);
        //4.invoke
        m.invoke(obj,o);

    }

    private void ScanPackageandpackageClasses(String basePackage) throws IOException, ClassNotFoundException {
        String packagePath=basePackage.replaceAll("\\.","/");
        System.out.println("扫描包路径："+basePackage+",替换后："+packagePath);
        Enumeration<URL> files=Thread.currentThread().getContextClassLoader().getResources(packagePath);
        while (files.hasMoreElements())
        {
            URL url=files.nextElement();
            System.out.println("配置的扫描路径为："+url.getFile());
            findClassesInPackages(url.getFile(),basePackage);
        }
    }
    private Set<Class> managedBeanCLasses=new HashSet<Class>();
    private void findClassesInPackages(String file, String basePackage) throws ClassNotFoundException {
        File f=new File(file);
        File[] classesFiles=f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".class") || file.isDirectory();
            }
        });
        //System.out.println(classesFiles);
        for (File cf:classesFiles)
        {
            if (cf.isDirectory())
            {
                basePackage+="."+cf.getName().substring(cf.getName().lastIndexOf("/")+1);
                findClassesInPackages(cf.getAbsolutePath(),basePackage);
            }else
            {
                URL[] urls=new URL[]{};
                URLClassLoader ucl=new URLClassLoader(urls);
                Class c=ucl.loadClass(basePackage+"."+cf.getName().replaceAll(".class",""));

                managedBeanCLasses.add(c);
            }
        }
    }
    //处理managedBeanClasses  所有的Class类  筛选所有的@Component  @Service  @Repository 的类  并实例化  存到beanMap中

    private void handleManagedBean() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class c:managedBeanCLasses)
        {
            if (c.isAnnotationPresent(MyComponent.class))
            {
                saveManagedBean(c);
            }else if (c.isAnnotationPresent(MyService.class))
            {
                saveManagedBean(c);
            }else if (c.isAnnotationPresent(MyRepository.class))
            {
                saveManagedBean(c);
            }else if (c.isAnnotationPresent(MyController.class))
            {
                saveManagedBean(c);
            }else{

            }
        }
    }

    private void saveManagedBean(Class c) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object o=c.newInstance();
        handlePostConstruct(o,c);
        String beanId=c.getSimpleName().substring(0,1).toLowerCase()+c.getSimpleName().substring(1);
        beanMap.put(beanId,o);

    }


    //实现DI
    private void handleAtMyBean(Class cls,Object obj) throws InvocationTargetException, IllegalAccessException {
        //获取cls所有的method
        //获取这个类中所有的方法
        Method[] ms=cls.getDeclaredMethods();
            for (Method m:ms)
            {
                //2.判断  循环 每个method上是否有  @MYBEAN 注解
                if (m.isAnnotationPresent(MyBean.class))
                {
                   Object o=m.invoke(obj);
                   //TODO:加入处理
                    handlePostConstruct(o,o.getClass());  //o:Helloworld 对象
                   beanMap.put(m.getName(),o);
                }


            }
        }
/*
处理一个bean中的  @MyPostConstruct 对应的方法
 */
    private void handlePostConstruct(Object o, Class<?> aClass) throws InvocationTargetException, IllegalAccessException {
        Method[] ms=aClass.getDeclaredMethods();
        for (Method m:ms)
        {
            //2.判断  循环 每个method上是否有  @MYBEAN 注解
                if (m.isAnnotationPresent(MyPostConstruct.class))
            {
               m.invoke(o);

            }


        }
    }


    //有 则invoke它 它有返回值  将空值存到  beanmap



    //获取当前  AppConfig
    private String[] getAnnoConfigBasePackages(Class cl)
    {
        String [] paths=new String[1];
        paths[0] =cl.getPackage().getName();
        return  paths;
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
