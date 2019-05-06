package com.banditos.server.orm;

import com.banditos.server.model.Place;
import org.springframework.data.repository.CrudRepository;

public interface PlaceRepository extends CrudRepository<Place, Long> {

    Place findByName(String name);
    Place findByFacebookDomain(String domain);
    Place findByVkDomain(String domain);
}
