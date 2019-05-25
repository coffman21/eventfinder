package com.banditos.server.parser.vk;

import com.banditos.server.model.Place;
import com.banditos.server.orm.PlaceRepository;
import com.banditos.server.orm.TusovkaRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class VkParser extends AbstractVkParser {

     //Parse only powerhouse for now

    VkParser(Environment env, TusovkaRepository tusovkaRepository) {
        super(env, tusovkaRepository);
    }

    @Override
    public Place resolvePlace(String placeDomain) {
        throw new UnsupportedOperationException("not implemented");
    }
}
