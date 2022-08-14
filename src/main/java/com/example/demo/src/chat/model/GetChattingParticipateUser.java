package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChattingParticipateUser {
    int userIdx;
    String userProfileImageUrl;
    String userNickName;
}