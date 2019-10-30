package net.cavitos.wcapture.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.client.model.CaptureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class CaptureApiDefaultClientTest {

    private MockRestServiceServer mockRestServiceServer;

    private CaptureApiClient captureApiClient;

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String CAPTURE_API_URL = "https://capture-api.com/capture/v1";

    @BeforeEach
    void setUp() {

        var restTemplate = new RestTemplate();

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        captureApiClient = new CaptureApiDefaultClient(objectMapper, restTemplate, CAPTURE_API_URL);
    }

    @Test
    void blah() throws Exception {

        var requestId = UUID.randomUUID().toString();
        var url = "https://gog.com";

        var content = objectMapper.writeValueAsString(buildCaptureRequest(requestId, url));
        var responseBody = objectMapper.writeValueAsString(buildCaptureResponse(requestId, url));

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


    }

    private CaptureRequest buildCaptureRequest(String requestId, String url) {

        var request = new CaptureRequest();
        request.setUrl(url);
        request.setRequestId(requestId);

        return request;
    }

    private CaptureResponse buildCaptureResponse(String requestId, String url) {

        var response = new CaptureResponse();
        response.setRequestId(requestId);
        response.setTargetUrl(url);
        response.setStoredPath("https://cdn.net/image.jpg");

        return response;
    }

}
