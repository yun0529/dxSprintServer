package com.example.demo.src.crew;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.crew.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/crews")
public class CrewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CrewProvider crewProvider;
    @Autowired
    private final CrewService crewService;
    @Autowired
    private final JwtService jwtService;

    public CrewController(CrewProvider crewProvider, CrewService crewService, JwtService jwtService){
        this.crewProvider = crewProvider;
        this.crewService = crewService;
        this.jwtService = jwtService;
    }
    /**
     * 크루 생성 API
     * [POST] /crews
     * @return BaseResponse<PostFestivalReq>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postCrew(@RequestBody PostCrewReq postCrewReq) {
        if(postCrewReq.getFestivalTitle().length() > 300){
            return new BaseResponse<>(POST_CREWS_INVALID_MAIN_TITLE);
        }
        if(postCrewReq.getCrewName().length() > 300){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_NAME);
        }
        if(postCrewReq.getCrewComment().length() > 500){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_COMMENT);
        }
        if(postCrewReq.getCrewHeadCount() > 100){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_HEAD_COUNT);
        }
        if(postCrewReq.getCrewMeetDate().length() > 45){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_MEET_DATE);
        }
        if(postCrewReq.getCrewMeetTime().length() > 45){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_MEET_TIME);
        }
        if(postCrewReq.getCrewGender().length() > 3){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_GENDER);
        }
        if(postCrewReq.getCrewMinAge() < 0){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_MIN_TAGE);
        }
        if(postCrewReq.getCrewMeetTime().length() > 100){
            return new BaseResponse<>(POST_CREWS_INVALID_CREW_MAX_AGE);
        }
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postCrewReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            crewService.postCrew(postCrewReq);
            String result = "등록 완료";

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 크루 참가 API
     * [POST] /crews/participate
     * @return BaseResponse<PostFestivalReq>
     */
    // Body
    @ResponseBody
    @PostMapping("/participate")
    public BaseResponse<String> postCrewParticipate(@RequestBody PostCrewParticipateReq postCrewParticipateReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postCrewParticipateReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            crewService.postCrewParticipate(postCrewParticipateReq);
            String result = "크루 참여 완료";

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 크루 조회 API
     * [GET] /crews
     * @return BaseResponse<PostFestivalReq>
     */
    // Body
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCrews>> getCrews(@RequestParam(required = false, defaultValue = "0") int crewIdx) {
        try{
            List<GetCrews> getCrews;
            if(crewIdx == 0){
                getCrews = crewProvider.getCrews();
            }else{
                getCrews = crewProvider.getCrews(crewIdx);
            }
            return new BaseResponse<>(getCrews);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 크루 상세 조회 API
     * [GET] /crews/detail
     * @return BaseResponse<GetDetailCrews>
     */
    // Body
    @ResponseBody
    @GetMapping("/detail")
    public BaseResponse<GetDetailCrews> getDetailCrews(@RequestParam(required = false) int crewIdx) {
        try{
            GetDetailCrews getDetailCrews = crewProvider.getDetailCrews(crewIdx);

            return new BaseResponse<>(getDetailCrews);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 크루 좋아요 API
     * [POST] /crews/dibs
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/dibs")
    public BaseResponse<String> getCrewDibs(@RequestBody CrewDibsReq crewDibsReq) {
        if(!crewDibsReq.getStatus().equals("Active") && !crewDibsReq.getStatus().equals("Inactive")){
            return new BaseResponse<>(POST_INVALID_DIBS_STATUS);
        }
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(crewDibsReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            crewService.postCrewDibs(crewDibsReq);
            String result;
            if(crewDibsReq.getStatus().equals("Active")){
                result = "좋아요 등록 성공";
            }else{
                result = "좋아요 취소 성공";
            }

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}