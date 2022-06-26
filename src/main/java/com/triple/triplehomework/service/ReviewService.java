package com.triple.triplehomework.service;

import com.triple.triplehomework.domain.*;
import com.triple.triplehomework.dto.ReviewDTO;
import com.triple.triplehomework.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final PhotoRepository photoRepository;
    private final PointRepository pointRepository;

    User user;
    Place place;

    String info = "";
    int plus = 1;
    int minus = -1;
    Point point;
    boolean firstReview = false;
    String res = "";


    public String addReview(ReviewDTO reviewDTO){
        user = userRepository.findByUserUuid(reviewDTO.getUserId());
        place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());
        Long placeId = place.getPlaceId();

        // 해당 장소에 작성된 리뷰 탐색 ( 보너스 1점 여부 체크 )
        List<Review> findByPlaceId = reviewRepository.findByPlace_PlaceId(placeId);
        log.info("findByPlaceId(): {}", findByPlaceId.size());

        // 해당 장소에 사용자가 작성한 리뷰가 있는지 확인
        Review reviewCheck = reviewRepository.findByUser_UserIdAndPlace_PlaceId(user.getUserId(), place.getPlaceId());
        // 해당 장소에 사용자가 작성한 리뷰가 없다면 진행
        if (reviewCheck == null) {
            // 포인트 부여
            if (reviewDTO.getContent() != null) {
                info = "리뷰 작성_텍스트 작성 포인트 부여";
                point = new Point(info, plus, user, place);
                pointRepository.save(point);
            }
            if (reviewDTO.getAttachedPhotoIds().length > 0) {
                info = "리뷰 작성_사진 첨부 포인트 부여";
                point = new Point(info, plus, user, place);
                pointRepository.save(point);
            }
            if (findByPlaceId.size() == 0) {
                info = "리뷰 작성_특정 장소 첫 리뷰 포인트 부여";
                point = new Point(info, plus, user, place);
                pointRepository.save(point);
                firstReview = true;
            }

            // 리뷰 DB 저장
            Review review = new Review(reviewDTO.getContent(), reviewDTO.getReviewId(), user, place, firstReview);
            review = reviewRepository.save(review);

            // 리뷰 사진 DB 저장
            for (String attached : reviewDTO.getAttachedPhotoIds()) {
                Photo photo = new Photo(attached, review);
                photoRepository.save(photo);
            }
            res = "리뷰가 작성되었습니다.";
        } else {
            res = "한 장소에 리뷰는 한개만 가능합니다.";
        }
        updatePoint(user);
        return res;
    }

    public String modReview(ReviewDTO reviewDTO){
        user = userRepository.findByUserUuid(reviewDTO.getUserId());
        place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());

        // 작성된 리뷰 찾기
        Review review = reviewRepository.findByReviewUuid(reviewDTO.getReviewId());
        if (review != null) {
            // 리뷰 첨부된 사진이 있는지 검사
            List<Photo> photoCheck = photoRepository.findByReview_ReviewId(review.getReviewId());

            // 포인트 부여 및 차감, DB 수정
            if (photoCheck.size() > 0) { // 리뷰에 사진이 있던 상황
                // 해당 리뷰의 기존 사진 DB 삭제
                photoRepository.deleteByReview_ReviewId(review.getReviewId());

                if (reviewDTO.getAttachedPhotoIds().length == 0) { // 첨부 사진이 없으면(사진을 삭제 했으면)
                    info = "리뷰 수정_사진 삭제 포인트 차감";
                    point = new Point(info, minus, user, place);
                    pointRepository.save(point);
                } else { // 첨부 사진이 있으면
                    // 사진 DB 추가
                    for (String attached : reviewDTO.getAttachedPhotoIds()) {
                        Photo photo = new Photo(attached, review);
                        photoRepository.save(photo);
                    }
                }
            } else { // 리뷰에 사진이 없던 상황
                // 첨부 사진이 있으며(사진을 추가 했으면)
                if (reviewDTO.getAttachedPhotoIds().length > 0) {
                    info = "리뷰 수정_사진 추가 포인트 부여";
                    point = new Point(info, plus, user, place);
                    pointRepository.save(point);

                    // 사진 DB 추가
                    for (String attached : reviewDTO.getAttachedPhotoIds()) {
                        Photo photo = new Photo(attached, review);
                        photoRepository.save(photo);
                    }
                }
            }

            // 리뷰 DB 수정
            review = new Review(review.getReviewId(), reviewDTO.getContent(), reviewDTO.getReviewId(), user, place, review.isFirstReview());
            reviewRepository.save(review);

            res = "리뷰가 수정되었습니다.";
        } else {
            res = "수정할 리뷰가 존재하지 않습니다.";
        }
        updatePoint(user);
        return res;
    }

    public String deleteReview(ReviewDTO reviewDTO){
        user = userRepository.findByUserUuid(reviewDTO.getUserId());
        place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());

        // 작성된 리뷰 찾기
        Review review = reviewRepository.findByReviewUuid(reviewDTO.getReviewId());
        if (review != null) {
            // 리뷰 첨부된 사진이 있는지 검사
            List<Photo> photoCheck = photoRepository.findByReview_ReviewId(review.getReviewId());

            // 포인트 차감
            if (photoCheck.size() > 0) {
                info = "리뷰 삭제_사진 첨부 포인트 차감";
                point = new Point(info, minus, user, place);
                pointRepository.save(point);
            }
            if (review.isFirstReview()) {
                info = "리뷰 삭제_보너스 포인트 차감";
                point = new Point(info, minus, user, place);
                pointRepository.save(point);
            }
            info = "리뷰 삭제_리뷰 포인트 차감";
            point = new Point(info, minus, user, place);
            pointRepository.save(point);

            // 리뷰 DB 삭제
            reviewRepository.delete(review);

            res = "리뷰가 삭제되었습니다.";
        } else {
            res = "삭제할 리뷰가 존재하지 않습니다.";
        }
        updatePoint(user);
        return res;
    }

    // user의 point를 업데이트한다.
    void updatePoint(User user) {
        int userPoint = pointRepository.getSumOfPoint(user.getUserId());
        userRepository.updatePoint(user.getUserId(), userPoint);
    }
}
