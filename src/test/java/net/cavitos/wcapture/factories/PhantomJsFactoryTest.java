package net.cavitos.wcapture.factories;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class PhantomJsFactoryTest {

    private PhantomJsFactory phantomJsFactory;

    @Before
    public void setUp() {
        initMocks(this);
        phantomJsFactory = new PhantomJsFactory("http://127.0.0.1:8910",
                                                System.getProperty("java.io.tmpdir"));
    }

    @Test
    public void testCreateWebDriver() throws MalformedURLException {
        final WebDriver webDriver = phantomJsFactory.createWebDriver();

        assertThat(webDriver, is(not(nullValue())));
    }

    @Test
    public void testTakeScreenshot() throws IOException {
        final WebDriver webDriver = phantomJsFactory.createWebDriver();
        phantomJsFactory.takeScreenshot(webDriver, "");
    }

    @Test
    public void testGetScreenshot() throws FileNotFoundException {
        phantomJsFactory.getScreenshot("");
    }

}
