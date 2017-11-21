package net.cavitos.phantom.web.controllers;

import net.cavitos.phantom.domain.Capture;
import net.cavitos.phantom.services.CaptureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Controller
public class MainController {

    @Inject
    private CaptureService captureService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        return new ModelAndView("main");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView capture(Model model, String url) {

        if (isEmpty(url)) {
            model.addAttribute("error", "Please provide a valid URL");
            return new ModelAndView("main", model.asMap());
        }

        Optional<Capture> captureHolder = captureService.captureURL(url);

        if (!captureHolder.isPresent()) {
            model.addAttribute("error", "Can't capture the URL provided: " + url);
            return new ModelAndView("main", model.asMap());
        }

        model.addAttribute("capture", captureHolder.get());
        return new ModelAndView("main", model.asMap());
    }

}