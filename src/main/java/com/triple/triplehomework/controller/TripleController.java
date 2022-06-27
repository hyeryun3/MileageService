package com.triple.triplehomework.controller;

import com.triple.triplehomework.domain.Place;
import com.triple.triplehomework.domain.User;
import com.triple.triplehomework.dto.PointDTO;
import com.triple.triplehomework.dto.ReviewDTO;
import com.triple.triplehomework.repository.PlaceRepository;
import com.triple.triplehomework.repository.UserRepository;
import com.triple.triplehomework.service.PointService;
import com.triple.triplehomework.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TripleController {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    private final PointService pointService;
    private final ReviewService reviewService;

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

    // 리뷰 이벤트
    @ResponseBody
    @PostMapping("events")
    public String reviewEvt(@RequestBody ReviewDTO reviewDTO){
        switch (reviewDTO.getAction()) {
            case "ADD":
                return reviewService.addReview(reviewDTO);
            case "MOD":
                return reviewService.modReview(reviewDTO);
            case "DELETE":
                return reviewService.deleteReview(reviewDTO);
        }
        return "";
    }

    // 포인트 조회
    @ResponseBody
    @GetMapping("/userPoint/{userId}")
    public String userPoint(@PathVariable("userId") String userUuid) {
        int userPoint = pointService.userPoint(userUuid);
        if(userPoint==-1){
            return "존재하지 않는 사용자입니다.";
        }else{
            return "사용자의 현재 포인트 : " + userPoint;
        }
    }

    // 포인트 이력
    @ResponseBody
    @GetMapping("/pointRecord/{userId}")
    public List<PointDTO> pointRecord(@PathVariable("userId") String userUuid) {
        return pointService.pointRecord(userUuid);
    }

}
