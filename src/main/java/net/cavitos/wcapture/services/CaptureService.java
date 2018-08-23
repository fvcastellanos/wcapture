package net.cavitos.wcapture.services;

import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.Platform.UNIX;
import static org.openqa.selenium.remote.BrowserType.PHANTOMJS;

@Service
public class CaptureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptureService.class);

    private final CaptureRepository captureRepository;
    private final String webDriverURL;
    private final String imageStorePath;

    public CaptureService(final CaptureRepository captureRepository,
                          @Value("${web.driver.url:http://127.0.0.1:8910}") final String webDriverURL,
                          @Value("${image.store.path:/opt/wcapture/captures}") final String imageStorePath) {
        this.captureRepository = captureRepository;
        this.webDriverURL = webDriverURL;
        this.imageStorePath = imageStorePath;
    }

    public Optional<Capture> captureUrl(final String url) {
        try {
            final DesiredCapabilities desiredCapabilities = new DesiredCapabilities(PHANTOMJS, "2.1.1", UNIX);

            final WebDriver webDriver = new RemoteWebDriver(new URL(webDriverURL), desiredCapabilities);
            webDriver.manage().window().setSize(new Dimension(1360, 780));
            webDriver.get(url);

            final String captureId = UUID.randomUUID().toString();
            takeScreenshot(webDriver, captureId);

            final CaptureHistory captureHistory = CaptureHistory
                    .builder()
                    .filename(captureId)
                    .url(url)
                    .build();

            captureRepository.insert(captureHistory);

            webDriver.close();

            return Optional.of(new Capture(captureId));

        } catch (final Exception exception) {
            LOGGER.error("Can't capture with url={}", url, exception);
            return Optional.empty();
        }
    }

    public InputStream getImageFile(final String captureId) throws FileNotFoundException {
        final String name = buildFilename(captureId);
        return new FileInputStream(name);
    }

    private String buildFilename(final String captureId) {
        return imageStorePath + "/" + captureId + ".png";
    }

    private void takeScreenshot(final WebDriver webDriver, final String captureId) throws IOException {
        final String filename = buildFilename(captureId);

        final WebDriver superDriver = new Augmenter().augment(webDriver);
        final byte[] image = ((TakesScreenshot) superDriver).getScreenshotAs(BYTES);

        try (final OutputStream fileOutputStream = new FileOutputStream(filename)) {
            fileOutputStream.write(image);
        }
    }

}
