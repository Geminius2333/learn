package cn.geminius.rabbitmq.springbootrabbitmq.controller;


import cn.geminius.rabbitmq.springbootrabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author geminius
 * @date 2021/7/8 22:00
 * 发送延迟消息
 */
@Slf4j
@RestController("ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 开始发消息
    @GetMapping("/sendMsg/{message}")
    public String sendMessage(@PathVariable("message") String message) {
        log.info("当前时间:{},发送一条信息给两个TTL队列:{}", LocalDateTime.now(), message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.ROUTING_KEY_XA, "消息来自ttl为10s的队列:" + message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.ROUTING_KEY_XB, "消息来自ttl为40s的队列:" + message);
        return "发送完成";
    }
}
