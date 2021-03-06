package net.cavitos.wcapture.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.vavr.control.Either;
import io.vavr.control.Try;
import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;
import net.cavitos.wcapture.client.model.HealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import java.util.Collections;

public class CaptureApiDefaultClient implements CaptureApiClient {

    private static final Logger logger = LoggerFactory.getLogger(CaptureApiDefaultClient.class);

    private final MeterRegistry meterRegistry;

    private final ObjectMapper objectMapper;
    private final RestOperations restOperations;
    private final String captureApiUrl;
    private final String healthApiUrl;

    public CaptureApiDefaultClient(final ObjectMapper objectMapper, final RestOperations restOperations,
                                   String captureApiUrl, String healthApiUrl, MeterRegistry meterRegistry) {

        this.captureApiUrl = captureApiUrl;
        this.objectMapper = objectMapper;
        this.restOperations = restOperations;
        this.meterRegistry = meterRegistry;
        this.healthApiUrl = healthApiUrl;
    }

    @Override
    public Either<ErrorResponse, CaptureResponse> captureUrl(String requestId, String url) {

        try {

            logger.info("capture request for url={}, requestId={}", url, requestId);

            var request = buildRequest(requestId, url);
            var responseEntity = restOperations.postForEntity(captureApiUrl, request, String.class);

            measureCaptureApiResponseCode(responseEntity.getStatusCodeValue());
            return Either.right(objectMapper.readValue(responseEntity.getBody(), CaptureResponse.class));

        } catch(HttpClientErrorException ex) {

            measureCaptureApiResponseCode(ex.getRawStatusCode());
            return Either.left(buildSerializedErrorResponse(requestId, ex.getResponseBodyAsString()));
        } catch (Exception ex) {

            measureCaptureApiResponseCode(500);
            logger.error("can't process capture request for url={}, requestId={} - ", url, requestId, ex);
            var message = String.format("can't process capture request for url=%s, requestId=%s", url, requestId);
            return Either.left(buildErrorResponse(requestId, message));
        }
    }

    @Override
    public Try<HealthResponse> getHealth() {

        return Try.of(() -> restOperations.getForEntity(healthApiUrl, HealthResponse.class))
                .map(ResponseEntity::getBody);
    }

    // -------------------------------------------------------------------------------------

    private CaptureRequest buildRequest(String requestId, String url) {

        var request = new CaptureRequest();
        request.setRequestId(requestId);
        request.setUrl(url);

        return request;
    }

    private ErrorResponse buildErrorResponse(String requestId, String message) {

        var error = new ErrorResponse();
        error.setRequestId(requestId);
        error.setError(message);

        return error;
    }

    private ErrorResponse buildSerializedErrorResponse(String requestId, String serializedObject) {

        return Try.of(() -> objectMapper.readValue(serializedObject, ErrorResponse.class))
                .getOrElse(buildErrorResponse(requestId, "can't process request"));
    }

    private void measureCaptureApiResponseCode(int responseCode) {

        var statusCodeValue = String.valueOf(responseCode);
        meterRegistry.counter("capture_api_response",
                Collections.singletonList(Tag.of("status_code", statusCodeValue))).increment();
    }
}
