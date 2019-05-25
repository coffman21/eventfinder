package com.banditos.server.orm;

import com.banditos.server.model.Tusovka;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TusovkaRepository extends CrudRepository<Tusovka, Long> {

    List<Tusovka> findByPlaceOrderByDateDesc(String place, Pageable pageable);

    List<Tusovka> findAllByDateGreaterThanEqual(Date date, Pageable pageable);

}
