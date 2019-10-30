package net.cavitos.wcapture.client;

import io.vavr.control.Either;
import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;

public interface CaptureApiClient {

    Either<ErrorResponse, CaptureResponse> captureUrl(String requestId, String url);
}
