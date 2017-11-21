package net.cavitos.phantom.web.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import java.util.Map;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        return new ModelAndView("main");
    }

    @RequestMapping(value = "/capture", method = RequestMethod.POST)
    public ModelAndView capture(String url) {

        if (StringUtils.isEmpty(url)) {
            Map<String, String> map = Maps.newHashMap();
            map.put("error", "Please provide a valid URL");

            return new ModelAndView("main", map);
        }

        return new ModelAndView("main");
    }

}