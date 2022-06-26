package com.triple.triplehomework.repository;

import com.triple.triplehomework.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByReview_ReviewId(Long reviewId);

    void deleteByReview_ReviewId(Long reviewId);
}
