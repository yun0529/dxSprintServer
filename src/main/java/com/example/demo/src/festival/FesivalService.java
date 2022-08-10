package com.example.demo.src.festival;


import com.example.demo.config.BaseException;
import com.example.demo.src.crew.model.CrewDibsReq;
import com.example.demo.src.festival.model.FestivalDibsReq;
import com.example.demo.src.festival.model.PostFestivalReq;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FesivalService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FestivalDao festivalDao;
    private final FestivalProvider festivalProvider;
    private final JwtService jwtService;


    @Autowired
    public FesivalService(FestivalDao festivalDao, FestivalProvider festivalProvider, JwtService jwtService) {
        this.festivalDao = festivalDao;
        this.festivalProvider = festivalProvider;
        this.jwtService = jwtService;

    }
    //POST
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = REPEATABLE_READ , rollbackFor = Exception.class)
    public void postFestival(PostFestivalReq postFestivalReq) throws BaseException {
        try{
            festivalDao.postFestival(postFestivalReq);

        } catch (Exception exception) {
            System.out.println(exception);
            exception.getStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = REPEATABLE_READ , rollbackFor = Exception.class)
    public void postFestivalDibs(FestivalDibsReq festivalDibsReq) throws BaseException {
        User user = festivalDao.getUserByUserIdx(festivalDibsReq.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            if(festivalDao.checkFestivalDibs(festivalDibsReq) == 1){
                festivalDao.modifyFestivalDibs(festivalDibsReq);
            }else{
                festivalDao.postFestivalDibs(festivalDibsReq);
            }

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
