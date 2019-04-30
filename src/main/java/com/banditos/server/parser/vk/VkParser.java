package com.banditos.server.parser.vk;

import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.orm.TusovkaRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class VkParser extends AbstractVkParser {

     //Parse only powerhouse for now

    VkParser(Environment env, TusovkaRepository tusovkaRepository, PlaceRepository placeRepository) {
        super(env, tusovkaRepository, placeRepository);
    }

    @PostConstruct
    private void init() {
        domain = "pwrhs";
    }
}