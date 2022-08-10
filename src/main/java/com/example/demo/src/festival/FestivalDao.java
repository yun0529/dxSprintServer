package com.example.demo.src.festival;


import com.example.demo.src.crew.model.CrewDibsReq;
import com.example.demo.src.festival.model.FestivalDibsReq;
import com.example.demo.src.festival.model.GetFestivalSearch;
import com.example.demo.src.festival.model.PostFestivalReq;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

@Repository
public class FestivalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int postFestival(PostFestivalReq postFestivalReq){
        String insertAuthCodeQuery = "insert into Festival (UC_SEQ, MAIN_TITLE, GUGUN_NM, LAT, LNG, PLACE, " +
                "TITLE, SUBTITLE, MAIN_PLACE, ADDR1, ADDR2, CNTCT_TEL, HOMEPAGE_URL, TRFC_INFO, " +
                "USAGE_DAY, USAGE_DAY_WEEK_AND_TIME, USAGE_AMOUNT, MAIN_IMG_NORMAL, MAIN_IMG_THUMB, ITEMCNTNTS, MIDDLE_SIZE_RM1) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] insertAuthCodeParams = new Object[]{postFestivalReq.getUC_SEQ(), postFestivalReq.getMAIN_TITLE(), postFestivalReq.getGUGUN_NM(), postFestivalReq.getLAT(),
                postFestivalReq.getLNG(), postFestivalReq.getPLACE(), postFestivalReq.getTITLE(), postFestivalReq.getSUBTITLE(), postFestivalReq.getMAIN_PLACE(),
                postFestivalReq.getADDR1(), postFestivalReq.getADDR2(), postFestivalReq.getCNTCT_TEL(), postFestivalReq.getHOMEPAGE_URL(), postFestivalReq.getTRFC_INFO(),
                postFestivalReq.getUSAGE_DAY(), postFestivalReq.getUSAGE_DAY_WEEK_AND_TIME(), postFestivalReq.getUSAGE_AMOUNT(), postFestivalReq.getMAIN_IMG_NORMAL(),
                postFestivalReq.getMAIN_IMG_THUMB(), postFestivalReq.getITEMCNTNTS(), postFestivalReq.getMIDDLE_SIZE_RM1()};
        return this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
    }

    public List<GetFestivalSearch> getFestivalSearches(String festival){
        String getFestivalsQuery = "select UC_SEQ,MAIN_TITLE,GUGUN_NM,LAT,LNG,PLACE,TITLE,SUBTITLE,MAIN_PLACE,ADDR1,ADDR2,CNTCT_TEL,HOMEPAGE_URL," +
                "TRFC_INFO,USAGE_DAY,USAGE_DAY_WEEK_AND_TIME,USAGE_AMOUNT,MAIN_IMG_NORMAL,MAIN_IMG_THUMB,ITEMCNTNTS,MIDDLE_SIZE_RM1, " +
                "(select count(case when (FestivalDibs.status = 'Active') then 1 end) from FestivalDibs where festivalIdx = UC_SEQ) as dibsCount " +
                "from Festival where Festival.MAIN_TITLE like concat('%',?,'%')";
        String getFestivalParams = festival;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetFestivalSearch(
                        rs.getInt("UC_SEQ"),
                        rs.getString("MAIN_TITLE"),
                        rs.getString("GUGUN_NM"),
                        rs.getDouble("LAT"),
                        rs.getDouble("LNG"),
                        rs.getString("PLACE"),
                        rs.getString("TITLE"),
                        rs.getString("SUBTITLE"),
                        rs.getString("MAIN_PLACE"),
                        rs.getString("ADDR1"),
                        rs.getString("ADDR2"),
                        rs.getString("CNTCT_TEL"),
                        rs.getString("HOMEPAGE_URL"),
                        rs.getString("TRFC_INFO"),
                        rs.getString("USAGE_DAY"),
                        rs.getString("USAGE_DAY_WEEK_AND_TIME"),
                        rs.getString("USAGE_AMOUNT"),
                        rs.getString("MAIN_IMG_NORMAL"),
                        rs.getString("MAIN_IMG_THUMB"),
                        rs.getString("ITEMCNTNTS"),
                        rs.getString("MIDDLE_SIZE_RM1"),
                        rs.getInt("dibsCount")), getFestivalParams
        );
    }
    public int postRecentSearch(int userIdx, String word){
        String insertAuthCodeQuery = "insert into RecentSearch (userIdx, recentSearchWord) VALUES (?,?)";
        Object[] insertAuthCodeParams = new Object[]{userIdx, word};
        return this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
    }
    public int postPopularSearch(String word){
        String insertAuthCodeQuery = "insert into PopularSearch (popularSearchWord) VALUES (?)";
        Object[] insertAuthCodeParams = new Object[]{word};
        return this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
    }
    public int postFestivalDibs(FestivalDibsReq festivalDibsReq){
        String insertCrewDibsQuery = "insert into FestivalDibs (festivalIdx, userIdx, status) VALUES (?,?,?)";
        Object[] insertAuthCodeParams = new Object[]{festivalDibsReq.getFestivalIdx(), festivalDibsReq.getUserIdx(), festivalDibsReq.getStatus()};
        return this.jdbcTemplate.update(insertCrewDibsQuery,insertAuthCodeParams);
    }

    public int modifyFestivalDibs(FestivalDibsReq festivalDibsReq){
        String modifyCrewDibsQuery = "update FestivalDibs set status = ? where festivalIdx = ? and userIdx = ?";
        Object[] modifyUserPwParams = new Object[]{festivalDibsReq.getStatus(), festivalDibsReq.getFestivalIdx(), festivalDibsReq.getUserIdx()};
        return this.jdbcTemplate.update(modifyCrewDibsQuery,modifyUserPwParams);
    }

    public int checkFestivalDibs(FestivalDibsReq festivalDibsReq){
        String checkUserNickNameQuery = "select exists(select festivalDibsIdx from FestivalDibs where festivalIdx = ? and userIdx = ?)";
        int checkCrewIdxParams = festivalDibsReq.getFestivalIdx();
        int checkUserIdxParams = festivalDibsReq.getUserIdx();
        return this.jdbcTemplate.queryForObject(checkUserNickNameQuery,
                int.class,
                checkCrewIdxParams, checkUserIdxParams);
    }
    public User getUserByUserIdx(int userIdx){
        String getIdQuery = "select userIdx, userEmail, userPw, userNickName, status from User where userIdx = ? ";
        int getIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getIdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("userEmail"),
                        rs.getString("userPw"),
                        rs.getString("userNickName"),
                        rs.getString("status")
                ),
                getIdxParams);
    }
}
