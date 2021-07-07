package cn.geminius.rabbitmq.eight;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/7 21:15
 */
public class Consumer2 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2等待接受消息...");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("C2消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {});
    }
}
