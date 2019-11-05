package net.cavitos.wcapture.client.actuator;

import io.vavr.control.Try;
import net.cavitos.wcapture.client.CaptureApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.client.HttpClientErrorException;

import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildDownHealthResponse;
import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildUpHealthResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaptureApiHealthIndicatorTest {

    @Mock
    private CaptureApiClient captureApiClient;

    private CaptureApiHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {

        healthIndicator = new CaptureApiHealthIndicator(captureApiClient);
    }

    @Test
    void testHealthCheck() {

        when(captureApiClient.getHealth())
                .thenReturn(Try.success(buildUpHealthResponse()));

        var health = healthIndicator.getHealth(true);

        assertThat(health.getStatus()).isEqualTo(Status.UP);
    }

    @Test
    void testHealthCheckOneServiceDownFails() {

        var result = buildUpHealthResponse();
        result.setStorageStatus("DOWN");

        when(captureApiClient.getHealth())
                .thenReturn(Try.success(result));

        var health = healthIndicator.getHealth(true);

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void testHealthCheckFails() {

        when(captureApiClient.getHealth())
                .thenReturn(Try.success(buildDownHealthResponse()));

        var health = healthIndicator.getHealth(true);

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void testHealthCheckThrowsException() {

        when(captureApiClient.getHealth())
                .thenThrow(new RuntimeException("test exception"));

        var health = healthIndicator.getHealth(true);

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }

}
