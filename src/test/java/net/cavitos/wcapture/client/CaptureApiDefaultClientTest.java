package net.cavitos.wcapture.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import net.cavitos.wcapture.client.model.CaptureRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildCaptureResponse;
import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildDownHealthResponse;
import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildErrorResponse;
import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildUpHealthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(MockitoExtension.class)
public class CaptureApiDefaultClientTest {

    private static final String CAPTURE_API_URL = "https://capture-api.com/capture/v1";
    private static final String CAPTURE_API_HEALTH_URL = "https://capture-api.com/capture/v1/health";
    @Mock
    private Counter counter;

    @Mock
    private MeterRegistry meterRegistry;

    private MockRestServiceServer mockRestServiceServer;

    private CaptureApiClient captureApiClient;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static String requestId = UUID.randomUUID().toString();
    private static String url = "https://gog.com";

    @BeforeEach
    void setUp() {

        var restTemplate = new RestTemplate();

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        captureApiClient = new CaptureApiDefaultClient(objectMapper, restTemplate, CAPTURE_API_URL, CAPTURE_API_HEALTH_URL, meterRegistry);
    }

    @AfterEach
    void tearDown() {

        verifyNoMoreInteractions(meterRegistry, counter);
    }

    @Test
    void testSuccessCaptureRequest() throws Exception {

        expectMetricRecorded();

        var captureResponse = buildCaptureResponse(requestId, url);
        var content = objectMapper.writeValueAsString(buildCaptureRequest(requestId, url));
        var responseBody = objectMapper.writeValueAsString(captureResponse);

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(CAPTURE_API_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(content, false))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseBody)
                );

        var result = captureApiClient.captureUrl(requestId, url);

        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isEqualTo(captureResponse);
    }

    @Test
    void testErrorResponse() throws Exception {

        var errorMessage = String.format("can't process capture request for url=%s, requestId=%s", url, requestId);

        var errorResponse = buildErrorResponse(requestId, errorMessage);
        var content = objectMapper.writeValueAsString(buildCaptureRequest(requestId, url));
        var responseBody = objectMapper.writeValueAsString(errorResponse);

        expectMetricRecorded();

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(CAPTURE_API_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(content, false))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseBody));

        var result = captureApiClient.captureUrl(requestId, url);

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(errorResponse);
    }

    @Test
    void testHealthCheck() throws Exception {

        var healthResponse = buildUpHealthResponse();
        var responseBody = objectMapper.writeValueAsString(healthResponse);
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(CAPTURE_API_HEALTH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseBody));

        var result = captureApiClient.getHealth();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(healthResponse);
    }

    @Test
    void testFailHealthCheck() throws Exception {

        var healthResponse = buildDownHealthResponse();
        var responseBody = objectMapper.writeValueAsString(healthResponse);
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(CAPTURE_API_HEALTH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseBody));

        var result = captureApiClient.getHealth();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(healthResponse);
    }

    @Test
    void testHealthCheckThrowsException() {

        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(CAPTURE_API_HEALTH_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        var result = captureApiClient.getHealth();
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(HttpServerErrorException.InternalServerError.class);
    }

    // ----------------------------------------------------------------------------

    private CaptureRequest buildCaptureRequest(String requestId, String url) {

        var request = new CaptureRequest();
        request.setUrl(url);
        request.setRequestId(requestId);

        return request;
    }

    private void expectMetricRecorded() {

        doNothing().when(counter)
                .increment();

        when(meterRegistry.counter(anyString(), any(Iterable.class)))
            .thenReturn(counter);
    }
}
