package cn.geminius.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author geminius
 * @date 2021/7/8 21:40
 *  配置类
 */
@Configuration
public class TtlQueueConfig {

    // 普通交换机名称
    public static final String X_EXCHANGE = "X";
    // 死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    // 普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    // 死信队列名称
    public static final String DEAD_LETTER_QUEUE = "QD";
    // 设置普通RoutingKey
    public static final String ROUTING_KEY_XA = "XA";
    public static final String ROUTING_KEY_XB = "XB ";
    // 死信RoutingKey
    public static final String DEAD_LETTER_ROUTING_KEY = "YD";

    // 声明xExchange
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    // 声明yExchange
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    // 声明普通队列 TTL
    @Bean("queueA")
    public Queue queueA() {
        HashMap<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        // 设置TTL
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    // 声明普通队列 TTL
    @Bean("queueB")
    public Queue queueB() {
        HashMap<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        // 设置TTL
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    // 死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    // 绑定
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {

        return BindingBuilder.bind(queueA).to(xExchange).with(ROUTING_KEY_XA);
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {

        return BindingBuilder.bind(queueB).to(xExchange).with(ROUTING_KEY_XB);
    }

    // 绑定
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {

        return BindingBuilder.bind(queueD).to(yExchange).with(DEAD_LETTER_ROUTING_KEY);
    }

}
