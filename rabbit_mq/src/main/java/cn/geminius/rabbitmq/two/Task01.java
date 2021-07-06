package cn.geminius.rabbitmq.two;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author geminius
 * @date 2021/7/4 22:54
 *
 * 生产者 发送大量消息
 */
public class Task01 {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列消息是否持久化 消息默认存储在内存中
         * 3.该队列是否供多个消费者消费 是否进行消息共享 ture: 多个  false: 单个
         * 4.是否自动删除 最后一个消费者断开连接以后该队列是否自动删除 ture:删除  false:不删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 从控制台获取信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null , message.getBytes());
            System.out.println("发送消息完成："+ message);
        }

    }
}
