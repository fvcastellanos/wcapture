package net.cavitos.wcapture.services;

import net.cavitos.wcapture.client.PhantomJsClient;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CaptureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptureService.class);

    private final CaptureHistoryRepository captureRepository;
    private final PhantomJsClient phantomJsClient;

    public CaptureService(final CaptureHistoryRepository captureRepository,
                          final PhantomJsClient phantomJsClient) {
        this.captureRepository = captureRepository;
        this.phantomJsClient = phantomJsClient;
    }

    public Optional<Capture> captureUrl(final String url) {
        try {
            final var webDriver = phantomJsClient.createWebDriver();
            webDriver.get(url);

            final var captureId = UUID.randomUUID().toString();

            phantomJsClient.takeScreenshot(webDriver, captureId);

            final var captureHistory = buildCaptureHistory(captureId, url);

            captureRepository.insert(captureHistory);

            webDriver.close();

            return Optional.of(new Capture(captureId));

        } catch (final Exception exception) {
            LOGGER.error("Can't capture with url={}", url, exception);
            return Optional.empty();
        }
    }

    public InputStream getCapturedUrl(final String captureId) throws FileNotFoundException {
        return phantomJsClient.getScreenshot(captureId);
    }

    public List<CaptureHistory> getCaptureHistories() {
        return captureRepository.findAll();
    }

    private CaptureHistory buildCaptureHistory(String captureId, String url) {

        return CaptureHistory.builder()
                .filename(captureId)
                .url(url)
                .build();
    }
}
