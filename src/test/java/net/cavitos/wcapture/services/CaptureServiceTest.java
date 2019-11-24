package net.cavitos.wcapture.services;

import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.fixture.CaptureRepositoryFixture;
import net.cavitos.wcapture.message.producer.CaptureRequestProducer;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptureServiceTest {

    private static final String REQUEST_ID = "1234";
    private static final String URL = "https://gog.com";

    @Mock
    private CaptureRepository captureRepository;

    @Mock
    private CaptureRequestProducer captureRequestProducer;

    private CaptureService captureService;

    @BeforeEach
    void setUp() {
        captureService = new CaptureService(captureRequestProducer, captureRepository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(captureRepository, captureRequestProducer);
    }

    @Test
    void testCaptureUrl() {

        doNothing().when(captureRequestProducer)
                .produce(any(CaptureRequest.class));

//        when(captureRepository.save(any(CaptureHistory.class)))
//                .thenReturn(new CaptureHistory());

        captureService.captureUrl(REQUEST_ID, URL);


//        verify(captureRepository).save(any(CaptureHistory.class));
        verify(captureRequestProducer).produce(any(CaptureRequest.class));
//        verify(captureApiClient).captureUrl(REQUEST_ID, URL);
    }

    @Test
    void testGetCaptureHistory() {

        var captureHistory = CaptureRepositoryFixture.buildCaptureHistory();

        Page<CaptureHistory> page = new PageImpl<>(Collections.singletonList(captureHistory));
        when(captureRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        var captures = captureService.getCaptureHistory();

        assertThat(captures.toArray()).contains(captureHistory);

        verify(captureRepository).findAll(any(Pageable.class));
    }

    @Test
    void testGetEmptyCaptureHistory() {

        when(captureRepository.findAll(any(Pageable.class)))
                .thenReturn(Page.empty());

        var captures = captureService.getCaptureHistory();

        assertThat(captures.toArray()).isEmpty();

        verify(captureRepository).findAll(any(Pageable.class));
    }

    @Test
    void testWhenExceptionThrownGetCaptureHistoryReturnEmpyList() {

        when(captureRepository.findAll(any(Pageable.class)))
                .thenThrow(new RuntimeException("test exception"));

        var captures = captureService.getCaptureHistory();

        assertThat(captures.toArray()).isEmpty();

        verify(captureRepository).findAll(any(Pageable.class));
    }

    // --------------------------------------------------------------------------------

//    @Test
//    void testCaptureUrlReturnError() {
//
//        when(captureApiClient.captureUrl(REQUEST_ID, URL))
//                .thenReturn(Either.left(buildErrorResponse(REQUEST_ID, "something went wrong")));
//
//        final var result = captureService.captureUrl(REQUEST_ID, URL);
//
//        assertThat(result.isLeft()).isTrue();
//        assertThat(result.getLeft()).isEqualTo("something went wrong");
//
//        verify(captureRepository).save(any(CaptureHistory.class));
//        verify(captureApiClient).captureUrl(REQUEST_ID, URL);
//    }

//    @Test
//    void testCaptureUrlThrowsException() {
//
//        when(captureApiClient.captureUrl(REQUEST_ID, URL))
//                .thenReturn(Either.right(buildCaptureResponse(REQUEST_ID, URL)));
//
//        when(captureRepository.save(any(CaptureHistory.class)))
//                .thenThrow(new RuntimeException("expected exception"));
//
//        final var result = captureService.captureUrl(REQUEST_ID, URL);
//
//        assertThat(result.isLeft()).isTrue();
//        assertThat(result.getLeft()).isEqualTo(String.format("can't capture url: %s", URL));
//
//        verify(captureRepository).save(any(CaptureHistory.class));
//    }


}
