package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2011, "이메일을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2012, "비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_PHONENUMBER(false, 2013, "전화번호를 입력해주세요."),
    POST_USERS_EMPTY_NICKNAME(false, 2014, "닉네임을 입력해주세요."),
    POST_USERS_INVALID_NUMBER(false, 2015, "전화번호 형식을 확인해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2016, "비밀번호 형식을 확인해주세요."),
    POST_USERS_INVALID_NUMBER_COUNT(false,2017,"전화번호 자릿수를 확인해주세요."),
    POST_USERS_INVALID_NICKNAME_COUNT(false,2018,"닉네임은 2~6자로 입력해주세요."),
    // [POST] /festivals
    POST_FESTIVALS_INVALID_MAIN_TITLE(false,2100,"콘텐츠명 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_GUGUN_NM(false,2101,"군,구 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_PLACE(false,2102,"장소 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_TITLE(false,2104,"제목 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_SUBTITLE(false,2105,"부제목 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_MAIN_PLACE(false,2106,"주요장소 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_ADDR1(false,2107,"주소 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_ADDR2(false,2108,"주소 기타 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_CNTCT_TEL(false,2109,"연락처 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_HOMEPAGE_URL(false,2110,"홈페이지 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_TRFC_INFO(false,2111,"교통정보 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_USAGE_DAY(false,2112,"운영기간 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_USAGE_DAY_WEEK_AND_TIME(false,2113,"이용요일 및 시간 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_USAGE_AMOUNT(false,2113,"이용요금 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_MAIN_IMG_NORMAL(false,2114,"이미지URL 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_MAIN_IMG_THUMB(false,2115,"썸네일이미지URL 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_ITEMCNTNTS(false,2116,"상세내용 글자수를 확인해주세요."),
    POST_FESTIVALS_INVALID_MIDDLE_SIZE_RM1(false,2117,"편의시설 글자수를 확인해주세요."),
    //[POST] /crews
    POST_CREWS_INVALID_MAIN_TITLE(false,2200,"콘텐츠명 글자수를 확인해주세요."),
    POST_CREWS_INVALID_CREW_NAME(false,2201,"크루이름 글자수를 확인해주세요."),
    POST_CREWS_INVALID_CREW_COMMENT(false,2202,"크루소개 글자수를 확인해주세요."),
    POST_CREWS_INVALID_CREW_HEAD_COUNT(false,2203,"크루 전체 인원은 100명이 최대입니다."),
    POST_CREWS_INVALID_CREW_MEET_DATE(false,2204,"크루 만나는 날짜 글자수를 확인해주세요."),
    POST_CREWS_INVALID_CREW_MEET_TIME(false,2205,"크루 만나는 시간 글자수를 확인해주세요."),
    POST_CREWS_INVALID_CREW_GENDER(false,2206,"크루 성별 글자수를 확인해주세요."),
    POST_CREWS_INVALID_CREW_MIN_TAGE(false,2207,"크루 최소 나이는 0세까지만 가능합니다."),
    POST_CREWS_INVALID_CREW_MAX_AGE(false,2208,"크루 최대 나이는 100세까지만 가능합니다."),
    //[POST] /crews(/festivals)/dibs
    POST_INVALID_DIBS_STATUS(false,2250,"좋아요 상태는 Active 또는 Inactive로 입력해주세요."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3010, "중복된 이메일입니다."),
    DUPLICATED_NUMBER(false,3011,"중복된 전화번호입니다."),
    DUPLICATED_NICKNAME(false,3012,"중복된 닉네임입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    FAILED_TO_LOGIN_STATUS(false,3015,"이미 로그인된 아이디입니다."),

    DO_LOGIN(false,3016,"계정이 로그아웃된 상태입니다."),

    DO_KAKAO_LOGIN(false,3017,"카카오 계정이 로그아웃된 상태입니다."),

    ALREADY_LOGIN(false,3018,"당근마켓 계정이 이미 로그인된 상태입니다."),

    ALREADY_KAKAO_LOGIN(false,3019,"카카오 계정이 이미 로그인된 상태입니다."),

    // [POST] /crews/participate
    ALREADY_FULL_COUNT(false,3100,"모집이 완료된 크루입니다."),
    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[POST] /products/interest/{productNo}
    MODIFY_FAIL_INTEREST(false,4014,"관심 설정 실패"),

    MODIFY_FAIL_KAKAO_JWT(false,4015,"카카오 JWT 수정 실패"),
    // 5000 : 필요시 만들어서 쓰세요
    //PATCH
    INVALID_USER_NICKNAME_LENGTH(false,5000,"닉네임은 13자리 이상으로 설정할 수 없습니다.");
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
