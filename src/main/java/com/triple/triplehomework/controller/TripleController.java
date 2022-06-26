package com.triple.triplehomework.controller;

import com.triple.triplehomework.domain.*;
import com.triple.triplehomework.dto.PointDTO;
import com.triple.triplehomework.dto.ReviewDTO;
import com.triple.triplehomework.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TripleController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PhotoRepository photoRepository;
    private final PointRepository pointRepository;

    @GetMapping("/")
    public String reviewPage() {

        // 임시 유저 등록
        String userUuid = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        User user = userRepository.findByUserUuid(userUuid);
        if (user == null) {
            user = new User(userUuid);
            userRepository.save(user);
        }

        // 임시 장소 등록
        String placeUuid = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
        Place place = placeRepository.findByPlaceUuid(placeUuid);
        if (place == null) {
            place = new Place(placeUuid);
            placeRepository.save(place);
        }

        return "review";
    }

    // 포인트 적립
    @ResponseBody
    @PostMapping("events")
    public String reviewEvt(@RequestBody ReviewDTO reviewDTO) {
        // ====== 데이터 입력받은 상황 가정 ======//

        // 전달받은 User의 uuid값으로 user 객체 찾기
        User user = userRepository.findByUserUuid(reviewDTO.getUserId());

        // 전달받은 Place의 uuid값으로 place 객체 찾기
        Place place = placeRepository.findByPlaceUuid(reviewDTO.getPlaceId());

        String info = "";
        int plus = 1;
        int minus = -1;
        Point point;
        boolean firstReview = false;
        String res = "";

        // 전달받은 Place의 Uuid 값으로 pk(place_id) 찾기.
        Long placeId = place.getPlaceId();
        log.info("placeId: {}", placeId);

        // 해당 장소에 작성된 리뷰 탐색 ( 보너스 1점 여부 체크 )
        List<Review> findByPlaceId = reviewRepository.findByPlace_PlaceId(placeId);
        log.info("findByPlaceId(): {}", findByPlaceId.size());

        if (reviewDTO.getAction().equals("ADD")) {   // 리뷰 생성
            // 해당 장소에 작성한 리뷰가 있는지 확인
            Review reviewCheck = reviewRepository.findByUser_UserIdAndPlace_PlaceId(user.getUserId(), place.getPlaceId());
            // 해당 장소에 작성한 리뷰가 없다면 진행
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

        } else if (reviewDTO.getAction().equals("MOD")) { // 리뷰 수정
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

        } else if (reviewDTO.getAction().equals("DELETE")) {  // 리뷰 삭제
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
        }

        int userPoint = pointRepository.getSumOfPoint(user.getUserId());
        // 리뷰 추가, 수정, 삭제 시 user의 point를 업데이트한다.
        userRepository.updatePoint(user.getUserId(), userPoint);

        return res;
    }

    // 포인트 조회
    @ResponseBody
    @GetMapping("/userPoint/{userId}")
    public String userPoint(@PathVariable("userId") String userUuid) {
        User user = userRepository.findByUserUuid(userUuid);
        if (user != null) {
            int userPoint = user.getPoint();
            return "사용자의 현재 포인트 : " + userPoint;
        } else {
            return "존재하지 않는 사용자입니다.";
        }
    }

    // 포인트 이력
    @ResponseBody
    @GetMapping("/pointRecord/{userId}")
    public List<PointDTO> pointRecord(@PathVariable("userId") String userUuid) {
        User user = userRepository.findByUserUuid(userUuid);

        if (user == null) {
            return null;
        }

        List<Point> entity = pointRepository.findByUser_UserId((user.getUserId()));

        PointDTO dto = null;
        List<PointDTO> pointList = new ArrayList<>();

        for (Point pointData : entity) {
            dto = new PointDTO();
            dto.setDate(pointData.getDate());
            dto.setInfo(pointData.getInfo());
            dto.setPoint(pointData.getPoint());
            pointList.add(dto);
        }

        return pointList;

    }

}
