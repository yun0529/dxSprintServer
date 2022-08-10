package com.example.demo.src.festival;


import com.example.demo.config.BaseException;
import com.example.demo.src.festival.model.GetFestivalSearch;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
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
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

//Provider : Read의 비즈니스 로직 처리
@Service
public class FestivalProvider {

    private final FestivalDao festivalDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FestivalProvider(FestivalDao festivalDao, JwtService jwtService) {
        this.festivalDao = festivalDao;
        this.jwtService = jwtService;
    }

    public List<GetFestivalSearch> getFestivalSearch(String festival) throws BaseException{
        try{
            List<GetFestivalSearch> getFestivalSearches = festivalDao.getFestivalSearches(festival);
            festivalDao.postPopularSearch(festival);
            return getFestivalSearches;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFestivalSearch> getFestivalSearchJwt(int userIdx, String festival) throws BaseException{
        try{
            List<GetFestivalSearch> getFestivalSearches = festivalDao.getFestivalSearches(festival);
            festivalDao.postRecentSearch(userIdx,festival);
            festivalDao.postPopularSearch(festival);
            return getFestivalSearches;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}