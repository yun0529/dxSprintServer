package com.example.demo.src.crew.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Member {
    int userIdx;
    String userProfileImageUrl;
    String userNickName;
}