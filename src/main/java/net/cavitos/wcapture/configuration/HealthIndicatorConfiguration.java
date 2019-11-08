package net.cavitos.wcapture.configuration;

import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.client.actuator.CaptureApiHealthIndicator;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.amqp.RabbitHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthIndicatorConfiguration {

    @Bean
    public CaptureApiHealthIndicator captureApiHealthIndicator(CaptureApiClient captureApiClient) {

        return new CaptureApiHealthIndicator(captureApiClient);
    }

    @Bean
    public RabbitHealthIndicator rabbitHealthIndicator(RabbitTemplate rabbitTemplate) {

        return new RabbitHealthIndicator(rabbitTemplate);
    }
}
