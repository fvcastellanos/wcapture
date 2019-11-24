package net.cavitos.wcapture.message.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import io.vavr.control.Try;
import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.model.CaptureHistoryFactory;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class CaptureRequestListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CaptureRequestListener.class);

    private final CaptureApiClient captureApiClient;
    private final CaptureRepository captureRepository;
    private final ObjectMapper objectMapper;

    public CaptureRequestListener(ObjectMapper objectMapper, CaptureApiClient captureApiClient, CaptureRepository captureRepository) {

        this.captureApiClient = captureApiClient;
        this.captureRepository = captureRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Timed("capture_url")
    public void onMessage(Message message) {

        Try.of(() -> {

            var value = new String(message.getBody());
            logger.info("message={}", value);

            var request = objectMapper.readValue(value, CaptureRequest.class);

            captureUrl(request.getRequestId(), request.getUrl());

            return request;
        });
    }

    private void captureUrl(String requestId, String url) {

        try {

            var result = captureApiClient.captureUrl(requestId, url);

            CaptureHistory captureHistory = result.isRight() ? CaptureHistoryFactory.fromCaptureResponse(result.get())
                    : CaptureHistoryFactory.fromCaptureError(url, result.getLeft());

            captureRepository.save(captureHistory);
            logger.info("storing capture_history={}", captureHistory);

        } catch (Exception ex) {

            logger.error("can't capture url={}, requestId={} - ", url, requestId, ex);
        }
    }
}
