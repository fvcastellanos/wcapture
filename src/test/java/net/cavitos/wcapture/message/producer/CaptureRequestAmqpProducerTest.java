package net.cavitos.wcapture.message.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cavitos.wcapture.fixture.CaptureApiClientFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaptureRequestAmqpProducerTest {

    private static final String EXCHANGE = "test-exchange";
    private static final String QUEUE = "test-queue";

    @Mock
    private RabbitOperations rabbitOperations;

    private ObjectMapper objectMapper;
    private CaptureRequestProducer captureRequestProducer;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        captureRequestProducer = new CaptureRequestAmqpProducer(EXCHANGE, QUEUE, objectMapper, rabbitOperations);
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(rabbitOperations);
    }

    @Test
    void testProduceRequest() throws Exception {

        var request = CaptureApiClientFixture.buildCaptureRequest("123", "http://foo.bar");
        var message = objectMapper.writeValueAsString(request);

        doNothing().when(rabbitOperations)
                .convertAndSend(EXCHANGE, QUEUE, message);

        captureRequestProducer.produce(request);

        verify(rabbitOperations).convertAndSend(EXCHANGE, QUEUE, message);
    }
}
