package cn.geminius.rabbitmq.night;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author geminius
 * @date 2021/7/11 21:00
 * 优先队列
 */
public class Producer {

    public static final String QUEUUE_NAME = "priority_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        Map<String, Object> arguments = new HashMap<>();
        // 官方允许是0-255 此处设置10    允许优化范围为0-10 不要设置过大
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUUE_NAME, true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            if (Objects.equals(i, 5)) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUUE_NAME, null, message.getBytes());
            }
        }
        System.out.println("消费发送成功");
    }

}
