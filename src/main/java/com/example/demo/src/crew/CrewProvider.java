package com.example.demo.src.crew;


import com.example.demo.config.BaseException;
import com.example.demo.src.crew.model.GetCrews;
import com.example.demo.src.crew.model.GetDetailCrews;
import com.example.demo.src.crew.model.GetParticipateCrew;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class CrewProvider {

    private final CrewDao crewDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CrewProvider(CrewDao crewDao, JwtService jwtService) {
        this.crewDao = crewDao;
        this.jwtService = jwtService;
    }
    public GetDetailCrews getDetailCrews(int crewIdx) throws BaseException{
        try{
            GetDetailCrews getFestivalSearches = crewDao.getDetailCrews(crewIdx);
            return getFestivalSearches;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetCrews> getCrews(int crewIdx) throws BaseException{
        try{
            List<GetCrews> getCrews = crewDao.getCrewsByCrewIdx(crewIdx);
            return getCrews;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCrews> getCrews() throws BaseException{
        try{
            List<GetCrews> getCrews = crewDao.getCrews();
            return getCrews;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetParticipateCrew> getParticipateCrew(int userIdx) throws BaseException{
        try{
            List<GetParticipateCrew> getParticipateCrews = crewDao.getParticipateCrews(userIdx);
            return getParticipateCrews;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}