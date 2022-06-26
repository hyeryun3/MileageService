package com.triple.triplehomework.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewDTO {

    private String action;
    private String userId;
    private String placeId;
    private String reviewId;
    private String content;
    private String[] attachedPhotoIds;

}
