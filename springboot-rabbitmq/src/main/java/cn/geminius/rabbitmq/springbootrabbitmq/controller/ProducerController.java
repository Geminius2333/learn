package cn.geminius.rabbitmq.springbootrabbitmq.controller;

import cn.geminius.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geminius
 * @date 2021/7/11 16:41
 * 开始发消息    测试确认
 */
@Slf4j
@RestController
@RequestMapping("confirm")
public class ProducerController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    // 发消息
    @GetMapping("/sendMessage/{message}")
    public String sendMessage(@PathVariable("message") String message) {

        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, message +"key1", correlationData1);
        log.info("发送消息1内容为: {}", message +"key1");

        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "2", message +"key12", correlationData2);
        log.info("发送消息2内容为: {}", message +"key12");

        return "发送完成";
    }
}