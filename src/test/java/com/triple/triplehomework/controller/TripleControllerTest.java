package com.triple.triplehomework.controller;

import com.triple.triplehomework.domain.Place;
import com.triple.triplehomework.domain.User;
import com.triple.triplehomework.dto.ReviewDTO;
import com.triple.triplehomework.repository.PlaceRepository;
import com.triple.triplehomework.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TripleControllerTest {

    @Autowired TripleController tripleController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlaceRepository placeRepository;

    @Test
    public void reviewPage() {
        // 임시 유저 등록
        String userUuid = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        User user = userRepository.findByUserUuid(userUuid);
        userRepository.save(user);

        // 임시 장소 등록
        String placeUuid = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
        Place place = placeRepository.findByPlaceUuid(placeUuid);
        placeRepository.save(place);

        ReviewDTO dto = new ReviewDTO();
        dto.setAction("ADD");
        dto.setReviewId("240a0658-dc5f-4878-9381-ebb7b2667772");
        dto.setUserId("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        dto.setPlaceId("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        dto.setContent("좋아요!");

        String res = tripleController.reviewEvt(dto);

        Assert.assertEquals("리뷰가 작성되었습니다.",res);
    }

    @Test
    public void reviewEvt() {
    }

    @Test
    public void userPoint() {
    }

    @Test
    public void pointRecord() {
    }
}