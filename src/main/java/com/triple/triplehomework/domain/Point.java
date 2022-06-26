package com.triple.triplehomework.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    private String info;
    private int point;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId")
    private Place place;

    public Point(){
        this.date = LocalDate.now();
    }

    public Point(String info, int point, User user, Place place) {
        this.info = info;
        this.point = point;
        this.user = user;
        this.place = place;
        this.date = LocalDate.now();
    }
}
