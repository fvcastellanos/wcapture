package net.cavitos.wcapture.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cavitos.wcapture.message.producer.CaptureRequestAmqpProducer;
import net.cavitos.wcapture.message.producer.CaptureRequestProducer;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptureRequestConfiguration {

    @Value("${web.capture.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${web.capture.rabbitmq.queue}")
    private String queueName;

    @Bean
    public CaptureRequestProducer captureRequestProducer(ObjectMapper objectMapper, RabbitOperations rabbitOperations) {

        return new CaptureRequestAmqpProducer(exchangeName, queueName, objectMapper, rabbitOperations);
    }
}
