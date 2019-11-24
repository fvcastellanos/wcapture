package net.cavitos.wcapture.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.message.listener.CaptureRequestListener;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerConfiguration {

    @Bean
    public MessageListener captureRequestListener(ObjectMapper objectMapper,
                                                  CaptureApiClient captureApiClient,
                                                  CaptureRepository captureRepository) {

        return new CaptureRequestListener(objectMapper, captureApiClient, captureRepository);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         Queue captureRequestQueue,
                                                                         MessageListener captureRequestListener) {

        var container = new SimpleMessageListenerContainer();
        container.setQueues(captureRequestQueue);
        container.setMessageListener(captureRequestListener);
        container.setConnectionFactory(connectionFactory);

        return container;
    }

}
