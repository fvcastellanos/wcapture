package net.cavitos.wcapture.services;

import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;
import net.cavitos.wcapture.domain.CaptureHistory;

public class CaptureHistoryFactory {

    private CaptureHistoryFactory() {
    }

    public static CaptureHistory fromCaptureResponse(CaptureResponse captureResponse) {

        return CaptureHistory.builder()
            .result("OK")
            .requestId(captureResponse.getRequestId())
            .storedPath(captureResponse.getStoredPath())
            .url(captureResponse.getTargetUrl())
            .build();
    }

    public static CaptureHistory fromCaptureError(String url, ErrorResponse errorResponse) {

        return CaptureHistory.builder()
            .result("ERROR")
            .url(url)
            .requestId(errorResponse.getRequestId())
            .error(errorResponse.getError())
            .build();
    }
}