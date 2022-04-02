package com.ha0l.spring;

import com.ha0l.service.AppConfig;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

public class SpringApplicationContext {

    private Class configClass;

    public SpringApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;

        //扫描
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
                for(File f : files) {
                    String fileName = f.getAbsolutePath();
//                    System.out.println(fileName);

                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));

                        className = className.replace("/", ".");

                        System.out.println(className);

                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(Component.class)) {

                            //bean

                        }
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {

        return null;
    }
}
