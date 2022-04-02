package com.ha0l.spring;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class SpringApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, BeanDefiniation> beanDefinitionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public SpringApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;

        //扫描 ----> BeanDefiniation ----> beanDefinitionMap
        if (configClass.isAnnotationPresent(ComponentScan.class)) {

            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value(); //扫描路径 com.ha0l.service

            path = path.replace(".", "/");

            ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            File file = new File(resource.getFile());
//            System.out.println(file);

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
//                    System.out.println(fileName);

                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));

                        className = className.replace("/", ".");

//                        System.out.println(className);

                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            Component component = clazz.getAnnotation(Component.class);
                            String beanName = component.value();

                            //bean
                            BeanDefiniation beanDefiniation = new BeanDefiniation();
                            beanDefiniation.setType(clazz);

                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                beanDefiniation.setScope(scopeAnnotation.value());
                            } else {
                                beanDefiniation.setScope("singleton");
                            }

                            beanDefinitionMap.put(beanName, beanDefiniation);
                        }
                    }
                }
            }
        }

        //实例化单例bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefiniation beanDefiniation = beanDefinitionMap.get(beanName);
            if (beanDefiniation.getScope().equals("singleton")) {

                Object bean = createBean(beanName, beanDefiniation);
                singletonObjects.put(beanName, bean);
            }
        }

    }

    private Object createBean(String beanName, BeanDefiniation beanDefiniation) {

        Class clazz = beanDefiniation.getType();

        try {
            Object instance = clazz.getConstructor().newInstance();
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Object getBean(String beanName) {

        BeanDefiniation beanDefiniation = beanDefinitionMap.get(beanName);
        if (beanDefiniation == null) {
            throw new NullPointerException();
        } else {
            String scope = beanDefiniation.getScope();

            if (scope.equals("singleton")) {
                Object bean = singletonObjects.get(beanName);
                if (bean == null) {
                    Object bean1 = createBean(beanName, beanDefiniation);
                    singletonObjects.put(beanName, bean1);
                }
                return bean;
            } else {
                //多例
                return createBean(beanName, beanDefiniation);
            }
        }
    }
}
