package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChattingRoomList;
import com.example.demo.src.crew.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ChatProvider chatProvider;
    @Autowired
    private final ChatService chatService;
    @Autowired
    private final JwtService jwtService;

    public ChatController(ChatProvider chatProvider, ChatService chatService, JwtService jwtService){
        this.chatProvider = chatProvider;
        this.chatService = chatService;
        this.jwtService = jwtService;
    }

    /**
     * 채팅방 조회 API
     * [GET] /chats/room/:userIdx
     * @return BaseResponse<List<GetChattingRoomList>>
     */
    // Body
    @ResponseBody
    @GetMapping("/room/{userIdx}")
    public BaseResponse<List<GetChattingRoomList>> getChattingRoom(@PathVariable("userIdx") int userIdx) {
        try{
            List<GetChattingRoomList> getChattingRoomLists = chatProvider.getChattingRoom(userIdx);
            return new BaseResponse<>(getChattingRoomLists);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}