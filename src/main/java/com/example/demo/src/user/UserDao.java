package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                    rs.getInt("userNo"),
                    rs.getString("userImageUrl"),
                    rs.getString("userNickname"),
                    rs.getInt("userCode"),
                    rs.getFloat("userManner"),
                    rs.getString("userRedealRate"),
                    rs.getString("userResponseRate"),
                    rs.getString("createdDate"),
                    rs.getString("updatedDate"),
                    rs.getString("status"),
                    rs.getString("userRegionNo"),
                    rs.getInt("userRegionCertificationNum"),
                    rs.getString("userSubRegion"),
                    rs.getInt("userSubRegionCertification"),
                    rs.getString("UserMainRegionUpdatedDate"))
                );
    }

    public List<GetUserRes> getUsersByUserNo(int userNo){
        String getUsersByEmailQuery = "select * from User where userNo = ?";
        int getUsersByEmailParams = userNo;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("userNickname"),
                        rs.getInt("userCode"),
                        rs.getFloat("userManner"),
                        rs.getString("userRedealRate"),
                        rs.getString("userResponseRate"),
                        rs.getString("createdDate"),
                        rs.getString("updatedDate"),
                        rs.getString("status"),
                        rs.getString("userRegionNo"),
                        rs.getInt("userRegionCertificationNum"),
                        rs.getString("userSubRegion"),
                        rs.getInt("userSubRegionCertification"),
                        rs.getString("UserMainRegionUpdatedDate")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userNo){
        String getUserQuery = "select userNo,userImageUrl,userNickname,userCode,userManner,userRedealRate,userResponseRate, " +
        "date_format(User.createdAt,'%Y년 %m월 %d일 가입') as createdAt, "+
        "case when (year(User.updatedAt) = year(now()) and dayofyear(now())- dayofyear(User.updatedAt) <= 3) then '최근 3일 이내 활동' " +
        "when (year(now()) - year(User.updatedAt) = 1) then '최근 1년 이내 활동' " +
        "when (year(now()) - year(User.updatedAt) <= 5) then '최근 5년 이내 활동' " +
        "end as updatedAt, User.status, "+
        "case " +
            "when (userRegionNo = '1') then '가좌동' " +
            "when (userRegionNo = '2') then '가호동' " +
            "when (userRegionNo = '3') then '호탄동' " +
            "when (userRegionNo = '4') then null " +
            "when (userRegionNo = '10') then '신사동' " +
            "end as userRegionNo " +
            ", userRegionCertificationNum, " +
        "case " +
            "when (User.userSubRegionNo = '1') then '가좌동' " +
            "when (User.userSubRegionNo = '2') then '가호동' " +
            "when (User.userSubRegionNo = '3') then '호탄동' " +
            "when (User.userSubRegionNo = '4') then null " +
            "when (User.userSubRegionNo = '10') then '신사동' " +
            "end as userSubRegionNo " +
            ", userSubRegionCertificationNum, " +
        "case " +
            "when (year(UserMainRegion.updatedAt) = year(now()) and dayofyear(now())- dayofyear(UserMainRegion.updatedAt) <= 30 and userRegionCertificationNum >= 1) then '최근 30일' " +
            "when (year(now()) - year(UserMainRegion.updatedAt) = 1) then '최근 1년 이내' " +
            "else '미인증' end as UserMainRegionUpdatedDate " +
        "from User "+
        "join UserMainRegion on User.userRegionNo = UserMainRegion.userMainRegionNo " +
        "join UserSubRegion on UserMainRegion.userSubRegionNo = UserSubRegion.userSubRegionNo " +
        "where User.userNo = ?";
        int getUserParams = userNo;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("userNickname"),
                        rs.getInt("userCode"),
                        rs.getFloat("userManner"),
                        rs.getString("userRedealRate"),
                        rs.getString("userResponseRate"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        rs.getString("status"),
                        rs.getString("userRegionNo"),
                        rs.getInt("userRegionCertificationNum"),
                        rs.getString("userSubRegionNo"),
                        rs.getInt("userSubRegionCertificationNum"),
                        rs.getString("UserMainRegionUpdatedDate"))
                        ,
                getUserParams);
    }

    public List<GetBadge> getBadgeByUserNo(int userNo){
        String getGetMyCarrotByUserNoQuery = "select " +
                "User.userNo, badgeName " +
                "from UserActivityBadge " +
                "join User on UserActivityBadge.userNo = User.userNo and userActivityBadgeNo != 0 " +
                "join BadgeName on UserActivityBadgeNo = badgeNameNo " +
                "where User.userNo = ? ";
        int getBadgeByUserNoParams = userNo;
        return this.jdbcTemplate.query(getGetMyCarrotByUserNoQuery,
                (rs, rowNum) -> new GetBadge(
                        rs.getInt("userNo"),
                        rs.getString("badgeName")),
                        getBadgeByUserNoParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (userEmail, userPw, userPhoneNumber,userNickName,userProfileImageUrl) VALUES (?,?,?,?,?)";

        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassWord(), postUserReq.getPhoneNumber(), postUserReq.getNickName(),
                "https://firebasestorage.googleapis.com/v0/b/risingtest-11264.appspot.com/o/images%2F%EC%95%84%EC%9D%B4%EB%94%94%EC%96%B4%EC%8A%A4%20%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.png?alt=media&token=8f45a074-b948-4f70-8b0a-4a70ae83b585"};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkUserEmail(String userEmail){
        String checkUserEmailQuery = "select exists(select userEmail from User where userEmail = ?)";
        String checkUserEmailParams = userEmail;
        return this.jdbcTemplate.queryForObject(checkUserEmailQuery,
                int.class,
                checkUserEmailParams);
    }

    public int checkUserPhoneNumber(String userPhoneNumber){
        String checkUserPhoneNumberQuery = "select exists(select userPhoneNumber from User where userPhoneNumber = ?)";
        String checkUserPhoneNumberParams = userPhoneNumber;
        return this.jdbcTemplate.queryForObject(checkUserPhoneNumberQuery,
                int.class,
                checkUserPhoneNumberParams);
    }

    public int checkUserNickName(String userNickName){
        String checkUserNickNameQuery = "select exists(select userNickName from User where userNickName = ?)";
        String checkUserNickNameParams = userNickName;
        return this.jdbcTemplate.queryForObject(checkUserNickNameQuery,
                int.class,
                checkUserNickNameParams);
    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set userNickname = ? where userNo = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserNickname(), patchUserReq.getUserNo()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getUserByEmail(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, userEmail, userPw, userNickName, status from User where userEmail = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("userEmail"),
                        rs.getString("userPw"),
                        rs.getString("userNickName"),
                        rs.getString("status")),
                getPwdParams
                );

    }
    public User getNo(int userNo){
        String getIdQuery = "select userNo, userId, userPw, userNickname, status from User where userNo = ? ";
        int getNoParams = userNo;
        return this.jdbcTemplate.queryForObject(getIdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userNo"),
                        rs.getString("userId"),
                        rs.getString("userPw"),
                        rs.getString("userNickname"),
                        rs.getString("status")
                ),
                getNoParams);
    }
    public int postAuthCode(String userPhoneNumber,String authCode){
        String insertAuthCodeQuery = "insert into AuthCode (authCode, userPhoneNumber) VALUES (?,?)";
        Object[] insertAuthCodeParams = new Object[]{authCode, userPhoneNumber};
        return this.jdbcTemplate.update(insertAuthCodeQuery,insertAuthCodeParams);
    }
    public int patchAuthCode(String userPhoneNumber,String authCode){
        String modifyUserPwNameQuery = "update AuthCode set authCode = ? where userPhoneNumber = ? ";
        Object[] modifyUserPwParams = new Object[]{authCode, userPhoneNumber};
        return this.jdbcTemplate.update(modifyUserPwNameQuery,modifyUserPwParams);
    }
    public int checkAuthPhoneNumber(String userPhoneNumber){
        String checkUserNickNameQuery = "select exists(select userPhoneNumber from AuthCode where userPhoneNumber = ?)";
        String checkUserNickNameParams = userPhoneNumber;
        return this.jdbcTemplate.queryForObject(checkUserNickNameQuery,
                int.class,
                checkUserNickNameParams);
    }
    public int modifyUserStatusLogIn(PostLoginReq postLoginReq){
        String modifyUserNameQuery = "update User set status = ? where userEmail = ? ";
        Object[] modifyUserNameParams = new Object[]{"Active", postLoginReq.getEmail()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

}
