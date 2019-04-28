package com.banditos.server.controller;

import com.banditos.server.model.Tusovka;
import com.banditos.server.orm.TusovkaRepository;
import com.banditos.server.parser.VkParser;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import java.net.MalformedURLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/")
public class MainController {
    @Autowired
    private TusovkaRepository tusovkaRepository;

    @Autowired
    private VkParser vkParser;

    @GetMapping(path="/tusovkas")
    public @ResponseBody Iterable<Tusovka> getAllTusovkas() {
        return tusovkaRepository.findAll();
    }

    @GetMapping(path = "runParser")
    public @ResponseBody Iterable<Tusovka> runParser()
            throws ApiException, ClientException, MalformedURLException {
        List<Tusovka> tusovkas = vkParser.getTusovkas();
        return tusovkas;
    }

}
