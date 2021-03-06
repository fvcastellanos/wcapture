package net.cavitos.wcapture.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.client.CaptureApiDefaultClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiClientConfiguration {

    @Value("${capture.api.url}")
    private String captureApiUrl;

    @Value("${capture.api.health.url}")
    private String healthApiUrl;

    @Bean
    public RestOperations restOperations() {

        return new RestTemplate();
    }

    @Bean
    public CaptureApiClient captureApiClient(ObjectMapper objectMapper,
                                             RestOperations restOperations,
                                             MeterRegistry meterRegistry) {

        return new CaptureApiDefaultClient(objectMapper, restOperations, captureApiUrl, healthApiUrl, meterRegistry);
    }

}