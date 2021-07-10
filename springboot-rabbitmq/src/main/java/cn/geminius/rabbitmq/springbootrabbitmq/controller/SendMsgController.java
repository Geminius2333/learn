package cn.geminius.rabbitmq.springbootrabbitmq.controller;


import cn.geminius.rabbitmq.springbootrabbitmq.config.DelayQueueConfig;
import cn.geminius.rabbitmq.springbootrabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
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

    /**
     * 开始发消息 基于插件 延迟消息
     * 结果:
     * [sendDelayMsg],当前时间:2021-07-10T22:26:50.917,发送一条时长20000ms信息给队列delay.queue:zhangsan1
     * [sendDelayMsg],当前时间:2021-07-10T22:26:57.893,发送一条时长2000ms信息给队列delay.queue:lisi2
     * [receiveDelayMsg], 当前时间: 2021-07-10T22:26:59.934,收到延迟队列的消息: lisi2
     * [receiveDelayMsg], 当前时间: 2021-07-10T22:27:10.929,收到延迟队列的消息: zhangsan1
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public String sendDelayMsg(@PathVariable("message") String message,
                               @PathVariable("delayTime") Integer delayTime) {
        log.info("[sendDelayMsg],当前时间:{},发送一条时长{}ms信息给队列delay.queue:{}", LocalDateTime.now(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_EXCHANGE_NAME, DelayQueueConfig.DELAY__ROUTING_KEY, message, m -> {
            m.getMessageProperties().setDelay(delayTime);
            return m;
        });
        return "发送完成";
    }


    /**
     * 开始发消息 消息TTL
     * 请求 10000ms   你好1
     * 2000ms   你好2
     * 结果:
     * [sendExpirationMsg],当前时间:2021-07-10T20:53:37.863,发送一条时长10000ms信息给队列QC:你好1
     * [sendExpirationMsg],当前时间:2021-07-10T20:53:45.171,发送一条时长2000ms信息给队列QC:你好2
     * 当前时间：2021-07-10T20:53:47.891，收到死信队列的消息：你好1
     * 当前时间：2021-07-10T20:53:47.891，收到死信队列的消息：你好2
     * <p>
     * RabbitMQ只会检查第一个消息是否过期,如果第一个消息的延时很长,而第二个消息延时很短,第二个消息并不会优先得到执行
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public String sendExpirationMsg(@PathVariable("message") String message,
                                    @PathVariable("ttlTime") String ttlTime) {
        log.info("[sendExpirationMsg],当前时间:{},发送一条时长{}ms信息给队列QC:{}", LocalDateTime.now(), ttlTime, message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.ROUTING_KEY_XC, message, m -> {
            m.getMessageProperties().setExpiration(ttlTime);
            return m;
        });
        return "发送完成";
    }

    // 开始发消息
    @GetMapping("/sendMsg/{message}")
    public String sendMessage(@PathVariable("message") String message) {
        log.info("当前时间:{},发送一条信息给两个TTL队列:{}", LocalDateTime.now(), message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.ROUTING_KEY_XA, "消息来自ttl为10s的队列:" + message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_EXCHANGE, TtlQueueConfig.ROUTING_KEY_XB, "消息来自ttl为40s的队列:" + message);
        return "发送完成";
    }
}
