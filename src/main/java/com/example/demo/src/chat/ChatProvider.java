package com.example.demo.src.chat;


import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.GetChattingRoomList;
import com.example.demo.src.chat.model.GetMessageList;
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
public class ChatProvider {

    private final ChatDao chatDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChatProvider(ChatDao chatDao, JwtService jwtService) {
        this.chatDao = chatDao;
        this.jwtService = jwtService;
    }
    public List<GetChattingRoomList> getChattingRoom(int userIdx) throws BaseException{
        try{
            List<GetChattingRoomList> getChattingRoomLists = chatDao.getChattingRoom(userIdx);
            return getChattingRoomLists;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetMessageList> getMessage(int roomIdx) throws BaseException{
        try{
            List<GetMessageList> getMessageLists = chatDao.getMessage(roomIdx);
            return getMessageLists;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}