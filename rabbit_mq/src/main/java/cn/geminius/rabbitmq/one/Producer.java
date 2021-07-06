package cn.geminius.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author geminius
 * @date 2021/7/4 20:59
 * 生产者 发送消息
 */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置地址
        factory.setHost("127.0.0.1");
        // 设置用户名
        factory.setUsername("admin");
        // 设置密码
        factory.setPassword("123456");
        // 创建连接
        Connection connection = factory.newConnection();
        // 获取Channel
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列消息是否持久化 消息默认存储在内存中
         * 3.该队列是否供多个消费者消费 是否进行消息共享 ture: 多个  false: 单个
         * 4.是否自动删除 最后一个消费者断开连接以后该队列是否自动删除 ture:删除  false:不删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 发消息
        String message = "hello world!!";
        /**
         * 发送一个消息
         * 1.发送到那个交换机
         * 2.路由的key值 本次是队列名称
         * 3.其他参数信息
         * 4.消息的数据内容
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完成");

    }
}
