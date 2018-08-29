package net.cavitos.wcapture.services;


import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.factories.PhantomJsFactory;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureHistoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
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
    private CaptureHistoryRepository captureHistoryRepository;

    @Mock
    private PhantomJsFactory phantomJsFactory;

    @Mock
    private WebDriver webDriver;

    @Mock
    private InputStream inputStream;

    private CaptureService captureService;

    @Before
    public void setUp() {
        initMocks(this);
        captureService = new CaptureService(captureHistoryRepository, phantomJsFactory);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(captureHistoryRepository, phantomJsFactory);
    }

    @Test
    public void testCaptureUrl() throws IOException {
        when(phantomJsFactory.createWebDriver()).thenReturn(webDriver);

        final Optional<Capture> captureHolder = captureService.captureUrl("http://www.google.com");

        assertThat(captureHolder.isPresent(), is(true));

        final Capture capture = captureHolder.get();

        assertThat(capture.getCaptureId(), is(not(nullValue())));

        verify(captureHistoryRepository).insert(any(CaptureHistory.class));
        verify(phantomJsFactory).createWebDriver();
        verify(phantomJsFactory).takeScreenshot(eq(webDriver), anyString());
    }

    @Test
    public void testCaptureUrl_Exception() throws MalformedURLException {
        when(phantomJsFactory.createWebDriver()).thenReturn(null);

        final Optional<Capture> captureHolder = captureService.captureUrl("http://www.google.com");

        assertThat(captureHolder.isPresent(), is(false));

        verify(phantomJsFactory).createWebDriver();
    }

    @Test
    public void testGetCapturedUrl() throws FileNotFoundException {
        final String captureId = "captureId";

        when(phantomJsFactory.getScreenshot(captureId)).thenReturn(inputStream);

        final InputStream inputStreamCapturedUrl = captureService.getCapturedUrl("captureId");

        assertThat(inputStreamCapturedUrl, is(inputStream));

        verify(phantomJsFactory).getScreenshot(captureId);
    }

    @Test
    public void testGetCaptureHistory() {
        final CaptureHistory captureHistory = CaptureHistory
                .builder()
                .filename("FILENAME-A")
                .url("http://www.fake.com")
                .build();

        when(captureHistoryRepository.findAll()).thenReturn(singletonList(captureHistory));

        final List<CaptureHistory> captureHistories = captureService.getCaptureHistories();

        assertThat(captureHistories.size(), is(1));
        assertThat(captureHistories.get(0), is(captureHistory));

        verify(captureHistoryRepository).findAll();
    }

}
