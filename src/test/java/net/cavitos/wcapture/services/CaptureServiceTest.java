package net.cavitos.wcapture.services;


import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.byteThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class CaptureServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private CaptureRepository captureRepository;

    @Mock
    private FileOutputStream fileOutputStream;

    private CaptureService captureService;

    @Before
    public void setUp() {
        initMocks(this);
        captureService = spy(new CaptureService(captureRepository,
                                            "http://127.0.0.1:8910",
                                            "/opt/wcapture/captures"));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(captureRepository);
    }

    @Test
    public void testCaptureUrl() {
        final String url = "http://www.google.com";

        final Optional<Capture> captureHolder = captureService.captureUrl(url);

        assertThat(captureHolder.isPresent(), is(true));

        final Capture capture = captureHolder.get();

        assertThat(capture.getCaptureId(), is(not(nullValue())));

        verify(captureRepository).insert(any(CaptureHistory.class));
    }

    @Test
    public void testCaptureUrl_Exception() throws IOException {

        doThrow(new IOException()).when(fileOutputStream).write(any(byte.class));


        final String url = "http://www.google.com";

//        expectedException.expect(Exception.class);
//        expectedException.expectMessage("Can't capture with URL={}" + url);


        final Optional<Capture> captureHolder = captureService.captureUrl(url);

//        assertThat(captureHolder.isPresent(), is(true));
//
//        final Capture capture = captureHolder.get();
//
//        assertThat(capture.getCaptureId(), is(not(nullValue())));
//
//        verify(captureRepository).insert(any(CaptureHistory.class));
    }

}
