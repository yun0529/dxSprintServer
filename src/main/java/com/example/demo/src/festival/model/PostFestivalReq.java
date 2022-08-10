package com.example.demo.src.festival.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFestivalReq {
    int UC_SEQ;
    String MAIN_TITLE;
    String GUGUN_NM;
    double LAT;
    double LNG;
    String PLACE;
    String TITLE;
    String SUBTITLE;
    String MAIN_PLACE;
    String ADDR1;
    String ADDR2;
    String CNTCT_TEL;
    String HOMEPAGE_URL;
    String TRFC_INFO;
    String USAGE_DAY;
    String USAGE_DAY_WEEK_AND_TIME;
    String USAGE_AMOUNT;
    String MAIN_IMG_NORMAL;
    String MAIN_IMG_THUMB;
    String ITEMCNTNTS;
    String MIDDLE_SIZE_RM1;
}
