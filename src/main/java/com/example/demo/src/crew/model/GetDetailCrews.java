package com.example.demo.src.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetDetailCrews {
    int userIdx;
    String userProfileImageUrl;
    int totalHeadCount;
    String crewMeetDate;
    String crewMeetTime;
    String crewGender;
    int crewMinAge;
    int crewMaxAge;
    String crewComment;
    int crewHeadCount;
    int dibsCount;
    List<Member> participateUser;
}