package cn.geminius.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geminius
 * @date 2021/7/10 21:13
 * RabbitMQ需要先安装 rabbitmq_delayed_message_exchange 插件
 */
@Configuration
public class DelayQueueConfig {

    // 交换机
    public static final String DELAY_EXCHANGE_NAME = "delay.exchange";
    public static final String DELAY_EXCHANGE_TYPE = "x-delayed-message";
    // 队列
    public static final String DELAY_QUEUE_NAME = "delay.queue";
    // RoutingKey
    public static final String DELAY__ROUTING_KEY = "delay.routingkey";

    // 声明交换机
    @Bean("delayExchange")
    public CustomExchange delayExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        /**
         * 1. 交换机名称
         * 2. 交换机类型
         * 3. 是否持久化
         * 4. 是否自动删除
         * 5. 其他参数
         */
        return new CustomExchange(DELAY_EXCHANGE_NAME, DELAY_EXCHANGE_TYPE, true, false, arguments);
    }

    // 声明队列
    @Bean("delayQueue")
    public Queue delayQueue() {
        return new Queue(DELAY_QUEUE_NAME);
    }

    // 绑定
    @Bean
    public Binding delayQueueBindingDelayExchange(@Qualifier("delayQueue") Queue delayQueue,
                                                  @Qualifier("delayExchange") CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY__ROUTING_KEY).noargs();
    }

}
