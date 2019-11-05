package net.cavitos.wcapture.fixture;

import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;
import net.cavitos.wcapture.client.model.HealthResponse;

public class CaptureApiClientFixture {

    private CaptureApiClientFixture() {
    }

    public static CaptureResponse buildCaptureResponse(String requestId, String url) {

        var response = new CaptureResponse();
        response.setRequestId(requestId);
        response.setTargetUrl(url);
        response.setStoredPath("https://cdn.net/image.jpg");

        return response;
    }

    public static ErrorResponse buildErrorResponse(String requestId, String message) {

        var error = new ErrorResponse();
        error.setRequestId(requestId);
        error.setError(message);

        return error;
    }

    public static CaptureRequest buildCaptureRequest(String requestId, String url) {

        var request = new CaptureRequest();
        request.setUrl(url);
        request.setRequestId(requestId);

        return request;
    }

    public static HealthResponse buildUpHealthResponse() {

        var health = new HealthResponse();
        health.setStatus("UP");
        health.setStorageStatus("UP");

        return health;
    }

    public static HealthResponse buildDownHealthResponse() {

        var health = new HealthResponse();
        health.setStatus("DOWN");
        health.setStorageStatus("DOWN");

        return health;
    }

}
