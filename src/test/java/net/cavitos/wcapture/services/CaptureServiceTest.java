package net.cavitos.wcapture.services;

import net.cavitos.wcapture.client.PhantomJsClient;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptureServiceTest {

    @Mock
    private CaptureRepository captureRepository;

    @Mock
    private PhantomJsClient phantomJsClient;

    @Mock
    private WebDriver webDriver;

    @Mock
    private InputStream inputStream;

    private CaptureService captureService;

    @BeforeEach
    void setUp() {
        captureService = new CaptureService(captureRepository, phantomJsClient);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(captureRepository, phantomJsClient);
    }

    @Test
    void testCaptureUrl() throws IOException {
        when(phantomJsClient.createWebDriver()).thenReturn(webDriver);

        final var captureHolder = captureService.captureUrl("http://www.google.com");

        assertThat(captureHolder.isPresent()).isTrue();

        final var capture = captureHolder.get();

        assertThat(capture.getCaptureId()).isNotNull();

        verify(captureRepository).save(any(CaptureHistory.class));
        verify(phantomJsClient).createWebDriver();
        verify(phantomJsClient).takeScreenshot(eq(webDriver), anyString());
    }

    @Test
    void testCaptureUrl_Exception() throws MalformedURLException {
        when(phantomJsClient.createWebDriver()).thenReturn(null);

        final var captureHolder = captureService.captureUrl("http://www.google.com");

        assertThat(captureHolder.isPresent()).isFalse();

        verify(phantomJsClient).createWebDriver();
    }

    @Test
    void testGetCapturedUrl() throws FileNotFoundException {
        final var captureId = "captureId";

        when(phantomJsClient.getScreenshot(captureId)).thenReturn(inputStream);

        final InputStream inputStreamCapturedUrl = captureService.getCapturedUrl("captureId");

        assertThat(inputStreamCapturedUrl).isEqualTo(inputStream);

        verify(phantomJsClient).getScreenshot(captureId);
    }

}
