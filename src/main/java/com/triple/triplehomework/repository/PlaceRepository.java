package com.triple.triplehomework.repository;

import com.triple.triplehomework.domain.Place;
import com.triple.triplehomework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PlaceRepository extends JpaRepository<Place, Long> {


    Place findByPlaceUuid(String placeUuid);
}
