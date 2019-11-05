package net.cavitos.wcapture.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry;
import net.cavitos.wcapture.client.actuator.CaptureApiHealthIndicator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.client.CaptureApiDefaultClient;

@Configuration
public class ApiClientConfiguration {

    @Value("${capture.api.url:https://apps.cavitos.net/capture/v1/}")
    private String captureApiUrl;

    @Value("${capture.api.health.url:https://apps.cavitos.net/capture/v1/health}")
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

    @Bean
    public CaptureApiHealthIndicator captureApiHealthIndicator(CaptureApiClient captureApiClient) {

        return new CaptureApiHealthIndicator(captureApiClient);
    }

}