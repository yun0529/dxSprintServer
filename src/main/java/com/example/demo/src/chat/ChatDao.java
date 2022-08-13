package com.example.demo.src.chat;


import com.example.demo.src.chat.model.GetChattingRoomList;
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
                "Room.crewIdx, Room.roomIdx, MAIN_IMG_NORMAL, crewName, type, chatContent, Chat.updatedAt " +
                "from DXDB.Room " +
                "join DXDB.Member on Room.roomIdx = Member.roomIdx " +
                "join Crew on Member.crewIdx = Crew.crewIdx " +
                "left join DXDB.Chat on Room.roomIdx = Chat.roomIdx " +
                "where Member.userIdx = ? " +
                "group by Room.roomIdx";
        int getUserParams = userIdx;
        return this.jdbcTemplate.query(getFestivalsQuery,
                (rs,rowNum) -> new GetChattingRoomList(
                        rs.getInt("crewIdx"),
                        rs.getInt("roomIdx"),
                        rs.getString("MAIN_IMG_NORMAL"),
                        rs.getString("crewName"),
                        rs.getString("type"),
                        rs.getString("chatContent"),
                        rs.getString("updatedAt")), getUserParams
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

}
