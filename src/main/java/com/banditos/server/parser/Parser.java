package com.banditos.server.parser;

import com.banditos.server.model.Place;
import com.banditos.server.model.Tusovka;
import java.util.List;

public interface Parser {
    List<Tusovka> parseTusovkas(Place place);

    Place resolvePlace(String placeDomain);
}
