package com.banditos.server.controller;

import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.orm.TusovkaRepository;
import com.banditos.server.parser.vk.VkParser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/")
public class MainController {
    private final TusovkaRepository tusovkaRepository;
    private final PlaceRepository placeRepository;
    private final VkParser vkParser;

    @Autowired
    public MainController(TusovkaRepository tusovkaRepository,
            PlaceRepository placeRepository,
            VkParser vkParser) {
        this.tusovkaRepository = tusovkaRepository;
        this.placeRepository = placeRepository;
        this.vkParser = vkParser;
    }

    @GetMapping(path="/tusovkas")
    public @ResponseBody Iterable<Tusovka> getAllTusovkas() {
        return tusovkaRepository.findAll();
    }

    @GetMapping(path = "/runParser")
    public @ResponseBody Iterable<Tusovka> runParser(
            @Param("vkDomain") String domain) {
        Place byVkDomain = placeRepository.findByVkDomain(domain);
        return vkParser.parseTusovkas(byVkDomain);
    }

}
