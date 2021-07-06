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
public class ReceiveLogsDirect1 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列
        channel.queueDeclare("console", false, false, false, null);
        // 绑定交换机
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        System.out.println("C1等待接受消息...");
        // 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台打印接收到的消息:"+new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("console", true, deliverCallback, consumerTag -> {});
    }
}
