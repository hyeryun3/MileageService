package com.triple.triplehomework.service;

import com.triple.triplehomework.domain.Place;
import com.triple.triplehomework.domain.User;
import com.triple.triplehomework.dto.PointDTO;
import com.triple.triplehomework.dto.ReviewDTO;
import com.triple.triplehomework.repository.PlaceRepository;
import com.triple.triplehomework.repository.PointRepository;
import com.triple.triplehomework.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PointServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired PointService pointService;
    @Autowired UserRepository userRepository;
    @Autowired PlaceRepository placeRepository;
    @Autowired PointRepository pointRepository;

    static String userUuid = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
    static String placeUuid = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
    static String reviewUuid = "240a0658-dc5f-4878-9381-ebb7b2667772";
    static String[] attachedPhotoIds = {"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"};

    @Test
    public void userPoint() {
        // given
        User user = new User(userUuid);
        user = userRepository.save(user);

        Place place = new Place(placeUuid);
        placeRepository.save(place);

        ReviewDTO dto = new ReviewDTO();
        dto.setUserId(userUuid);
        dto.setPlaceId(placeUuid);
        dto.setContent("리뷰작성");
        dto.setReviewId(reviewUuid);
        dto.setAttachedPhotoIds(attachedPhotoIds);
        reviewService.addReview(dto);

        // when
        int point = pointService.userPoint(userUuid);

        // then
        Assert.assertEquals(point, pointRepository.getSumOfPoint(user.getUserId()));
    }

    @Test
    public void pointRecord() { // 수정해야함.
        // given
        User user = new User(userUuid);
        user = userRepository.save(user);

        Place place = new Place(placeUuid);
        placeRepository.save(place);

        ReviewDTO dto = new ReviewDTO();
        dto.setUserId(userUuid);
        dto.setPlaceId(placeUuid);
        dto.setContent("리뷰작성");
        dto.setReviewId(reviewUuid);
        dto.setAttachedPhotoIds(attachedPhotoIds);
        reviewService.addReview(dto);

        // when
        List<PointDTO> pointRecord = pointService.pointRecord(userUuid);

        // then
        Assert.assertEquals(pointRecord, pointRepository.findByUser_UserId(user.getUserId()));
    }
}