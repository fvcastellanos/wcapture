package net.cavitos.phantom.services;

import net.cavitos.phantom.domain.Capture;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Named
public class CaptureService {

    @Value("${web.driver.url:http://127.0.0.1:8910}")
    private String webDriverURL;

    @Value("${image.store.path:/opt/wcapture/captures}")
    private String imageStorePath;

    private Logger logger = LoggerFactory.getLogger(CaptureService.class);

    public Optional<Capture> captureURL(String url) {

        try {
            logger.info("capturing URL: {}", url);
            DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.PHANTOMJS, "2.1.1", Platform.EL_CAPITAN);

            WebDriver webDriver = new RemoteWebDriver(new URL(webDriverURL), capabilities);
            webDriver.manage().window().setSize(new Dimension(1360, 780));

            logger.info("sending the get request to the web driver");            
            webDriver.get(url);

            String captureId = UUID.randomUUID().toString();
            String fileName = takeScreenShot(webDriver, captureId);
            String title = webDriver.getTitle();
            webDriver.close();

            logger.info("closing driver connection...");
            return Optional.of(new Capture(title, captureId, fileName));

        } catch (Exception ex) {
            logger.error("can't capture URL: {} - ", url, ex);
            return Optional.empty();
        }
    }

    public InputStream getImageFile(String captureId) throws Exception {
        String name = buildFileName(captureId);
        return new FileInputStream(name);
    }

    // ----------------------------------

    private String buildFileName(String captureId) {
        return imageStorePath + "/" + captureId + ".png";
    }

    private String takeScreenShot(WebDriver webDriver, String captureId) throws Exception {
        String title = webDriver.getTitle();
        String fileName = buildFileName(captureId);

        logger.info("generating image of url: {}", webDriver.getCurrentUrl());
        WebDriver superDriver = new Augmenter().augment(webDriver);
        byte[] image = ((TakesScreenshot)superDriver).getScreenshotAs(OutputType.BYTES);
        OutputStream fileOutputStream = new FileOutputStream(fileName);
        fileOutputStream.write(image);
        fileOutputStream.close();
        logger.info("image: {} of site: {} created", fileName, title);    

        return fileName;
    }
}