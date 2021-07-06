package cn.geminius.rabbitmq.three;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import cn.geminius.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author geminius
 * @date 2021/7/5 20:55
 *
 * 消息在手动应答时不丢失,放回队列重新消费
 */
public class Worker3 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("c1等待消息处理时间较短...");

        // 声明 接受消息
        DeliverCallback deliverCallback = (cosumerTag, message) -> {
            // 沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接受到的消息："+ new String(message.getBody(), StandardCharsets.UTF_8));
            // 手动应答
            /**
             * 1. 消息标识
             * 2. 是否批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        // 取消消息时的回调
        CancelCallback cancelCallback = (comsumerTag) -> {
            System.out.println(comsumerTag + "消息消费被中断");
        };
        // 设置不公平分发 值为1是自动不公平分发
        //int prefetchCount = 1;
        // 设置预取值
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

        // 使用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
