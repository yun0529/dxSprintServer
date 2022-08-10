package com.example.demo.src.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCrews {
    int festivalIdx;
    String festivalImageUrl;
    String title;
    String crewName;
    String crewGender;
    int crewHeadCount;
    int totalHeadCount;
    String crewMeetDate;
    int dibsCount;
}