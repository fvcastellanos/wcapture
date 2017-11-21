package net.cavitos.phantom;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileOutputStream;
import java.net.URL;

public class CaptureTest {

    public static void main(String [] args) {
        try {

            DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.PHANTOMJS, "2.1.1", Platform.EL_CAPITAN);
//            capabilities.setCapability("browserConnectionEnabled", true);

//            WebDriver webDriver = new PhantomJSDriver(capabilities);
            WebDriver webDriver = new RemoteWebDriver(new URL("http://127.0.0.1:8910"), capabilities);
            webDriver.manage().window().setSize(new Dimension(1920, 1080));

            webDriver.get("https://github.com/");
            String title = webDriver.getTitle();

//            PhantomJSDriver superDriver = (PhantomJSDriver) webDriver;

//            byte[] image = superDriver.getScreenshotAs(OutputType.BYTES);
//            ImageOutputStream os = new FileImageOutputStream(new File("~/cavitos-net.png"));
//            os.write(image);
//            os.close();

            WebDriver superDriver = new Augmenter().augment(webDriver);

            byte[] image = ((TakesScreenshot)superDriver).getScreenshotAs(OutputType.BYTES);
            FileOutputStream fileOutputStream = new FileOutputStream("/Users/fcastellanos/Desktop/cavitos.png");
            fileOutputStream.write(image);
            fileOutputStream.close();

            webDriver.close();

            System.out.println(title);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
