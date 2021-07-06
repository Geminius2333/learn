package cn.geminius.rabbitmq.five;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/6 20:57
 *
 * 消息接收
 */
public class ReceiveLogs2 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 声明一个临时队列
        /**
         * 生成一个临时队列 ，名称随机
         *  当消费者断开与队列的链接后 队列自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("C2等待接受消息...");
        // 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台打印接收到的消息:"+new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
    }
}
