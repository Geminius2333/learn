package cn.geminius.rabbitmq.springbootrabbitmq.consumer;

import cn.geminius.rabbitmq.springbootrabbitmq.config.DelayQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author geminius
 * @date 2021/7/10 21:28
 * 消费者   基于插件的延迟消息
 */
@Slf4j
@Component
public class DelayQueueConsumer {

    // 监听消息
    @RabbitListener(queues = DelayQueueConfig.DELAY_QUEUE_NAME)
    public void receiveDelayMsg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("[receiveDelayMsg], 当前时间: {},收到延迟队列的消息: {}", LocalDateTime.now(), msg);

    }
}
