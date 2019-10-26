package net.cavitos.wcapture.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PhantomJsClientTest {

    private PhantomJsClient phantomJsClient;

    @BeforeEach
    void setUp() {
        phantomJsClient = new PhantomJsClient("http://127.0.0.1:8910",
                                              System.getProperty("java.io.tmpdir"));
    }

    @Test
    void testCreateWebDriver() throws MalformedURLException {
        final var webDriver = phantomJsClient.createWebDriver();

        assertThat(webDriver).isNotNull();
    }

    @Test
    void testTakeScreenshot() throws IOException {
        final var webDriver = phantomJsClient.createWebDriver();
        phantomJsClient.takeScreenshot(webDriver, "");
    }

    @Test
    void testGetScreenshot() throws FileNotFoundException {
        phantomJsClient.getScreenshot("");
    }

}
