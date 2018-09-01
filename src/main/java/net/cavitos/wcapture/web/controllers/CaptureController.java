package net.cavitos.wcapture.web.controllers;

import net.cavitos.wcapture.model.Capture;
import net.cavitos.wcapture.services.CaptureService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

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

        LOGGER.info("capture request received for url: {}", url);
        final var captureHolder = captureService.captureUrl(url);

        if (!captureHolder.isPresent()) {
            LOGGER.error("can't capture url: {}", url);
            model.addAttribute("error", "Can't capture the URL provided: " + url);
            return "main";
        }

        model.addAttribute("capture", captureHolder.get());
        return "main";
    }

    @GetMapping("/file/{capture-id}")
    public void getCapturedUrl(@PathVariable("capture-id") final String captureId, final HttpServletResponse response) throws IOException {
        if (isBlank(captureId)) {
            LOGGER.error("No image information received");
            throw new RuntimeException("Can't download image");
        }

        final var inputStream = captureService.getCapturedUrl(captureId);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.setContentType("image/png");
        response.flushBuffer();
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
