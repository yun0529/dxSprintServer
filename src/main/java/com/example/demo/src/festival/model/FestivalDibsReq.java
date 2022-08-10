package com.example.demo.src.festival.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FestivalDibsReq {
    int userIdx;
    int FestivalIdx;
    String status;
}
