package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkUserId(postUserReq.getUserId()) ==1){
            throw new BaseException(POST_USERS_EXISTS_NUMBER);
        }
        /*String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUserPw());
            postUserReq.setUserPw(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }*/

        try{
            int userNo = userDao.createUser(postUserReq);
            //jwt 발급.
            //String jwt = jwtService.createJwt(userNo);
            Random rand  = new Random();
            String randomSum= "";
            for(int i=0; i<4; i++) {
                String ran = Integer.toString(rand.nextInt(10));
                randomSum+=ran;
            }
            int userCode = Integer.parseInt(randomSum);
            return new PostUserRes(userCode,userNo);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        User user = userDao.getNo(patchUserReq.getUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void modifyInterestCategory(PatchInterestCategoryReq patchInterestCategoryReq) throws BaseException {
        User user = userDao.getNo(patchInterestCategoryReq.getUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            int result = userDao.modifyInterestCategory(patchInterestCategoryReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
