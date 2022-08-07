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
import static com.example.demo.utils.ValidationRegex.isRegexUserId;
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getUserId() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NUMBER);
        }
        //전화번호 형식
        if(!isRegexUserId(postUserReq.getUserId())){
            return new BaseResponse<>(POST_USERS_INVALID_NUMBER);
        }
        //전화번호 자릿수
        if(postUserReq.getUserId().length() > 13){
            return new BaseResponse<>(POST_USERS_INVALID_NUMBER_COUNT);
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @PatchMapping("/certification")
    public BaseResponse<PostCertificationUserRes> postCertificationUser(@RequestBody PostCertificationUserReq postCertificationUserReq){
        try{
            PostCertificationUserRes postCertificationUserRes = userProvider.certificationUser(postCertificationUserReq);
            return new BaseResponse<>(postCertificationUserRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validation 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
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
            if(user.getUserNickname().length() > 13){
                return new BaseResponse<>(INVALID_USER_NICKNAME_LENGTH);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userNo,user.getUserNickname());
            userService.modifyUserName(patchUserReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 관심 카테고리 조회 API
     * [GET] /users/interestCategory/:userNo
     * @return BaseResponse<GetInterestCategory>
     */
    @ResponseBody
    @GetMapping("/interestCategory/{userNo}") // (GET) 127.0.0.1:9000/users/interestCategory/:userNo
    public BaseResponse<List<GetInterestCategory>> getInterestCategory(@PathVariable("userNo") int userNo) {
        // Get Users
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetInterestCategory> getInterestCategory = userProvider.getInterestCategory(userNo);
            return new BaseResponse<>(getInterestCategory);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 관심카테고리 설정 API
     * [PATCH] /users/interestCategory/:userNo
     * @return BaseResponse<String>
     */
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    @PatchMapping("/interestCategory/{userNo}")
    public BaseResponse<String> modifyInterestCategory(@PathVariable("userNo") int userNo, @RequestBody InterestCategory interestCategory){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNo != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(interestCategory.getIsCheck().equals("Y")){
                PatchInterestCategoryReq patchInterestCategoryReq = new PatchInterestCategoryReq(userNo,interestCategory.getInterestCategoryNo(), interestCategory.getIsCheck());
                userService.modifyInterestCategory(patchInterestCategoryReq);

                String result = "";
                return new BaseResponse<>(result);
            }
            else if(interestCategory.getIsCheck().equals("N")){
                PatchInterestCategoryReq patchInterestCategoryReq = new PatchInterestCategoryReq(userNo,interestCategory.getInterestCategoryNo(), interestCategory.getIsCheck());
                userService.modifyInterestCategory(patchInterestCategoryReq);

                String result = "";
                return new BaseResponse<>(result);
            }
            else{
                return new BaseResponse<>(POST_INVALID_USERS_INTEREST_CATEGORY_INPUT);
            }
        } catch (BaseException exception) {
            System.out.println(exception);
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

    /**
     * 카카오 소셜 로그인 API
     * [GET] /users/login/kakao
     *
     */
/*    @ResponseBody
    @GetMapping("/login/kakao") // (GET) 127.0.0.1:9000/users/login/kakao
    public BaseResponse<String> kakaoCallback(@RequestParam String code) throws BaseException {
        String access_Token = userService.getKaKaoAccessToken(code);
        userService.createKakaoUser(access_Token);
        System.out.println("token : " + code);
        String result = code;
        return new BaseResponse<>(result);
    }

*/
}