package com.triple.triplehomework.repository;

import com.triple.triplehomework.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("select sum(p.point) from Point p where p.user.userId=:userId")
    int getSumOfPoint(@Param("userId") Long userId);

    List<Point> findByUser_UserId(Long userId);
}
