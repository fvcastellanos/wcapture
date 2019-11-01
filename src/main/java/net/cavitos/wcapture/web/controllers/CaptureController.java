package net.cavitos.wcapture.web.controllers;

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

@Controller
public class CaptureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptureController.class);

    private final CaptureService captureService;

    public CaptureController(final CaptureService captureService) {
        this.captureService = captureService;
    }

    @GetMapping("/")
    public String capture() {
        return "main";
    }

    @PostMapping("/")
    public String capture(final Model model, final String url) {
        if (!isBlank(url) && !isValidUrl(url)) {
            model.addAttribute("error", "Please provide a valid URL");
            return "main";
        }

        var requestId = UUID.randomUUID().toString();

        LOGGER.info("capture request received for url={}, created requestId={}", url, requestId);
        final var result = captureService.captureUrl(requestId, url);

        if (result.isLeft()) {

            LOGGER.error("can't capture url={}, error={}", url, result.getLeft());

            model.addAttribute("error", "Can't capture the URL provided: " + url);

            return "main";
        }

        model.addAttribute("capture", result.get());
        return "main";
    }

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
