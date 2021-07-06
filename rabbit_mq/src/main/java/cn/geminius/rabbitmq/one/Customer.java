package cn.geminius.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @author geminius
 * @date 2021/7/4 21:37
 * 消费者 接收消息
 */
public class Customer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        // 声明 接受消息
        DeliverCallback deliverCallback = (cosumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        // 取消消息时的回调
        CancelCallback cancelCallback = (comsumerTag) -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功后是否要自动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
