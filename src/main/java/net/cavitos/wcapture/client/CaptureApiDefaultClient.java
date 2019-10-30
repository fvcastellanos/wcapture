package net.cavitos.wcapture.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestOperations;

import io.vavr.control.Either;
import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;

public class CaptureApiDefaultClient implements CaptureApiClient {

    private static final Logger logger = LoggerFactory.getLogger(CaptureApiDefaultClient.class);

    private final ObjectMapper objectMapper;
    private final RestOperations restOperations;
    private final String captureApiUrl;

    public CaptureApiDefaultClient(final ObjectMapper objectMapper, final RestOperations restOperations,
                                   String captureApiUrl) {

        this.captureApiUrl = captureApiUrl;
        this.objectMapper = objectMapper;
        this.restOperations = restOperations;
    }

    @Override
    public Either<ErrorResponse, CaptureResponse> captureUrl(String requestId, String url) {

        try {

            logger.info("capture request for url={}, requestId={}", url, requestId);

            var request = buildRequest(requestId, url);
            var responseEntity = restOperations.postForEntity(captureApiUrl, request, String.class);

            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {

                return Either.right(objectMapper.readValue(responseEntity.getBody(), CaptureResponse.class));
            }

            return Either.left(objectMapper.readValue(responseEntity.getBody(), ErrorResponse.class));

        } catch(Exception ex) {

            logger.error("can't process capture request for url={}, requestId={} - ", url, requestId, ex);

            var message = String.format("can't process capture request for url=%s, requestId=%s", url, requestId);
            return Either.left(buildErrorResponse(requestId, message));
        }
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
}
