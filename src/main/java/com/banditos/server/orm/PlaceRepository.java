package com.banditos.server.orm;

import com.banditos.server.model.Place;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaceRepository extends CrudRepository<Place, Long> {
    List<Place> findByName(String name);
}
