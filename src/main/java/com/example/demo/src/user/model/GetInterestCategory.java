package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetInterestCategory {
    private int userNo;
    private String userInterestCategory;
    private String isCheck;
}
