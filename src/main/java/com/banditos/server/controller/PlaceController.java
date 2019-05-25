package com.banditos.server.controller;

import com.banditos.server.model.Place;
import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.parser.facebook.FacebookParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/place")
public class PlaceController {

    private final PlaceRepository placeRepository;
    private final FacebookParser parser;

    public PlaceController(PlaceRepository placeRepository, FacebookParser parser) {
        this.placeRepository = placeRepository;
        this.parser = parser;
    }

    @GetMapping(path = "/add")
    @ResponseBody
    public String addPlace(@RequestParam("place") String placeDomain) {
        Place place = parser.resolvePlace(placeDomain);
        placeRepository.save(place);
        return placeDomain;
    }


    @GetMapping(path = "/delete")
    @ResponseBody
    public String deletePlace(@RequestParam("place") String place) {

        return place;
    }
}
