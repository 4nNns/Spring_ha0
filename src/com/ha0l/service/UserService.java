package com.ha0l.service;

import com.ha0l.spring.Autowired;
import com.ha0l.spring.Component;
import com.ha0l.spring.Scope;

@Component("userService")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }
}
