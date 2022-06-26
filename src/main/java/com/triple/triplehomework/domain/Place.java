package com.triple.triplehomework.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(columnDefinition = "varchar(36)")
    private String placeUuid;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Point> points = new ArrayList<>();

    public Place(){ this.placeUuid = UUID.randomUUID().toString(); }

    public Place(String UUID){ this.placeUuid = UUID; }

}
