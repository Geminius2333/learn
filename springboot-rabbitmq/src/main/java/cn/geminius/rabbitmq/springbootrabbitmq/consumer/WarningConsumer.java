package cn.geminius.rabbitmq.springbootrabbitmq.consumer;

import cn.geminius.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/11 20:34
 * 报警消费者
 */
@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveMsg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("报警消费者收到消息:{}", msg);
    }
}
