package net.cavitos.phantom.web.controllers;

import net.cavitos.phantom.domain.Capture;
import net.cavitos.phantom.services.CaptureService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Controller
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Inject
    private CaptureService captureService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {

        logger.info("someone is willing to capture an url");
        return new ModelAndView("main");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView capture(Model model, String url) {

        logger.info("capture request received for url: {}", url);

        if (isEmpty(url)) {

            logger.error("not valid url provided");
            model.addAttribute("error", "Please provide a valid URL");
            return new ModelAndView("main", model.asMap());
        }

        logger.info("capture request received for url: {}", url);
        Optional<Capture> captureHolder = captureService.captureURL(url);

        if (!captureHolder.isPresent()) {

            logger.error("can't capture url: {}", url);
            model.addAttribute("error", "Can't capture the URL provided: " + url);
            return new ModelAndView("main", model.asMap());
        }

        logger.info("url {} captured, redirecting to download the image", url);
        Capture capture = captureHolder.get();
        String redirectUrl = "redirect:/file/" + capture.getCaptureId();

        return new ModelAndView(redirectUrl);
    }

    @RequestMapping(value = "/file/{capture-id}", method = RequestMethod.GET)
    public void getFile(@PathVariable("capture-id") String captureId, HttpServletResponse response) throws Exception {

        if (isEmpty(captureId)) {
            logger.error("no image information received");
            throw new Exception("Can't download image");
        }

        logger.info("trying to download image");

        InputStream inputStream = captureService.getImageFile(captureId);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.setContentType("image/png");
        response.flushBuffer();
    }

}