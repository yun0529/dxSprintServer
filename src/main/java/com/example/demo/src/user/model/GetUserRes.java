package com.example.demo.src.user.model;



import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {

    private int userNo;
    private String userImageUrl;
    private String userNickname;
    private int userCode;
    private float userManner;
    private String userRedealRate;
    private String userResponseRate;
    private String createdAt;
    private String updatedAt;
    private String status;
    private String userRegionNo;
    private int userRegionCertificationNum;
    private String userSubRegionNo;
    private int userSubRegionCertificationNum;
    private String UserMainRegionUpdatedDate;

}