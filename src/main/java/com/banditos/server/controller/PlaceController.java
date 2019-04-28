package com.banditos.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/place")
public class PlaceController {

    @GetMapping(path = "/add")
    @ResponseBody
    public String addPlace(@RequestParam("place") String place) {
        return place;
    }
}
