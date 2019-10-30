package net.cavitos.wcapture.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.vavr.control.Either;
import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.client.model.CaptureResponse;
import net.cavitos.wcapture.client.model.ErrorResponse;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureRepository;

@Service
public class CaptureService {

    private static final Logger logger = LoggerFactory.getLogger(CaptureService.class);

    private final CaptureRepository captureRepository;
    private final CaptureApiClient captureApiClient;

    public CaptureService(final CaptureRepository captureRepository,
                          final CaptureApiClient captureApiClient) {
        this.captureRepository = captureRepository;
        this.captureApiClient = captureApiClient;
    }

    public Either<String, Capture> captureUrl(final String requestId, final String url) {

        try {

            var result = captureApiClient.captureUrl(requestId, url);
            
            CaptureHistory captureHistory = result.isRight() ? CaptureHistoryFactory.fromCaptureResponse(result.get())
                : CaptureHistoryFactory.fromCaptureError(url, result.getLeft());
               
            captureRepository.save(captureHistory);
            logger.info("storing capture_history={}", captureHistory);

            return result
                .map(this::buildCapture)
                .mapLeft(ErrorResponse::getError);
    
        } catch (Exception ex) {

            logger.error("can't capture url={}, requestId={} - ", url, requestId, ex);            
            return Either.left("can't captuer url: " + url);
        }
    }

    private Capture buildCapture(CaptureResponse captureResponse) {

        return Capture.builder()
            .captureId(captureResponse.getRequestId())
            .storedPath(captureResponse.getStoredPath())
            .build();
    }
}
