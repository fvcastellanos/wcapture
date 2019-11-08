package net.cavitos.wcapture.services;

import io.micrometer.core.annotation.Timed;
import net.cavitos.wcapture.client.model.CaptureRequest;
import net.cavitos.wcapture.domain.CaptureHistory;
import net.cavitos.wcapture.message.producer.CaptureRequestProducer;
import net.cavitos.wcapture.repositories.CaptureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CaptureService {

    private static final Logger logger = LoggerFactory.getLogger(CaptureService.class);

    private final CaptureRequestProducer captureRequestProducer;
    private final CaptureRepository captureRepository;

    public CaptureService(final CaptureRequestProducer captureRequestProducer,
                          final CaptureRepository captureRepository) {

        this.captureRequestProducer = captureRequestProducer;
        this.captureRepository = captureRepository;
    }

    public void captureUrl(final String requestId, final String url) {

        logger.info("capture request received for url={}, requestId={}", url, requestId);

        var request = new CaptureRequest();
        request.setRequestId(requestId);
        request.setUrl(url);

        captureRequestProducer.produce(request);
    }

    public List<CaptureHistory> getCaptureHistory() {

        try {

            var pageRequest = PageRequest.of(0, 50, Sort.by("id").descending());

            var page = captureRepository.findAll(pageRequest);
            return page.getContent();

        } catch (Exception ex) {

            logger.error("can't get capture history - ", ex);
            return Collections.emptyList();
        }
    }
}
