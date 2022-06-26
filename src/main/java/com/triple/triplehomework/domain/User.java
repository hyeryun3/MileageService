package com.triple.triplehomework.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(columnDefinition = "varchar(36)")
    private String userUuid;

    @Column(columnDefinition = "int default 0")
    private int point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> points = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public User(){ this.userUuid = UUID.randomUUID().toString(); }

    public User(String UUID){ this.userUuid = UUID; }
}
