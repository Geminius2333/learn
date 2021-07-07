package cn.geminius.rabbitmq.eight;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/7 21:07
 *
 *  成为死信三个原因：
 *      1.消息被拒绝
 *      2.消息TTL过期
 *      3.队列达到最长长度
 *
 *  死信队列生产者
 *
 */
public class Producer {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 死信消息 设置TTL时间
        /*AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                .builder().expiration("10000").build();
                */
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }


    }
}
