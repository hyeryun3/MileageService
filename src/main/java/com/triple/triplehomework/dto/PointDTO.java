package com.triple.triplehomework.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PointDTO {

    private LocalDate date;
    private int point;
    private String info;

}
