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

    private final PointService pointService;

    // 리뷰 작성
    public String addReview(ReviewDTO reviewDTO){
        boolean firstReview = false;
        String res = "";
        String info = "";

        // 해당 장소에 사용자가 작성한 리뷰가 있는지 확인
        User user = userRepository.findByUserUuid(reviewDTO.getUserId());
        Place place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());
        Review reviewCheck = reviewRepository.findByUser_UserIdAndPlace_PlaceId(user.getUserId(), place.getPlaceId());

        // 해당 장소에 사용자가 작성한 리뷰가 없다면 진행
        if (reviewCheck == null) {
            place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());

            // 해당 장소에 작성된 리뷰 탐색 ( 보너스 1점 여부 체크 )
            List<Review> findByPlaceId = reviewRepository.findByPlace_PlaceId(place.getPlaceId());
            // 작성된 리뷰가 없는 장소라면
            if(findByPlaceId.size()==0){
                firstReview=true;
            }

            // 리뷰 DB 저장
            Review review = new Review(reviewDTO.getContent(), reviewDTO.getReviewId(), user, place, firstReview);
            review = reviewRepository.save(review);

            if(reviewDTO.getAttachedPhotoIds() != null){
                // 리뷰 사진 DB 저장
                for (String attached : reviewDTO.getAttachedPhotoIds()) {
                    Photo photo = new Photo(attached, review);
                    photoRepository.save(photo);
                }
            }

            // 포인트 적립
            if (reviewDTO.getContent() != null) {
                info = "리뷰 작성_텍스트 작성 포인트 부여";
                pointService.plusPoint(info, user, place);
            }
            // 첨부 이미지가 없을 시, Null값으로 들어오는지 빈 배열로 들어오는지 모르기 때문에 예외처리하였음.
            try {
                if (reviewDTO.getAttachedPhotoIds().length > 0) {
                    info = "리뷰 작성_사진 첨부 포인트 부여";
                    pointService.plusPoint(info, user, place);
                }
            } catch (Exception e){
                log.info("Exception : {}",e.getMessage());
            }
            if (firstReview) {
                info = "리뷰 작성_특정 장소 첫 리뷰 포인트 부여";
                pointService.plusPoint(info, user, place);
            }
            pointService.updatePoint(user);
            res = "리뷰가 작성되었습니다.";
        } else {
            res = "한 장소에 리뷰는 한개만 가능합니다.";
        }

        return res;
    }

    // 리뷰 수정
    public String modReview(ReviewDTO reviewDTO) {
        String res = "";
        String info = "";

        User user = userRepository.findByUserUuid(reviewDTO.getUserId());
        Place place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());

        // 작성된 리뷰 찾기
        Review review = reviewRepository.findByReviewUuid(reviewDTO.getReviewId());
        // 작성된 리뷰가 있다면
        if (review != null) {
            // 리뷰 DB 수정
            review = new Review(review.getReviewId(), reviewDTO.getContent(), reviewDTO.getReviewId(), user, place, review.isFirstReview());
            reviewRepository.save(review);

            // 리뷰에 첨부된 사진이 있는지 검사
            List<Photo> photoCheck = photoRepository.findByReview_ReviewId(review.getReviewId());

            // 기존 리뷰에 사진 존재 여부에 따른 조건
            if (photoCheck.size() > 0) { // 작성된 리뷰에 첨부된 사진이 있는 상황
                // 해당 리뷰의 기존 사진 DB 삭제
                photoRepository.deleteByReview_ReviewId(review.getReviewId());

                if (reviewDTO.getAttachedPhotoIds()==null || reviewDTO.getAttachedPhotoIds().length == 0) { // 첨부 사진이 없을 시 (=사진을 삭제 했으면)
                    info = "리뷰 수정_사진 삭제 포인트 차감";
                    pointService.minusPoint(info,user,place);
                } else { // 첨부 사진이 있으면
                    // 사진 DB 추가
                    for (String attached : reviewDTO.getAttachedPhotoIds()) {
                        Photo photo = new Photo(attached, review);
                        photoRepository.save(photo);
                    }
                }
            } else { // 작성된 리뷰에 첨부된 사진이 없는 상황
                // 첨부 이미지가 없을 시, Null값으로 들어오는지 빈 배열로 들어오는지 모르기 때문에 예외처리하였음.
                try {
                    if (reviewDTO.getAttachedPhotoIds().length > 0) { // 첨부 사진이 있을 시 (=사진을 추가 했으면)
                        // 사진 DB 추가
                        for (String attached : reviewDTO.getAttachedPhotoIds()) {
                            Photo photo = new Photo(attached, review);
                            photoRepository.save(photo);
                        }

                        info = "리뷰 수정_사진 추가 포인트 부여";
                        pointService.plusPoint(info, user, place);
                    }
                } catch (Exception e){
                    log.info("Exception : {}",e.getMessage());
                }
            }
            pointService.updatePoint(user);
            res = "리뷰가 수정되었습니다.";
        } else {
            res = "수정할 리뷰가 존재하지 않습니다.";
        }
        return res;
    }

    // 리뷰 삭제
    public String deleteReview(ReviewDTO reviewDTO) {
        String res = "";
        String info = "";

        User user = userRepository.findByUserUuid(reviewDTO.getUserId());
        Place place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());

        // 작성된 리뷰 찾기
        Review review = reviewRepository.findByReviewUuid(reviewDTO.getReviewId());
        if (review != null) {
            // 리뷰 첨부된 사진이 있는지 검사
            List<Photo> photoCheck = photoRepository.findByReview_ReviewId(review.getReviewId());

            // 포인트 차감
            info = "리뷰 삭제_리뷰 포인트 차감";
            pointService.minusPoint(info,user,place);

            if (photoCheck.size() > 0) {
                info = "리뷰 삭제_사진 첨부 포인트 차감";
                pointService.minusPoint(info,user,place);
            }
            if (review.isFirstReview()) {
                info = "리뷰 삭제_보너스 포인트 차감";
                pointService.minusPoint(info,user,place);
            }

            // 리뷰 DB 삭제
            reviewRepository.delete(review);

            pointService.updatePoint(user);
            res = "리뷰가 삭제되었습니다.";
        } else {
            res = "삭제할 리뷰가 존재하지 않습니다.";
        }
        return res;
    }
}
