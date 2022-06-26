package com.triple.triplehomework.repository;

import com.triple.triplehomework.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPlace_PlaceId(Long placeId);

    Review findByUser_UserIdAndPlace_PlaceId(Long userId, Long placeId);

    Review findByReviewUuid(String reviewUuid);
}
