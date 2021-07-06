package cn.geminius.rabbitmq.seven;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * @author geminius
 * @date 2021/7/6 22:24
 */
public class EmitLogsTopic {

    // 交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","队列Q1Q2接收");
        bindingKeyMap.put("lazy.orange.elephant","队列Q1Q2接收");
        bindingKeyMap.put("quick.orange.fox","队列Q1接收");
        bindingKeyMap.put("lazy.brown.fox","队列Q2接收");
        bindingKeyMap.put("lazy.pink.rabbit","满足两个绑定，队列Q2接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定，被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","四个单词，不匹配任何绑定，被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","四个单词，队列Q2接收");
        for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
            String routingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息：" + message);
        }
    }
//    运行结果
//    生产者发送消息：四个单词，不匹配任何绑定，被丢弃
//    生产者发送消息：不匹配任何绑定，被丢弃
//    生产者发送消息：队列Q1Q2接收
//    生产者发送消息：队列Q2接收
//    生产者发送消息：队列Q1Q2接收
//    生产者发送消息：队列Q1接收
//    生产者发送消息：满足两个绑定，队列Q2接收一次
//    生产者发送消息：四个单词，队列Q2接收
}
