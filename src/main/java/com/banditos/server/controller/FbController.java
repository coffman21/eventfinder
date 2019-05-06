package com.banditos.server.controller;

import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.parser.facebook.FacebookParser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/fb")
public class FbController {

    private final FacebookParser facebookParser;
    private final PlaceRepository placeRepository;

    @Autowired
    public FbController(FacebookParser facebookParser, PlaceRepository placeRepository) {
        this.facebookParser = facebookParser;
        this.placeRepository = placeRepository;
    }

    @GetMapping(path = "/get")
    public @ResponseBody List<Tusovka> getPage(@Param("fbDomain") String domain) {
        Place byFacebookDomain = placeRepository.findByFacebookDomain(domain);
        return facebookParser.parseTusovkas(byFacebookDomain);
    }
}
