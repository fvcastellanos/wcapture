package net.cavitos.wcapture.message.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import net.cavitos.wcapture.client.model.CaptureRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitOperations;

public class CaptureRequestAmqpProducer implements CaptureRequestProducer {

    private static final Logger logger = LoggerFactory.getLogger(CaptureRequestAmqpProducer.class);

    private final ObjectMapper objectMapper;
    private final String exchange;
    private final String queue;

    private final RabbitOperations rabbitOperations;

    public CaptureRequestAmqpProducer(String exchange, String queue, ObjectMapper objectMapper, RabbitOperations rabbitOperations) {

        this.objectMapper = objectMapper;
        this.exchange = exchange;
        this.queue = queue;
        this.rabbitOperations = rabbitOperations;
    }

    @Override
    public void produce(CaptureRequest request) {

        Try.of(() -> {

            var message = objectMapper.writeValueAsString(request);
            rabbitOperations.convertAndSend(exchange, queue, message);
            return message;
        }).onSuccess(message -> logger.info("capture request {} queued", message))
                .onFailure(ex -> logger.error("can't queue requestId={} - ", request.getRequestId(), ex));
    }
}
