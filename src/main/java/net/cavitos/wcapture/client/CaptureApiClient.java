package net.cavitos.wcapture.client;

import io.vavr.control.Either;
import io.vavr.control.Try;
import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;
import net.cavitos.wcapture.client.model.HealthResponse;

public interface CaptureApiClient {

    Either<ErrorResponse, CaptureResponse> captureUrl(String requestId, String url);
    Try<HealthResponse> getHealth();
}
