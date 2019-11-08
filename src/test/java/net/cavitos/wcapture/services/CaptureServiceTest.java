package net.cavitos.wcapture.services;

import io.vavr.control.Either;
import net.cavitos.wcapture.client.CaptureApiClient;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildCaptureResponse;
import static net.cavitos.wcapture.fixture.CaptureApiClientFixture.buildErrorResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptureServiceTest {

/*
    private static final String REQUEST_ID = "1234";
    private static final String URL = "https://gog.com";

    @Mock
    private CaptureRepository captureRepository;

    @Mock
    private CaptureApiClient captureApiClient;

    private CaptureService captureService;

    @BeforeEach
    void setUp() {
        captureService = new CaptureService(captureRepository, captureApiClient);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(captureRepository, captureApiClient);
    }

    @Test
    void testCaptureUrl() {

        when(captureApiClient.captureUrl(REQUEST_ID, URL))
            .thenReturn(Either.right(buildCaptureResponse(REQUEST_ID, URL)));

        when(captureRepository.save(any(CaptureHistory.class)))
                .thenReturn(new CaptureHistory());

        final var result = captureService.captureUrl(REQUEST_ID, URL);

        assertThat(result.isRight()).isTrue();

        final var capture = result.get();

        assertThat(capture.getCaptureId()).isNotNull();

        verify(captureRepository).save(any(CaptureHistory.class));
        verify(captureApiClient).captureUrl(REQUEST_ID, URL);
    }

    @Test
    void testCaptureUrlReturnError() {

        when(captureApiClient.captureUrl(REQUEST_ID, URL))
                .thenReturn(Either.left(buildErrorResponse(REQUEST_ID, "something went wrong")));

        final var result = captureService.captureUrl(REQUEST_ID, URL);

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo("something went wrong");

        verify(captureRepository).save(any(CaptureHistory.class));
        verify(captureApiClient).captureUrl(REQUEST_ID, URL);
    }

    @Test
    void testCaptureUrlThrowsException() {

        when(captureApiClient.captureUrl(REQUEST_ID, URL))
                .thenReturn(Either.right(buildCaptureResponse(REQUEST_ID, URL)));

        when(captureRepository.save(any(CaptureHistory.class)))
                .thenThrow(new RuntimeException("expected exception"));

        final var result = captureService.captureUrl(REQUEST_ID, URL);

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(String.format("can't capture url: %s", URL));

        verify(captureRepository).save(any(CaptureHistory.class));
    }
*/
}
