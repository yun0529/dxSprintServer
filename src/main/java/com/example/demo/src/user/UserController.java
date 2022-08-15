package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
   //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) int userNo) {
        try{
            if(userNo == 0){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByUserNo(userNo);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userNo
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userNo}") // (GET) 127.0.0.1:9000/app/users/:userNo
    public BaseResponse<GetUserRes> getUser(@PathVariable("userNo") int userNo) {
        // Get Users
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserRes getUserRes = userProvider.getUser(userNo);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 마이 당근 회원 1명 조회 API
     * [GET] /badge/:userNo
     * @return BaseResponse<GetBadge>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/badge/{userNo}") // (GET) 127.0.0.1:9000/users/badge/:userNo
    public BaseResponse<List<GetBadge>> getBadge(@PathVariable("userNo") int userNo) {
        // Get Users
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetBadge> getBadge = userProvider.getBadge(userNo);
            return new BaseResponse<>(getBadge);
        } catch(BaseException exception){
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 이메일이 입력되지 않았을 경우
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        // 비밀번호가 입력되지 않았을 경우
        if(postUserReq.getPassWord() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        // 전화번호가 입력되지 않았을 경우
        if(postUserReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
        }
        // 닉네임이 입력되지 않았을 경우
        if(postUserReq.getNickName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        //전화번호 형식
        if(!isRegexUserId(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_NUMBER);
        }
        //비밀번호 형식
        if(!isRegexUserPw(postUserReq.getPassWord())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        //이메일 형식
        if(!isRegexUserEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //전화번호 자릿수
        if(postUserReq.getPhoneNumber().length() > 13){
            return new BaseResponse<>(POST_USERS_INVALID_NUMBER_COUNT);
        }
        //닉네임 자릿수
        if(postUserReq.getNickName().length()>6 || postUserReq.getNickName().length()<2){
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME_COUNT);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 인증번호 발급 API
     * [Post] /users/certification
     * @return BaseResponse<PostCertificationUserRes>
     */
    @ResponseBody
    @PostMapping("/auth-code/{userPhoneNumber}")
    public BaseResponse<PostAuthCodeRes> postCertificationUser(@PathVariable("userPhoneNumber") String userPhoneNumber){
        // 전화번호가 입력되지 않았을 경우
        if(userPhoneNumber == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
        }
        //전화번호 형식
        if(!isRegexUserId(userPhoneNumber)){
            return new BaseResponse<>(POST_USERS_INVALID_NUMBER);
        }
        //전화번호 자릿수
        if(userPhoneNumber.length() > 13){
            return new BaseResponse<>(POST_USERS_INVALID_NUMBER_COUNT);
        }
        try{
            PostAuthCodeRes postAuthCodeRes = userProvider.postAuthCode(userPhoneNumber);
            return new BaseResponse<>(postAuthCodeRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 이메일 로그인 API
     * [POST] /users/email
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/email")
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public BaseResponse<PostLoginRes> emailLogIn(@RequestBody PostLoginReq postLoginReq){
        // 이메일이 입력되지 않았을 경우
        if(postLoginReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(postLoginReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        //이메일 형식
        if(!isRegexUserEmail(postLoginReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{

            PostLoginRes postLoginRes = userProvider.emailLogIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    @PatchMapping("/{userNo}")
    public BaseResponse<String> modifyUserName(@PathVariable("userNo") int userNo, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(user.getUserNickName().length() > 13){
                return new BaseResponse<>(INVALID_USER_NICKNAME_LENGTH);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userNo,user.getUserNickName());
            userService.modifyUserName(patchUserReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 자동 로그인 API
     * [GET] /users/auto-login
     * @return BaseResponse<GetInterestCategory>
     */
    @ResponseBody
    @GetMapping("/auto-login") // (GET) 127.0.0.1:9000/users/interestCategory/:userNo
    public BaseResponse<PostLoginRes> getAutoLogin() {
        // Get Users
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            userProvider.getAutoLogin(userIdxByJwt);
            PostLoginRes getAutoLogin = new PostLoginRes(userIdxByJwt, jwtService.getJwt());
            return new BaseResponse<>(getAutoLogin);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}