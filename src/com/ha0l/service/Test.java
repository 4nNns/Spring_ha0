package com.ha0l.service;

import com.ha0l.spring.SpringApplicationContext;

public class Test {

    public static void main(String[] args) throws ClassNotFoundException {

        SpringApplicationContext springApplicationContext = new SpringApplicationContext(AppConfig.class);

        UserService userService = (UserService) springApplicationContext.getBean("userService");


    }
}
