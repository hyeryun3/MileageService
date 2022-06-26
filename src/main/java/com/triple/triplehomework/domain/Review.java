package com.triple.triplehomework.domain;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @NotNull
    private String content;

    @NotNull
    private LocalDate date;

    @Column(columnDefinition = "varchar(36)")
    private String reviewUuid;

    @Column(columnDefinition = "boolean default false")
    private boolean firstReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId")
    private Place place;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Photo> photos;

    public Review(){
        this.date = LocalDate.now();
        this.reviewUuid = UUID.randomUUID().toString();
    }

    // 임시 데이터 저장을 위한 생성자
    public Review(String content, String reviewUuid, User user, Place place,boolean firstReview) {
        this.date = LocalDate.now();
        this.content = content;
        this.reviewUuid = reviewUuid;
        this.user = user;
        this.place = place;
        this.firstReview = firstReview;
    }

    // 리뷰 수정을 위한 생성자
    public Review(Long reviewId, String content, String reviewUuid, User user, Place place, boolean firstReview) {
        this.reviewId = reviewId;
        this.date = LocalDate.now();
        this.content = content;
        this.reviewUuid = reviewUuid;
        this.user = user;
        this.place = place;
        this.firstReview = firstReview;
    }
}
