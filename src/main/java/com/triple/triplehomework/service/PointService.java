package com.triple.triplehomework.service;

import com.triple.triplehomework.domain.Place;
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

    final int plus = 1;
    final int minus = -1;

    public Point plusPoint(String info, User user, Place place){
        Point point = new Point(info, plus, user, place);
        return pointRepository.save(point);
    }

    public Point minusPoint(String info, User user, Place place){
        Point point = new Point(info, minus, user, place);
        return pointRepository.save(point);
    }

    public void updatePoint(User user) {
        int userPoint = pointRepository.getSumOfPoint(user.getUserId());
        userRepository.updatePoint(user.getUserId(), userPoint);
    }

    public int userPoint(String userUuid){
        User user = userRepository.findByUserUuid(userUuid);
        if (user != null) {
            int userPoint = user.getPoint();
            return userPoint;
        } else {
            return -1;
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
