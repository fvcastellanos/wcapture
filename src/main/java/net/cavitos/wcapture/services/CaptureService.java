package net.cavitos.wcapture.services;

import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.factories.PhantomJsFactory;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureHistoryRepository;
import org.openqa.selenium.WebDriver;
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

    private final CaptureHistoryRepository captureHistoryRepository;
    private final PhantomJsFactory phantomJsFactory;

    public CaptureService(final CaptureHistoryRepository captureHistoryRepository,
                          final PhantomJsFactory phantomJsFactory) {
        this.captureHistoryRepository = captureHistoryRepository;
        this.phantomJsFactory = phantomJsFactory;
    }

    public Optional<Capture> captureUrl(final String url) {
        try {
            final WebDriver webDriver = phantomJsFactory.createWebDriver();
            webDriver.get(url);

            final String captureId = UUID.randomUUID().toString();

            phantomJsFactory.takeScreenshot(webDriver, captureId);

            final CaptureHistory captureHistory = CaptureHistory
                    .builder()
                    .filename(captureId)
                    .url(url)
                    .build();

            captureHistoryRepository.insert(captureHistory);

            webDriver.close();

            return Optional.of(new Capture(captureId));

        } catch (final Exception exception) {
            LOGGER.error("Can't capture with url={}", url, exception);
            return Optional.empty();
        }
    }

    public InputStream getCapturedUrl(final String captureId) throws FileNotFoundException {
        return phantomJsFactory.getScreenshot(captureId);
    }

    public List<CaptureHistory> getCaptureHistories() {
        return captureHistoryRepository.findAll();
    }

}
