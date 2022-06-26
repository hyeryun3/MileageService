package com.triple.triplehomework.service;

import com.triple.triplehomework.domain.Point;
import com.triple.triplehomework.domain.User;
import com.triple.triplehomework.dto.PointDTO;
import com.triple.triplehomework.repository.PointRepository;
import com.triple.triplehomework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public String userPoint(String userUuid){
        User user = userRepository.findByUserUuid(userUuid);
        if (user != null) {
            int userPoint = user.getPoint();
            return "사용자의 현재 포인트 : " + userPoint;
        } else {
            return "존재하지 않는 사용자입니다.";
        }
    }

    public List<PointDTO> pointRecord(String userUuid){
        User user = userRepository.findByUserUuid(userUuid);
        List<PointDTO> pointList = new ArrayList<>();

        if (user != null) {
            List<Point> entity = pointRepository.findByUser_UserId((user.getUserId()));

            PointDTO dto = null;

            for (Point pointData : entity) {
                dto = new PointDTO();
                dto.setDate(pointData.getDate());
                dto.setInfo(pointData.getInfo());
                dto.setPoint(pointData.getPoint());
                pointList.add(dto);
            }
        }
        return pointList;
    }

}
