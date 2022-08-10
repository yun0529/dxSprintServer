package com.example.demo.src.crew;


import com.example.demo.config.BaseException;
import com.example.demo.src.crew.model.CheckCrewHeadCount;
import com.example.demo.src.crew.model.CrewDibsReq;
import com.example.demo.src.crew.model.PostCrewParticipateReq;
import com.example.demo.src.crew.model.PostCrewReq;
import com.example.demo.src.festival.model.PostFestivalReq;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CrewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CrewDao crewDao;
    private final CrewProvider crewProvider;
    private final JwtService jwtService;


    @Autowired
    public CrewService(CrewDao crewDao, CrewProvider crewProvider, JwtService jwtService) {
        this.crewDao = crewDao;
        this.crewProvider = crewProvider;
        this.jwtService = jwtService;

    }
    //POST
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = REPEATABLE_READ , rollbackFor = Exception.class)
    public void postCrew(PostCrewReq postCrewReq) throws BaseException {
        User user = crewDao.getUserByUserIdx(postCrewReq.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            int crewIdx = crewDao.postCrew(postCrewReq);

            crewDao.postRoom(crewIdx);

        } catch (Exception exception) {
            System.out.println(exception);
            exception.getStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = SERIALIZABLE , rollbackFor = Exception.class)
    public void postCrewParticipate(PostCrewParticipateReq postCrewParticipateReq) throws BaseException {
        User user = crewDao.getUserByUserIdx(postCrewParticipateReq.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        CheckCrewHeadCount checkCrewHeadCount = crewDao.getCrewHeadCount(postCrewParticipateReq.getCrewIdx());
        if(checkCrewHeadCount.getHeadCount() >= checkCrewHeadCount.getTotalHeadCount()){
            throw new BaseException(ALREADY_FULL_COUNT);
        }
        try{
            crewDao.postCrewParticipate(postCrewParticipateReq);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = REPEATABLE_READ , rollbackFor = Exception.class)
    public void postCrewDibs(CrewDibsReq crewDibsReq) throws BaseException {
        User user = crewDao.getUserByUserIdx(crewDibsReq.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            if(crewDao.checkCrewDibs(crewDibsReq) == 1){
                crewDao.modifyCrewDibs(crewDibsReq);
            }else{
                crewDao.postCrewDibs(crewDibsReq);
            }

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
