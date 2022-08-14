package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChattingParticipateUser;
import com.example.demo.src.chat.model.GetChattingRoomList;
import com.example.demo.src.chat.model.GetMessageList;
import com.example.demo.src.chat.model.PostMessage;
import com.example.demo.src.crew.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetChattingRoomList> getChattingRoomLists = chatProvider.getChattingRoom(userIdx);
            return new BaseResponse<>(getChattingRoomLists);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 채팅 보내기 API
     * [POST] /chats/message
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/message")
    public BaseResponse<String> getCrewDibs(@RequestBody PostMessage postMessage) {
        if(postMessage.getContent().length() > 5000){
            return new BaseResponse<>(POST_INVALID_MESSAGE_LENGTH);
        }
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postMessage.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            chatService.postMessage(postMessage);
            String result = "채팅 보내기 성공";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 채팅방 조회 API
     * [GET] /chats/message/:roomIdx
     * @return BaseResponse<List<GetChattingRoomList>>
     */
    // Body
    @ResponseBody
    @GetMapping("/message/{roomIdx}")
    public BaseResponse<List<GetMessageList>> getMessage(@PathVariable("roomIdx") int roomIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdxByJwt != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetMessageList> getMessageLists = chatProvider.getMessage(roomIdx, userIdxByJwt);
            return new BaseResponse<>(getMessageLists);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 채팅방 참여 인원 조회 API
     * [GET] /chats/message/:roomIdx
     * @return BaseResponse<List<GetChattingRoomList>>
     */
    // Body
    @ResponseBody
    @GetMapping("/user/{roomIdx}")
    public BaseResponse<List<GetChattingParticipateUser>> getChattingParticipateUser(@PathVariable("roomIdx") int roomIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdxByJwt != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetChattingParticipateUser> getChattingParticipateUsers = chatProvider.getChattingParticipateUser(roomIdx, userIdxByJwt);
            return new BaseResponse<>(getChattingParticipateUsers);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}