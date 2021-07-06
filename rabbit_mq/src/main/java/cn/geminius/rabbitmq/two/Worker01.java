package cn.geminius.rabbitmq.two;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author geminius
 * @date 2021/7/4 22:46
 *
 * 工作线程，相当于之前的消费者
 */
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        // 声明 接受消息
        DeliverCallback deliverCallback = (cosumerTag, message) -> {
            System.out.println("接受到的消息："+ new String(message.getBody()));
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = (comsumerTag) -> {
            System.out.println(comsumerTag + "消息消费被中断");
        };
        System.out.println("c2等待接受消息...");
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

    }
}
