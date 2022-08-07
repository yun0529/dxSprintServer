package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.sql.DataSource;
import javax.transaction.Transactional;
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
        String getUsersByEmailQuery = "select * from User where userNo =?";
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
        String createUserQuery = "insert into User (userId, userPw, userRegionNo,userCode,userNickname) VALUES (?,?,?,?,?)";
        Random rand  = new Random();
        String randomSum= "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            randomSum+=ran;
        }
        int userCode = Integer.parseInt(randomSum);
        Object[] createUserParams = new Object[]{postUserReq.getUserId(), userCode, postUserReq.getUserRegionNo(),userCode,"당근 유저"};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkUserId(String userId){
        String checkUserIdQuery = "select exists(select userId from User where userId = ?)";
        String checkUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);
    }

    /*public int certificationUser(PostCertificationUserReq postCertificationUserReq){
        String certificationUserQuery = "select userNo from User where userId = ? ";
        String getUserIdParams = postCertificationUserReq.getUserId();
        return this.jdbcTemplate.queryForObject(certificationUserQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userNo"),
                        rs.getString("userId"),
                        rs.getString("userPw"),
                        rs.getString("userNickname"),
                        rs.getString("status")
                ),
                getUserIdParams
        );
    }*/

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set userNickname = ? where userNo = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserNickname(), patchUserReq.getUserNo()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userNo, userId, userPw, userNickname, status from User where userId = ?";
        String getPwdParams = postLoginReq.getUserId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userNo"),
                        rs.getString("userId"),
                        rs.getString("userPw"),
                        rs.getString("userNickname"),
                        rs.getString("status")
                ),
                getPwdParams
                );

    }
    public User getId(PostCertificationUserReq postCertificationUserReq){
        String getIdQuery = "select userNo, userId, userPw, userNickname, status from User where userId = ? ";
        System.out.println(postCertificationUserReq.getUserId());
        String getIdParams = postCertificationUserReq.getUserId();
        return this.jdbcTemplate.queryForObject(getIdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userNo"),
                        rs.getString("userId"),
                        rs.getString("userPw"),
                        rs.getString("userNickname"),
                        rs.getString("status")
                ),
                getIdParams);
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
    public int setPw(String userId,String pw){
        String serUserPwNameQuery = "update User set userPw = ? where userId = ? ";
        Object[] serUserPwParams = new Object[]{pw, userId};
        System.out.println(pw);
        return this.jdbcTemplate.update(serUserPwNameQuery,serUserPwParams);
    }
    public int modifyUserStatusLogIn(PostLoginReq postLoginReq){
        String modifyUserNameQuery = "update User set status = ? where userId = ? ";
        Random rand  = new Random();
        String randomSum= "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            randomSum+=ran;
        }
        int userCode = Integer.parseInt(randomSum);
        Object[] modifyUserNameParams = new Object[]{"Active", postLoginReq.getUserId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public List<GetInterestCategory> getInterestCategory(int userNo){
        String getGetMyCarrotByUserNoQuery = "select " +
                "userNo, " +
                "case " +
                "when (userInterestCategoryNo = 1) then '디지털기기' " +
                "when (userInterestCategoryNo = 2) then '생활가전' " +
                "when (userInterestCategoryNo = 3) then '가구/인테리어' " +
                "when (userInterestCategoryNo = 4) then '유아동' " +
                "when (userInterestCategoryNo = 5) then '생활/가공식품' " +
                "when (userInterestCategoryNo = 6) then '유아도서' " +
                "when (userInterestCategoryNo = 7) then '스포츠/레저' " +
                "when (userInterestCategoryNo = 8) then '여성잡화' " +
                "when (userInterestCategoryNo = 9) then '여성의류' " +
                "when (userInterestCategoryNo = 10) then '남성패션/잡화' " +
                "when (userInterestCategoryNo = 11) then '게임/취미' " +
                "when (userInterestCategoryNo = 12) then '뷰티/미용' " +
                "when (userInterestCategoryNo = 13) then '반려동물용품' " +
                "when (userInterestCategoryNo = 14) then '도서/티켓/음반' " +
                "when (userInterestCategoryNo = 15) then '식물' " +
                "when (userInterestCategoryNo = 16) then '기타 중고물품' " +
                "when (userInterestCategoryNo = 17) then '삽니다' " +
                "end as userInterestCategory, isCheck " +
                "from UserInterestCategory " +
                "where userNo = ?";
        int getInterestCategoryByUserNoParams = userNo;
        return this.jdbcTemplate.query(getGetMyCarrotByUserNoQuery,
                (rs, rowNum) -> new GetInterestCategory(
                        rs.getInt("userNo"),
                        rs.getString("userInterestCategory"),
                        rs.getString("isCheck")),
                getInterestCategoryByUserNoParams);
    }

    public int modifyInterestCategory(PatchInterestCategoryReq patchInterestCategoryReq){
        String modifyInterestCategoryQuery = "update UserInterestCategory set isCheck = ? where userNo = ? and userInterestCategoryNo = ?";
        Object[] modifyUserNameParams = new Object[]{
                patchInterestCategoryReq.getIsCheck(),
                patchInterestCategoryReq.getUserNo(),
                patchInterestCategoryReq.getInterestCategoryNo()};

        return this.jdbcTemplate.update(modifyInterestCategoryQuery,modifyUserNameParams);
    }

}
