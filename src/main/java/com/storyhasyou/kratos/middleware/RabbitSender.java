package com.storyhasyou.kratos.middleware;

import com.google.common.collect.Maps;
import com.storyhasyou.kratos.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The type Rabbit sender.
 *
 * @author fangxi
 */
@Slf4j
public class RabbitSender {


    /**
     * key: topic
     */
    private final Map<String, RabbitTemplate> rabbitTemplateMap = new ConcurrentHashMap<>(1 << 7);
    /**
     * The Connection factory.
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 发送消息的方法
     *
     * @param <T>        the type parameter
     * @param body       消息主体
     * @param exchange   the exchange
     * @param routingKey the routing key
     */
    public <T> void send(T body, String exchange, String routingKey) {
        this.send(body, exchange, routingKey, null);
    }


    /**
     * Send.
     *
     * @param <T>             the type parameter
     * @param body            the body
     * @param exchange        the exchange
     * @param routingKey      the routing key
     * @param confirmCallback the confirm callback
     */
    public <T> void send(T body, String exchange, String routingKey, RabbitTemplate.ConfirmCallback confirmCallback) {
        this.send(body, Maps.newHashMap(), exchange, routingKey, confirmCallback, null);

    }

    /**
     * Send delay.
     *
     * @param <T>        the type parameter
     * @param body       the body
     * @param exchange   the exchange
     * @param routingKey the routing key
     * @param ttl        the ttl
     */
    public <T> void sendDelay(T body, String exchange, String routingKey, long ttl) {
        this.send(body, Maps.newHashMap(), exchange, routingKey, null, message -> {
            message.getMessageProperties().setHeader("x-delay", ttl);
            message.getMessageProperties().setExpiration(String.valueOf(ttl));
            return message;
        });

    }

    /**
     * Send delay.
     *
     * @param <T>                  the type parameter
     * @param body                 the body
     * @param exchange             the exchange
     * @param routingKey           the routing key
     * @param messagePostProcessor the message post processor
     */
    public <T> void sendDelay(T body, String exchange, String routingKey, MessagePostProcessor messagePostProcessor) {
        this.send(body, Maps.newHashMap(), exchange, routingKey, null, messagePostProcessor);
    }

    /**
     * Send.
     *
     * @param <T>                  the type parameter
     * @param body                 the body
     * @param properties           the properties
     * @param exchange             the exchange
     * @param routingKey           the routing key
     * @param confirmCallback      the confirm callback
     * @param messagePostProcessor the message post processor
     */
    public <T> void send(T body, Map<String, Object> properties, String exchange, String routingKey,
                         RabbitTemplate.ConfirmCallback confirmCallback, MessagePostProcessor messagePostProcessor) {
        if (log.isDebugEnabled()) {
            log.debug("发送mq消息, 内容: {}, 附加属性: {}, change: {}, routingKey: {}", body, properties, exchange, routingKey);
        }
        // 构造消息
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<T> msg = MessageBuilder.createMessage(body, messageHeaders);

        CorrelationData correlationData = new CorrelationData(IdUtils.uuid());
        RabbitTemplate rabbitTemplate = this.getRabbitTemplate(exchange, routingKey, confirmCallback);
        if (messagePostProcessor == null) {
            rabbitTemplate.convertAndSend(exchange, routingKey, msg, correlationData);
        } else {
            rabbitTemplate.convertAndSend(exchange, routingKey, msg, messagePostProcessor, correlationData);
        }

    }

    /**
     * Gets rabbit template.
     *
     * @param topic           the topic
     * @param routingKey      the routing key
     * @param confirmCallback the confirm callback
     * @return the rabbit template
     */
    private RabbitTemplate getRabbitTemplate(String topic, String routingKey,
                                             RabbitTemplate.ConfirmCallback confirmCallback) {
        Assert.notNull(topic, "topic must not be null");
        RabbitTemplate rabbitTemplate = rabbitTemplateMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());
        newRabbitTemplate.setRoutingKey(routingKey);
        if (confirmCallback != null) {
            newRabbitTemplate.setConfirmCallback(confirmCallback);
        }
        rabbitTemplateMap.putIfAbsent(topic, newRabbitTemplate);
        return newRabbitTemplate;
    }
}
