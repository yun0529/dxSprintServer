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
    POST_USERS_EMPTY_NUMBER(false, 2015, "전화번호를 입력해주세요."),
    POST_USERS_INVALID_NUMBER(false, 2016, "전화번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_NUMBER(false,2017,"중복된 전화번호입니다."),

    POST_USERS_INVALID_NUMBER_COUNT(false,2018,"전화번호 자릿수를 확인해주세요."),

    // [PATCH] /users/certification
    POST_USERS_INVALID_EMPTY_USERNO(false,2019,"유저 번호를 입력해주세요."),

    // [POST] /products
    POST_PRODUCTS_EMPTY_TITLE(false,2020,"상품 타이틀을 입력해주세요."),

    POST_PRODUCTS_EMPTY_CONTENT(false,2021,"상품 내용을 입력해주세요."),

    // [PATCH] /users/interestCategory/{userNo}
    POST_INVALID_USERS_INTEREST_CATEGORY_INPUT(false,2022,"관심 상품의 체크 여부를 확인해주세요."),
    // [PATCH] /products/productStatus/:userNo
    PATCH_INVALID_USERS_SALE_STATUS_INPUT(false,2023,"판매 상태 입력 여부를 확인해주세요."),

    // [POST] /products/interest
    POST_INVALID_PRODUCT_INTEREST_INPUT(false,2024,"관심 상태 입력 형식이 틀렸습니다."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    FAILED_TO_LOGIN_STATUS(false,3015,"이미 로그인된 아이디입니다."),

    DO_LOGIN(false,3016,"당근마켓 계정이 로그아웃된 상태입니다."),

    DO_KAKAO_LOGIN(false,3017,"카카오 계정이 로그아웃된 상태입니다."),

    ALREADY_LOGIN(false,3018,"당근마켓 계정이 이미 로그인된 상태입니다."),

    ALREADY_KAKAO_LOGIN(false,3019,"카카오 계정이 이미 로그인된 상태입니다."),
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
