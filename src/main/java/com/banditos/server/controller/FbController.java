package com.banditos.server.controller;

import com.banditos.server.model.Tusovka;
import com.banditos.server.parser.facebook.FacebookParser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/fb")
public class FbController {

    private final FacebookParser facebookParser;

    @Autowired
    public FbController(
            FacebookParser facebookParser) {
        this.facebookParser = facebookParser;
        facebookParser.setDomain("powerhousemoscow");
    }

    @GetMapping(path = "/get")
    public @ResponseBody List<Tusovka> getPage() {
        return facebookParser.parseTusovkas();
    }
}
