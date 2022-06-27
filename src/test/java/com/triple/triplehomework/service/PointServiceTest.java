package com.triple.triplehomework.service;

import com.triple.triplehomework.domain.Place;
import com.triple.triplehomework.domain.Point;
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

import java.time.LocalDate;
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
    public void plusPoint(){
        // given
        User user = new User(userUuid);
        Place place = new Place(placeUuid);
        String info = "포인트 추가";

        // when
        Point point = pointService.plusPoint(info,user,place);

        // then
        Assert.assertEquals("포인트는 1씩 적립된다.",1,point.getPoint());
        Assert.assertEquals("포인트 적립 내용은 '포인트 추가'",info,point.getInfo());
    }

    @Test
    public void minusPoint(){
        // given
        User user = new User(userUuid);
        Place place = new Place(placeUuid);
        String info = "포인트 차감";

        // when
        Point point = pointService.minusPoint(info,user,place);

        // then
        Assert.assertEquals("포인트는 1씩 차감된다.",-1,point.getPoint());
        Assert.assertEquals("포인트 적립 내용은 '포인트 차감'",info,point.getInfo());
    }

    @Test
    public void updatePoint(){
        // given
        User user = new User(userUuid);
        userRepository.save(user);

        Place place = new Place(placeUuid);
        placeRepository.save(place);

        String info = "포인트 추가";
        for(int i=0; i<3; i++){
            pointService.plusPoint(info,user,place);
        }

        // when
        pointService.updatePoint(user);

        // then
        Assert.assertEquals("사용자의 포인트는 업데이트 되어야 한다.",3,userRepository.findByUserUuid(userUuid).getPoint());
    }

    @Test
    public void userPoint(){
        // given
        User user = new User(userUuid);
        userRepository.save(user);

        // when
        int point = pointService.userPoint(userUuid);

        // then
        Assert.assertEquals("존재하지 않는 사용자면 -1을 리턴한다.",-1,pointService.userPoint("1212"));
        Assert.assertEquals("사용자의 현재 포인트를 리턴한다.",0, point);
    }

    @Test
    public void pointRecord(){
        // given
        User user = new User(userUuid);
        userRepository.save(user);

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
        Assert.assertEquals("포인트 기록_포인트",1, pointRecord.get(0).getPoint());
        Assert.assertEquals("포인트 기록_내용","리뷰 작성_텍스트 작성 포인트 부여", pointRecord.get(0).getInfo());
        Assert.assertEquals("포인트 기록_내용","리뷰 작성_사진 첨부 포인트 부여", pointRecord.get(1).getInfo());
        Assert.assertEquals("포인트 기록_내용","리뷰 작성_특정 장소 첫 리뷰 포인트 부여", pointRecord.get(2).getInfo());
        Assert.assertEquals("포인트 기록_날짜", LocalDate.now(), pointRecord.get(0).getDate());
    }
}