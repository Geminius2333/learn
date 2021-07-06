package cn.geminius.rabbitmq.six;

import cn.geminius.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author geminius
 * @date 2021/7/6 21:23
 */
public class DirectLogs {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送:"+message);
        }

    }
}
