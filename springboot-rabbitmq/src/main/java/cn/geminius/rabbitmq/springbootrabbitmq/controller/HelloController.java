package cn.geminius.rabbitmq.springbootrabbitmq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geminius
 * @date 2021/7/8 21:32
 */

@RestController("hello")
@RequestMapping({"", "/", "hello"})
public class HelloController {

    @GetMapping({"", "hello"})
    public String hello() {
        return "hello world";
    }
}
