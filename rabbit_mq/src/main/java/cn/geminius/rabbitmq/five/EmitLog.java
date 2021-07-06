package cn.geminius.rabbitmq.five;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author geminius
 * @date 2021/7/6 21:07
 *
 * 发消息 交换机
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送:"+message);
        }

    }
}
