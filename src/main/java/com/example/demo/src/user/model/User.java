package com.example.demo.src.user.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userNo;
    private String userId;
    private String userPw;
    private String userNickname;
    private String status;
}
