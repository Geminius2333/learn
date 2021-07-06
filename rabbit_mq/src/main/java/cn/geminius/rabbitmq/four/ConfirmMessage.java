package cn.geminius.rabbitmq.four;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author geminius
 * @date 2021/7/5 21:59
 * <p>
 * 发布确认方式
 * 使用的时间   比较哪种是最好的
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量测试
 */
public class ConfirmMessage {

    // 批量发送消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1.单个确认
        //ConfirmMessage.publishMessageIndividually();  // 发布1000个单独确认时间194ms
        // 2.批量确认
        //ConfirmMessage.publishMessageBatch();  // 发布1000个批量确认消息时间33ms
        // 3.异步批量
        ConfirmMessage.publishMessageAsync();   // 发布1000个异步发布确认消息时间29ms

    }

    // 单个发送 确认信息
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("发送成功");
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息时间" + (end - begin) + "ms");
    }

    // 批量确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量确认消息大小
        int batchSize = 100;

        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //  判断达到100批量确认一次
            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息时间" + (end - begin) + "ms");

    }

    // 异步 确认信息
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启确认
        channel.confirmSelect();
        /**
         * 线程安全有序的 hashmap 适用于高并发
         * 1.轻松的将序号和消息进行关联
         * 2.可以轻松批量条数 只要给序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outStandingConfirms = new ConcurrentSkipListMap<>();

        // 消息确定成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                // 2.删除已确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outStandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认消息：" + deliveryTag);
        };
        // 消息确认失败
        /**
         * 1. 消息的标记
         * 2. 是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 3.打印一下未确认的消息有哪些
            String message = outStandingConfirms.get(deliveryTag);
            System.out.println("未确认消息标识：" + deliveryTag + ", 未确认消息：" + message);
        };
        // 准备消息的监听器 监听哪些消息成功， 哪些消息失败
        /**
         * 1.监听哪些消息成功了
         * 2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);  // 异步
        // 开始时间
        long begin = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 1.记录发送的消息
            outStandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步发布确认消息时间" + (end - begin) + "ms");
    }
}
