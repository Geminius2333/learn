package cn.geminius.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author geminius
 * @date 2021/7/11 17:05
 *
 *  交换机确认   需要再配置文件中配置   spring.rabbitmq.publisher-confirm-type: correlated
 *  回退消息    需要再配置文件中配置   spring.rabbitmq.publisher-returns: true
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 1.交换机接收到了消息,回调方法
     * 1.1.correlationData 保存回调消息的id及相关信息
     * 1.2 ack   交换机是否收到消息   ack = true
     * 1.3 cause  null
     * 2.交换机接收失败了,回调方法
     * 2.1 correlationData 保存回调消息的id及相关信息
     * 2.2 ack   交换机是否收到消息   ack = false
     * 2.3 cause  失败原因
     *
     * @param correlationData
     * @param ack             交换机是否收到消息   ack = true
     * @param cause           null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = Objects.nonNull(correlationData) ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到ID: {}的消息", id);
        } else {
            log.info("交换机还未收到ID: {}的消息, 由于原因: {}", id, cause);
        }
    }

    /**
     * 可以在消息传递过程中不可达目的地的时候将消息返回给生产者
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息:{},被交换机{}退回, 退回原因: {}, 路由key: {}", new String(returned.getMessage().getBody(), StandardCharsets.UTF_8),
                returned.getExchange(), returned.getReplyText(), returned.getRoutingKey());
    }
}
