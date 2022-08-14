package com.example.demo.src.chat;


import com.example.demo.src.chat.model.GetChattingRoomList;
import com.example.demo.src.chat.model.GetLastChat;
import com.example.demo.src.chat.model.GetMessageList;
import com.example.demo.src.chat.model.PostMessage;
import com.example.demo.src.crew.model.*;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetChattingRoomList> getChattingRoom(int userIdx){
        String getFestivalsQuery = "select " +
                "Room.crewIdx, Room.roomIdx, MAIN_IMG_NORMAL as festivalImageUrl, crewName, type " +
                "from DXDB.Room " +
                "join DXDB.Member on Room.roomIdx = Member.roomIdx " +
                "join Crew on Member.crewIdx = Crew.crewIdx " +
                "left join DXDB.Chat on Room.roomIdx = Chat.roomIdx " +
                "join Festival on Crew.festivalIdx = Festival.UC_SEQ " +
                "where Member.userIdx = ? " +
                "group by Room.roomIdx";
        int getUserParams = userIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetChattingRoomList(
                        rs.getInt("crewIdx"),
                        rs.getInt("roomIdx"),
                        rs.getString("festivalImageUrl"),
                        rs.getString("crewName"),
                        rs.getString("type"),
                        "",
                        ""), getUserParams
        );
    }

    public List<GetLastChat> getLastChat(int roomIdx){
        String getFestivalsQuery = "select chatContent, updatedAt from DXDB.Chat where roomIdx = ? order by Chat.createdAt desc";
        int getUserParams = roomIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetLastChat(
                        rs.getString("chatContent"),
                        rs.getString("updatedAt")), getUserParams
        );
    }

    public int postMessage(PostMessage postMessage){
        String insertCrewDibsQuery = "insert into Chat (roomIdx, userIdx, type, chatContent) VALUES (?,?,?,?)";
        Object[] insertAuthCodeParams = new Object[]{postMessage.getRoomIdx(), postMessage.getUserIdx(), postMessage.getType(), postMessage.getContent()};
        return this.jdbcTemplate.update(insertCrewDibsQuery,insertAuthCodeParams);
    }

    public int modifyCrewDibs(CrewDibsReq crewDibsReq){
        String modifyCrewDibsQuery = "update CrewDibs set status = ? where crewIdx = ? and userIdx = ?";
        Object[] modifyUserPwParams = new Object[]{crewDibsReq.getStatus(), crewDibsReq.getCrewIdx(), crewDibsReq.getUserIdx()};
        return this.jdbcTemplate.update(modifyCrewDibsQuery,modifyUserPwParams);
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
    public List<GetMessageList> getMessage(int userIdx){
        String getFestivalsQuery = "select " +
                "roomIdx, Chat.userIdx, userProfileImageUrl, userNickName, type, chatContent, Chat.updatedAt " +
                "from DXDB.Chat " +
                "join DXDB.User on Chat.userIdx = User.userIdx " +
                "where roomIdx = ? " +
                "order by updatedAt asc";
        int getUserParams = userIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetMessageList(
                        rs.getInt("roomIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("userProfileImageUrl"),
                        rs.getString("userNickName"),
                        rs.getString("type"),
                        rs.getString("chatContent"),
                        rs.getString("updatedAt")), getUserParams
        );
    }
}
