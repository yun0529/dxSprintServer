package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChattingRoomList {

    int crewIdx;
    int roomIdx;
    String festivalImageUrl;
    String crewName;
    String type;
    String chatContent;
    String updatedAt;
}