package cn.geminius.rabbitmq.six;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/6 21:16
 */
public class ReceiveLogsDirect2 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列
        channel.queueDeclare("disk", false, false, false, null);
        // 绑定交换机
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        System.out.println("C2等待接受消息...");
        // 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台打印接收到的消息:"+new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {});
    }
}
