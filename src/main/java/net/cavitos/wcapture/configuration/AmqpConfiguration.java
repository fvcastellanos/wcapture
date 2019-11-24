package net.cavitos.wcapture.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RabbitAutoConfiguration.class)
public class AmqpConfiguration {

    @Value("${web.capture.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${web.capture.rabbitmq.queue}")
    private String queueName;

    @Bean
    public Exchange exchange() {

        return ExchangeBuilder.directExchange(exchangeName)
                .build();
    }

    @Bean
    public Queue captureRequestQueue() {

        return new Queue(queueName, false);
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {

        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(queueName)
                .noargs();
    }
}