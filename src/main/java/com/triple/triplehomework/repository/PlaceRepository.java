package com.triple.triplehomework.repository;

import com.triple.triplehomework.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PlaceRepository extends JpaRepository<Place, Long> {


    Place findByPlaceUuid(String placeUuid);
}
