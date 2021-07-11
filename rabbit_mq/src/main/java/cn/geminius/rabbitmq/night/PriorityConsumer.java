package cn.geminius.rabbitmq.night;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/11 21:07
 * 优先队列消费者
 */
public class PriorityConsumer {

    public static final String QUEUE_NAME = "priority_queue";

    /**
     * 运行结果:
     * 优先队列消费者收到消息: info5
     * 优先队列消费者收到消息: info1
     * 优先队列消费者收到消息: info2
     * 优先队列消费者收到消息: info3
     * 优先队列消费者收到消息: info4
     * 优先队列消费者收到消息: info6
     * 优先队列消费者收到消息: info7
     * 优先队列消费者收到消息: info8
     * 优先队列消费者收到消息: info9
     * 优先队列消费者收到消息: info10
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("优先队列消费者收到消息: " + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
