package com.ha0l.spring;

import com.ha0l.service.AppConfig;

public class SpringApplicationContext {

    private Class configClass;

    public SpringApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    public Object getBean(String beanName) {

        return null;
    }
}
