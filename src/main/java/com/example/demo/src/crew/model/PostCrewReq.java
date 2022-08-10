package com.example.demo.src.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCrewReq {
    int UC_SEQ;
    int userIdx;
    String festivalTitle;
    String crewName;
    String crewComment;
    int crewHeadCount;
    String crewMeetDate;
    String crewMeetTime;
    String crewGender;
    int crewMinAge;
    int crewMaxAge;
}
