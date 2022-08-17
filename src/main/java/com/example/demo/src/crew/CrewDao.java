package com.example.demo.src.crew;


import com.example.demo.src.crew.model.*;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CrewDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int postCrew(PostCrewReq postCrewReq){
        String insertAuthCodeQuery = "insert into Crew (festivalIdx, userIdx, festivalTitle, crewName, crewComment, crewHeadCount, crewMeetDate, " +
                "crewMeetTime, crewGender, crewMinAge, crewMaxAge) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        Object[] insertAuthCodeParams = new Object[]{postCrewReq.getUC_SEQ(), postCrewReq.getUserIdx(), postCrewReq.getFestivalTitle(), postCrewReq.getCrewName(),
                postCrewReq.getCrewComment(), postCrewReq.getCrewHeadCount(), postCrewReq.getCrewMeetDate(), postCrewReq.getCrewMeetTime(),postCrewReq.getCrewGender(),
        postCrewReq.getCrewMinAge(), postCrewReq.getCrewMaxAge()};
        this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int postRoom(int crewIdx){
        String insertAuthCodeQuery = "insert into Room (crewIdx) VALUES (?)";
        Object[] insertAuthCodeParams = new Object[]{crewIdx};
        this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int postCrewParticipate(PostCrewParticipateReq postCrewParticipateReq){
        String insertAuthCodeQuery = "insert into Member (crewIdx, roomIdx, userIdx) VALUES (?,?,?)";
        Object[] insertAuthCodeParams = new Object[]{postCrewParticipateReq.getCrewIdx(), postCrewParticipateReq.getRoomIdx(), postCrewParticipateReq.getUserIdx()};
        this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public GetRoomIdx getRoomIdxByCrewUser(int userIdx, int crewIdx){
        String getIdQuery = "select roomIdx, updatedAt from Member where userIdx = ? and crewIdx = ?";
        int getUserIdxParams = userIdx;
        int getCrewIdxParams = crewIdx;
        return this.jdbcTemplate.queryForObject(getIdQuery,
                (rs,rowNum)-> new GetRoomIdx(
                        rs.getInt("roomIdx"),
                        rs.getString("updatedAt")
                ), getUserIdxParams, getCrewIdxParams);
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
    public CheckCrewHeadCount getCrewHeadCount(int crewIdx){
        String getIdQuery = "select count(Member.userIdx) as participateCount, crewHeadCount as totalHeadCount " +
                "from DXDB.Member " +
                "join Crew on Member.crewIdx = Crew.crewIdx " +
                "where Member.crewIdx = ?";
        int getIdxParams = crewIdx;
        return this.jdbcTemplate.queryForObject(getIdQuery,
                (rs,rowNum)-> new CheckCrewHeadCount(
                        rs.getInt("participateCount"),
                        rs.getInt("totalHeadCount")
                ), getIdxParams);
    }
    public List<GetCrews> getCrews(){
        String getFestivalsQuery = "select " +
                "crewIdx, festivalIdx, MAIN_IMG_NORMAL as festivalImageUrl, MAIN_TITLE as title, crewName, crewGender, " +
                "(select count(case when (Member.crewIdx = Crew.crewIdx) then 1 end) from DXDB.Member) + 1 as crewHeadCount, " +
                "crewHeadCount as totalHeadCount, crewMeetDate," +
                "(select count(case when (CrewDibs.status = 'Active') then 1 end) from CrewDibs where CrewDibs.crewIdx = Crew.crewIdx) as dibsCount "  +
                "from Crew " +
                "left join Festival on Crew.festivalIdx = Festival.UC_SEQ ";
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetCrews(
                        rs.getInt("crewIdx"),
                        rs.getInt("festivalIdx"),
                        rs.getString("festivalImageUrl"),
                        rs.getString("title"),
                        rs.getString("crewName"),
                        rs.getString("crewGender"),
                        rs.getInt("crewHeadCount"),
                        rs.getInt("totalHeadCount"),
                        rs.getString("crewMeetDate"),
                        rs.getInt("dibsCount"))
        );
    }
    public List<GetCrews> getCrewsByCrewIdx(int crewIdx){
        String getFestivalsQuery = "select " +
                "crewIdx, festivalIdx, MAIN_IMG_NORMAL as festivalImageUrl, MAIN_TITLE as title, crewName, crewGender, " +
                "(select count(case when (Member.crewIdx = ?) then 1 end) from DXDB.Member) + 1 as crewHeadCount, " +
                "crewHeadCount as totalHeadCount, crewMeetDate, "  +
                "(select count(case when (CrewDibs.status = 'Active') then 1 end) from CrewDibs where CrewDibs.crewIdx = Crew.crewIdx) as dibsCount "  +
                "from Crew " +
                "left join Festival on Crew.festivalIdx = Festival.UC_SEQ " +
                "where crewIdx = ?";
        int getCrewParams = crewIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetCrews(
                        rs.getInt("crewIdx"),
                        rs.getInt("festivalIdx"),
                        rs.getString("festivalImageUrl"),
                        rs.getString("title"),
                        rs.getString("crewName"),
                        rs.getString("crewGender"),
                        rs.getInt("crewHeadCount"),
                        rs.getInt("totalHeadCount"),
                        rs.getString("crewMeetDate"),
                        rs.getInt("dibsCount")), getCrewParams, getCrewParams
        );
    }

    public GetDetailCrews getDetailCrews(int crewIdx){
        String getFestivalsQuery = "select " +
                "Crew.userIdx, userProfileImageUrl,crewHeadCount as totalHeadCount, crewMeetDate, crewMeetTime, " +
                "crewGender, crewMinAge, crewMaxAge, crewComment, " +
                "(select count(case when (Member.crewIdx = ?) then 1 end) from DXDB.Member) + 1 as crewHeadCount,  " +
                "(select count(case when (CrewDibs.status = 'Active') then 1 end) from CrewDibs where CrewDibs.crewIdx = Crew.crewIdx) as dibsCount "  +
                "from DXDB.User " +
                "join Crew on User.userIdx = Crew.userIdx " +
                "where Crew.crewIdx = ?";
        int getCrewParams = crewIdx;
        return this.jdbcTemplate.queryForObject(getFestivalsQuery,
                (rs,rowNum) -> new GetDetailCrews(
                        rs.getInt("userIdx"),
                        rs.getString("userProfileImageUrl"),
                        rs.getInt("totalHeadCount"),
                        rs.getString("crewMeetDate"),
                        rs.getString("crewMeetTime"),
                        rs.getString("crewGender"),
                        rs.getInt("crewMinAge"),
                        rs.getInt("crewMaxAge"),
                        rs.getString("crewComment"),
                        rs.getInt("crewHeadCount"),
                        rs.getInt("dibsCount"), getParticipateUser(getCrewParams)), getCrewParams, getCrewParams
        );
    }

    public List<Member> getParticipateUser(int crewIdx){
        String getFestivalsQuery = "select Member.userIdx, userProfileImageUrl, userNickName " +
                "from DXDB.Member " +
                "join DXDB.User on Member.userIdx = User.userIdx " +
                "where crewIdx = ?";
        int getCrewParams = crewIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new Member(
                        rs.getInt("userIdx"),
                        rs.getString("userProfileImageUrl"),
                        rs.getString("userNickName")
                        ), getCrewParams
        );
    }

    public List<GetParticipateCrew> getParticipateCrews(int userIdx){
        String getFestivalsQuery = "select " +
                "festivalIdx, MAIN_IMG_NORMAL as festivalImageUrl, MAIN_TITLE as title, crewName, crewGender, " +
                "(select count(case when (Member.crewIdx = Crew.crewIdx) then 1 end) from DXDB.Member) + 1 as crewHeadCount, " +
                "crewHeadCount as totalHeadCount, crewMeetDate " +
                "from DXDB.Member " +
                "left join DXDB.Crew on Member.crewIdx = Crew.crewIdx " +
                "left join Festival on Crew.festivalIdx = Festival.UC_SEQ " +
                "where Member.userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetParticipateCrew(
                        rs.getInt("festivalIdx"),
                        rs.getString("festivalImageUrl"),
                        rs.getString("title"),
                        rs.getString("crewName"),
                        rs.getString("crewGender"),
                        rs.getInt("crewHeadCount"),
                        rs.getInt("totalHeadCount"),
                        rs.getString("crewMeetDate")), getUserParams
        );
    }

    public int postCrewDibs(CrewDibsReq crewDibsReq){
        String insertCrewDibsQuery = "insert into CrewDibs (crewIdx, userIdx, status) VALUES (?,?,?)";
        Object[] insertAuthCodeParams = new Object[]{crewDibsReq.getCrewIdx(), crewDibsReq.getUserIdx(), crewDibsReq.getStatus()};
        return this.jdbcTemplate.update(insertCrewDibsQuery,insertAuthCodeParams);
    }

    public int modifyCrewDibs(CrewDibsReq crewDibsReq){
        String modifyCrewDibsQuery = "update CrewDibs set status = ? where crewIdx = ? and userIdx = ?";
        Object[] modifyUserPwParams = new Object[]{crewDibsReq.getStatus(), crewDibsReq.getCrewIdx(), crewDibsReq.getUserIdx()};
        return this.jdbcTemplate.update(modifyCrewDibsQuery,modifyUserPwParams);
    }

    public int checkCrewDibs(CrewDibsReq crewDibsReq){
        String checkUserNickNameQuery = "select exists(select crewDibsIdx from CrewDibs where crewIdx = ? and userIdx = ?)";
        int checkCrewIdxParams = crewDibsReq.getCrewIdx();
        int checkUserIdxParams = crewDibsReq.getUserIdx();
        return this.jdbcTemplate.queryForObject(checkUserNickNameQuery,
                int.class,
                checkCrewIdxParams, checkUserIdxParams);
    }
    public List<GetCrews> getCrewsByFestivalIdx(int festivalIdx){
        String getFestivalsQuery = "select " +
                "crewIdx, festivalIdx, MAIN_IMG_NORMAL as festivalImageUrl, MAIN_TITLE as title, crewName, crewGender, " +
                "(select count(case when (Member.crewIdx = Crew.crewIdx) then 1 end) from DXDB.Member) + 1 as crewHeadCount, " +
                "crewHeadCount as totalHeadCount, crewMeetDate, " +
                "(select count(case when (CrewDibs.status = 'Active') then 1 end) from CrewDibs where CrewDibs.crewIdx = Crew.crewIdx) as dibsCount " +
                "from Crew " +
                "left join Festival on Crew.festivalIdx = Festival.UC_SEQ " +
                "where festivalIdx = ?";
        int getFestivalParams = festivalIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetCrews(
                        rs.getInt("crewIdx"),
                        rs.getInt("festivalIdx"),
                        rs.getString("festivalImageUrl"),
                        rs.getString("title"),
                        rs.getString("crewName"),
                        rs.getString("crewGender"),
                        rs.getInt("crewHeadCount"),
                        rs.getInt("totalHeadCount"),
                        rs.getString("crewMeetDate"),
                        rs.getInt("dibsCount")), getFestivalParams
        );
    }
}
