package net.cavitos.wcapture.factories;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.Platform.UNIX;
import static org.openqa.selenium.remote.BrowserType.PHANTOMJS;

@Component
public class PhantomJsFactory {

    private final String webDriverURL;
    private final String imageStorePath;

    public PhantomJsFactory(@Value("${web.driver.url:http://127.0.0.1:8910}") final String webDriverURL,
                            @Value("${image.store.path:/opt/wcapture/captures}") final String imageStorePath) {
        this.webDriverURL = webDriverURL;
        this.imageStorePath = imageStorePath;
    }

    public WebDriver createWebDriver() throws MalformedURLException {
        final DesiredCapabilities desiredCapabilities = new DesiredCapabilities(PHANTOMJS, "2.1.1", UNIX);
        final WebDriver webDriver = new RemoteWebDriver(new URL(webDriverURL), desiredCapabilities);
        webDriver.manage().window().setSize(new Dimension(1360, 780));

        return webDriver;

    }

    public void takeScreenshot(final WebDriver webDriver, final String captureId) throws IOException {
        final String filename = buildFilename(captureId);

        final WebDriver superDriver = new Augmenter().augment(webDriver);
        final byte[] image = ((TakesScreenshot) superDriver).getScreenshotAs(BYTES);

        try (final OutputStream fileOutputStream = new FileOutputStream(filename)) {
            fileOutputStream.write(image);
        }
    }

    public InputStream getScreenshot(final String captureId) throws FileNotFoundException {
        final String name = buildFilename(captureId);
        return new FileInputStream(name);
    }

    private String buildFilename(final String captureId) {
        return imageStorePath + "/" + captureId + ".png";
    }

}
