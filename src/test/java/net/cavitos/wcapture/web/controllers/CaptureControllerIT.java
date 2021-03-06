package net.cavitos.wcapture.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cavitos.wcapture.fixture.CaptureApiClientFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Transactional
@SpringBootTest
@WebAppConfiguration
@TestPropertySource({"classpath:application.properties", "classpath:application-test.properties"})
class CaptureControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${capture.api.url}")
    private String captureApiUrl;

    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);

        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetCaptureMainView() throws Exception {
        mockMvc()
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @Test
    void testPostCaptureUrl() throws Exception {

        var requestId = UUID.randomUUID().toString();
        var url = "https://gog.com";

        expectSuccessCapture(requestId, url);

        var params = new LinkedMultiValueMap<String, String>();
        params.add("url", url);
        params.add("requestId", requestId);

        mockMvc().perform(post("/")
                .contentType(APPLICATION_FORM_URLENCODED)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("requestId", is(requestId)));
    }

    @Test
    void testPostCapture_InvalidUrl() throws Exception {
        mockMvc()
                .perform(post("/")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("url", "asdf1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("error", is("Please provide a valid URL")));
    }

    @Test
    void testGetCaptures() throws Exception {

        mockMvc().perform(get("/captures"))
                .andExpect(status().isOk())
                .andExpect(view().name("captures"))
                .andExpect(model().attribute("captures", is(not(nullValue()))));
    }

    // ------------------------------------------------------------------------------------------------------

    private MockMvc mockMvc() {
        return webAppContextSetup(webApplicationContext)
                .build();
    }

    private void expectSuccessCapture(String requestId, String url) throws Exception {

        var request = CaptureApiClientFixture.buildCaptureRequest(requestId, url);
        var response = CaptureApiClientFixture.buildCaptureResponse(requestId, url);

        mockRestServiceServer.expect(requestTo(captureApiUrl))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(request), false))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(response)));
    }

}
