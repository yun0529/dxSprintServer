package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByUserNo(int userNo) throws BaseException{
        GetUserRes users = userDao.getUser(userNo);
        if(users.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByUserNo(userNo);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserRes getUser(int userNo) throws BaseException {
        GetUserRes users = userDao.getUser(userNo);
        if(users.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            GetUserRes getUserRes = userDao.getUser(userNo);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetBadge> getBadge(int userNo) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            List<GetBadge> getBadge = userDao.getBadgeByUserNo(userNo);
            return getBadge;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserId(String userId) throws BaseException{
        try{
            return userDao.checkUserId(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd = postLoginReq.getUserPw();
        /*try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getUserPw());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }*/

        if(user.getUserPw().equals(encryptPwd)){
            if(user.getStatus().equals("Active")){
                throw new BaseException(FAILED_TO_LOGIN_STATUS);
            }
            else{
                userDao.modifyUserStatusLogIn(postLoginReq);
                int userNo = user.getUserNo();
                String jwt = jwtService.createJwt(userNo);
                return new PostLoginRes(userNo,jwt);
            }
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public PostCertificationUserRes certificationUser(PostCertificationUserReq postCertificationUserReq) throws BaseException {
        User user = userDao.getId(postCertificationUserReq);
        String userCertId = postCertificationUserReq.getUserId();
        if(user.getUserId().equals(userCertId)){
            if(user.getStatus().equals("Active")){
                throw new BaseException(FAILED_TO_LOGIN_STATUS);
            }
            else{
                int userNo = user.getUserNo();
                Random rand  = new Random();
                String randomSum= "";
                for(int i=0; i<4; i++) {
                    String ran = Integer.toString(rand.nextInt(10));
                    randomSum+=ran;
                }
                int userCode = Integer.parseInt(randomSum);
                System.out.println(userCode);
                userDao.setPw(user.getUserId(), String.valueOf(userCode));
                return new PostCertificationUserRes(userNo,userCode);
            }
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
    public List<GetInterestCategory> getInterestCategory(int userNo) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            List<GetInterestCategory> getInterestCategory = userDao.getInterestCategory(userNo);
            return getInterestCategory;
        } catch (Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void getAutoLogin(int userNo) throws BaseException{
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
    }
}
