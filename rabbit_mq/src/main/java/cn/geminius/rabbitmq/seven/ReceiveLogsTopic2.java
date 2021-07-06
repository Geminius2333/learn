package cn.geminius.rabbitmq.seven;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/6 22:14
 *
 * 主题订阅 消费者C1
 */
public class ReceiveLogsTopic2 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("消费者C2等待接受消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("控制台打印接收到的消息:"+new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接收队列：" + queueName + "  绑定键：" + message.getEnvelope().getRoutingKey());
        };
        // 接收消息
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    //    运行结果
//    消费者C2等待接受消息...
//    控制台打印接收到的消息:队列Q1Q2接收
//    接收队列：Q2  绑定键：lazy.orange.elephant
//    控制台打印接收到的消息:队列Q2接收
//    接收队列：Q2  绑定键：lazy.brown.fox
//    控制台打印接收到的消息:队列Q1Q2接收
//    接收队列：Q2  绑定键：quick.orange.rabbit
//    控制台打印接收到的消息:满足两个绑定，队列Q2接收一次
//    接收队列：Q2  绑定键：lazy.pink.rabbit
//    控制台打印接收到的消息:四个单词，队列Q2接收
//    接收队列：Q2  绑定键：lazy.orange.male.rabbit
}
