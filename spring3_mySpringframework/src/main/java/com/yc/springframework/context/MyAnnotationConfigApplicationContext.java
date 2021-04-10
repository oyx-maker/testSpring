package com.yc.springframework.context;

import com.yc.springframework.stereotype.*;

import java.io.File;
import java.io.FileFilter;
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
public class MyAnnotationConfigApplicationContext implements MyApplicationContext {
    private Map<String ,Object > beanMap=new HashMap<String,Object>();

    public MyAnnotationConfigApplicationContext(Class<?>... componentClasses){
        try {
            register(componentClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(Class<?>[] componentClasses) throws Exception {
        if(componentClasses==null||componentClasses.length<=0){
            throw  new RuntimeException("没有指定配置类");
        }
        for(Class cl:componentClasses) {
            //请实现这个里面的代码
            //原码1：只实现IOC，MyPostConstruct MyPreDestroy
            if(!cl.isAnnotationPresent(MyConfiguration.class)){
                continue;
            }
            String []basePackages=getAppConfigBasePackages(cl);
            if(cl.isAnnotationPresent(MyComponentScan.class)){
                MyComponentScan mcs=(MyComponentScan)cl.getAnnotation(MyComponentScan.class);
                if(mcs.basePackages()!=null && mcs.basePackages().length>0){
                    basePackages=mcs.basePackages();
                }
            }
            //处理@MyBean的情况
            Object obj=cl.newInstance();
            handleAtMyBean(cl,obj);
            for(String basePackage:basePackages){
                scanPackageAndSubPackageClasses(basePackage);
            }
            handleManagedBean();
            handleDi(beanMap);
        }

        //版本2：实现id
    }
    /*
    循环 beanmap中的每个bean ，找到它们每个类中的每个由@Autowiree @Resource z注解的方法以实现di

     */
    private void handleDi(Map<String,Object>beanMap) throws InvocationTargetException, IllegalAccessException {
        Collection<Object>objectCollection=beanMap.values();
        for(Object obj:objectCollection){
            Class cls=obj.getClass();
            Method[]ms=cls.getDeclaredMethods();
            for(Method m:ms){
                if(m.isAnnotationPresent(MyAutowired.class)&&m.getName().startsWith("set")){
                    invokeAutowiredMethod(m,obj);
                }else if(m.isAnnotationPresent(MyResource.class)&&m.getName().startsWith("set")){
                    invokeResourceMethod(m,obj);
                }
            }
            Field[]fs=cls.getDeclaredFields();
            for(Field field:fs){
                if(field.isAnnotationPresent(MyAutowired.class)){

                }else if(field.isAnnotationPresent(MyResource.class)){

                }
            }
        }
    }

    private void invokeResourceMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        MyResource mr=m.getAnnotation(MyResource.class);
        String beanId=mr.name();
        if(beanId==null||beanId.equalsIgnoreCase("")){
            String pname=m.getParameterTypes()[0].getSimpleName();
            beanId=pname.substring(0,1).toLowerCase()+pname.substring(1);
        }
        Object o=beanMap.get(beanId);
        m.invoke(obj,o);

    }

    private void invokeAutowiredMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        Class typeClass=m.getParameterTypes()[0];
        Set<String>keys=beanMap.keySet();
        for(String key:keys){
            Object o=beanMap.get(key);

            Class[]interfaces= o.getClass().getInterfaces();
            for(Class c:interfaces) {
                System.out.println(c.getName()+"\t"+typeClass);
                if (c == typeClass) {

                    m.invoke(obj, o);
                    break;
                }
            }
        }
    }

    private void handleManagedBean() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for(Class c:managedBeanClasses){
            if(c.isAnnotationPresent(MyComponent.class)){
                saveManageBean(c);
            }else if(c.isAnnotationPresent(MyService.class)){
                saveManageBean(c);
            }else if(c.isAnnotationPresent(MyRepository.class)){
                saveManageBean(c);
            }else if(c.isAnnotationPresent(MyController.class)){
                saveManageBean(c);
            }else{

            }
        }
    }

    private void saveManageBean(Class c) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object o=c.newInstance();
        handlePostConstruct(o,c);
        String beanId=c.getSimpleName().substring(0,1).toLowerCase()+c.getSimpleName().substring(1);
        beanMap.put(beanId,o);
    }

    private void scanPackageAndSubPackageClasses(String basePackage) throws Exception {
        String packagePath=basePackage.replaceAll("\\.","/");
        System.out.println("扫描包路径:"+basePackage+",替换后:"+packagePath);
        Enumeration<URL> files=Thread.currentThread().getContextClassLoader().getResources(packagePath);
        while (files.hasMoreElements()){
            URL url=files.nextElement();
            System.out.println("配置的扫描路径为:"+url.getFile());
            findClassesInPackages(url.getFile(),basePackage);
        }
    }
    private Set<Class> managedBeanClasses=new HashSet<Class>();

    private void findClassesInPackages(String file, String basePackage) throws ClassNotFoundException {
        File f=new File(file);
        File []classFiles=f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".class")||file.isDirectory();
            }
        });
        for(File cf:classFiles){
            if(cf.isDirectory()){
                basePackage+="."+cf.getName().substring(cf.getName().lastIndexOf("/")+1);
                findClassesInPackages(cf.getAbsolutePath(),basePackage);
            }else{
                URL[]urls=new URL[]{};
                URLClassLoader ucl=new URLClassLoader(urls) ;
                Class c=ucl.loadClass(basePackage+"."+cf.getName().replace(".class",""));
                managedBeanClasses.add(c);
            }
        }

    }

    private void handleAtMyBean(Class cls, Object obj) throws InvocationTargetException, IllegalAccessException {
        Method[]ms=cls.getDeclaredMethods();
        for(Method m:ms){
            if(m.isAnnotationPresent(MyBean.class)){
                Object o=m.invoke(obj);
                handlePostConstruct(o,o.getClass()) ;
                beanMap.put(m.getName(),o);
            }

        }
    }

    private void handlePostConstruct(Object o, Class<?> cls) throws InvocationTargetException, IllegalAccessException {
        Method[]ms=cls.getDeclaredMethods();
        for(Method m:ms){
            if(m.isAnnotationPresent(MyPostConstruct.class)){
                m.invoke(o);
            }
        }
    }

    private String[] getAppConfigBasePackages(Class cl){
        String[]paths=new String[1];
        paths[0]=cl.getPackage().getName();
        return paths;
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
