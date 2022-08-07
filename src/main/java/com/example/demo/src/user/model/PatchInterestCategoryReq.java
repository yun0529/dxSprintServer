package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchInterestCategoryReq {
    private int userNo;
    private int interestCategoryNo;
    private String isCheck;
}
