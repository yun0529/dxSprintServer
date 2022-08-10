package com.example.demo.src.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCrewParticipateReq {
    int crewIdx;
    int roomIdx;
    int userIdx;
}
