package net.cavitos.wcapture.services;


import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.client.PhantomJsClient;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CaptureServiceTest {

    @Mock
    private CaptureRepository captureRepository;

    @Mock
    private PhantomJsClient phantomJsClient;

    @Mock
    private WebDriver webDriver;

    @Mock
    private InputStream inputStream;

    private CaptureService captureService;

    @Before
    public void setUp() {
        initMocks(this);
        captureService = new CaptureService(captureRepository, phantomJsClient);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(captureRepository, phantomJsClient);
    }

    @Test
    public void testCaptureUrl() throws IOException {
        when(phantomJsClient.createWebDriver()).thenReturn(webDriver);

        final var captureHolder = captureService.captureUrl("http://www.google.com");

        assertThat(captureHolder.isPresent(), is(true));

        final var capture = captureHolder.get();

        assertThat(capture.getCaptureId(), is(not(nullValue())));

        verify(captureRepository).insert(any(CaptureHistory.class));
        verify(phantomJsClient).createWebDriver();
        verify(phantomJsClient).takeScreenshot(eq(webDriver), anyString());
    }

    @Test
    public void testCaptureUrl_Exception() throws MalformedURLException {
        when(phantomJsClient.createWebDriver()).thenReturn(null);

        final var captureHolder = captureService.captureUrl("http://www.google.com");

        assertThat(captureHolder.isPresent(), is(false));

        verify(phantomJsClient).createWebDriver();
    }

    @Test
    public void testGetCapturedUrl() throws FileNotFoundException {
        final var captureId = "captureId";

        when(phantomJsClient.getScreenshot(captureId)).thenReturn(inputStream);

        final InputStream inputStreamCapturedUrl = captureService.getCapturedUrl("captureId");

        assertThat(inputStreamCapturedUrl, is(inputStream));

        verify(phantomJsClient).getScreenshot(captureId);
    }
    
}
