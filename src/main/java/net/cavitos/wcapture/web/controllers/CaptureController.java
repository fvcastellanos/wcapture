package net.cavitos.wcapture.web.controllers;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import net.cavitos.wcapture.services.CaptureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URL;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Controller
public class CaptureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptureController.class);

    private final MeterRegistry meterRegistry;

    private final CaptureService captureService;

    public CaptureController(final CaptureService captureService, final MeterRegistry meterRegistry) {
        this.captureService = captureService;
        this.meterRegistry = meterRegistry;
    }

    @Counted
    @GetMapping("/")
    public String capture(final Model model) {

        model.addAttribute("requestId", UUID.randomUUID().toString());
        return "main";
    }

    @PostMapping("/")
    public String capture(final Model model, final String requestId, final String url) {

        if (!isBlank(url) && !isValidUrl(url)) {
            model.addAttribute("error", "Please provide a valid URL");
            return "main";
        }

        var generatedRequestId = requestId;
        if (isEmpty(requestId)) {

            generatedRequestId = UUID.randomUUID().toString();
        }

        LOGGER.info("capture request received for url={}, created requestId={}", url, generatedRequestId);
        captureService.captureUrl(generatedRequestId, url);

        model.addAttribute("requestId", generatedRequestId);
        return "main";
    }

    @GetMapping("/captures")
    @Timed("load_captures")
    public String showCaptures(final Model model) {

        LOGGER.info("getting capture history latest 50 captures...for now");
        var captures = captureService.getCaptureHistory();

        model.addAttribute("captures", captures);

        return "captures";
    }

    // ------------------------------------------------------------------------------------------------------------

    private boolean isValidUrl(final String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (final Exception exception) {
            LOGGER.error("Invalid url={}", url, exception);
            return false;
        }
    }

}
