package com.example.demo.src.chat;


import com.example.demo.config.BaseException;
import com.example.demo.src.crew.model.CheckCrewHeadCount;
import com.example.demo.src.crew.model.CrewDibsReq;
import com.example.demo.src.crew.model.PostCrewParticipateReq;
import com.example.demo.src.crew.model.PostCrewReq;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ChatService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatDao chatDao;
    private final ChatProvider chatProvider;
    private final JwtService jwtService;


    @Autowired
    public ChatService(ChatDao chatDao, ChatProvider chatProvider, JwtService jwtService) {
        this.chatDao = chatDao;
        this.chatProvider = chatProvider;
        this.jwtService = jwtService;

    }
    //POST
    //@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = REPEATABLE_READ , rollbackFor = Exception.class)

}
