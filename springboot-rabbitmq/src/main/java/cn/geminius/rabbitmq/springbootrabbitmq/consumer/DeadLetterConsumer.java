package cn.geminius.rabbitmq.springbootrabbitmq.consumer;

import cn.geminius.rabbitmq.springbootrabbitmq.config.TtlQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author geminius
 * @date 2021/7/8 22:10
 * 队列TTL 消费者
 */
@Slf4j
@Component
public class DeadLetterConsumer {

    /**
     *  接收消息
     */
    @RabbitListener(queues = TtlQueueConfig.DEAD_LETTER_QUEUE)
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到死信队列的消息：{}", LocalDateTime.now(), msg);
    }
}
