package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMessageList {
    int roomIdx;
    int userIdx;
    String userProfileImageUrl;
    String userNickName;
    String type;
    String chatContent;
    String updatedAt;
}