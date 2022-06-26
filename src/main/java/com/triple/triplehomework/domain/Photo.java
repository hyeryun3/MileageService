package com.triple.triplehomework.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @NotNull
    private String attachedPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId")
    private Review review;

    // 임시 데이터 저장을 위한 생성자
    public Photo(String attached, Review review) {
        this.attachedPhoto = attached;
        this.review = review;
    }
}
