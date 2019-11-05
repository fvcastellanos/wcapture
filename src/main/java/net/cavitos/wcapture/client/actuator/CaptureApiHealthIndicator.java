package net.cavitos.wcapture.client.actuator;

import net.cavitos.wcapture.client.CaptureApiClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

public class CaptureApiHealthIndicator extends AbstractHealthIndicator {

    private static final String DOWN_STATUS = "DOWN";

    private final CaptureApiClient captureApiClient;

    public CaptureApiHealthIndicator(final CaptureApiClient captureApiClient) {

        this.captureApiClient = captureApiClient;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        var healthResult = captureApiClient.getHealth();

        if (healthResult.isSuccess()) {

            var result = healthResult.get();

            builder.up();

            if (DOWN_STATUS.equalsIgnoreCase(result.getStatus()) ||
                    DOWN_STATUS.equalsIgnoreCase(result.getStorageStatus())) {

                builder.down();

            }

            builder.withDetail("capture-api", result.getStatus())
                    .withDetail("image-storage", result.getStorageStatus())
                    .build();

            return;
        }

        builder.down()
                .withDetail("issue", healthResult.getCause())
                .build();
    }
}
