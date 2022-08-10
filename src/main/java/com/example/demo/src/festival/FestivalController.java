package com.example.demo.src.festival;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.crew.model.CrewDibsReq;
import com.example.demo.src.festival.model.FestivalDibsReq;
import com.example.demo.src.festival.model.GetFestivalSearch;
import com.example.demo.src.festival.model.PostFestivalReq;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexUserId;
import static com.example.demo.utils.ValidationRegex.isRegexUserPw;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@RestController
@RequestMapping("/festivals")
public class FestivalController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FestivalProvider festivalProvider;
    @Autowired
    private final FesivalService fesivalService;
    @Autowired
    private final JwtService jwtService;

    public FestivalController(FestivalProvider festivalProvider, FesivalService fesivalService, JwtService jwtService){
        this.festivalProvider = festivalProvider;
        this.fesivalService = fesivalService;
        this.jwtService = jwtService;
    }
    /**
     * 축제 저장 및 업데이트 API
     * [POST] /festival
     * @return BaseResponse<PostFestivalReq>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postFestival(@RequestBody PostFestivalReq postFestivalReq) {
        if(postFestivalReq.getMAIN_TITLE().length() > 300){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_MAIN_TITLE);
        }
        if(postFestivalReq.getGUGUN_NM().length() > 100){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_GUGUN_NM);
        }
        if(postFestivalReq.getPLACE().length() > 300){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_PLACE);
        }
        if(postFestivalReq.getTITLE().length() > 300){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_TITLE);
        }
        if(postFestivalReq.getSUBTITLE().length() > 300){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_SUBTITLE);
        }
        if(postFestivalReq.getMAIN_PLACE().length() > 100){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_MAIN_PLACE);
        }
        if(postFestivalReq.getADDR1().length() > 200){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_ADDR1);
        }
        if(postFestivalReq.getADDR2().length() > 200){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_ADDR2);
        }
        if(postFestivalReq.getCNTCT_TEL().length() > 200){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_CNTCT_TEL);
        }
        if(postFestivalReq.getHOMEPAGE_URL().length() > 200){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_HOMEPAGE_URL);
        }
        if(postFestivalReq.getTRFC_INFO().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_TRFC_INFO);
        }
        if(postFestivalReq.getUSAGE_DAY().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_USAGE_DAY);
        }
        if(postFestivalReq.getUSAGE_DAY_WEEK_AND_TIME().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_USAGE_DAY_WEEK_AND_TIME);
        }
        if(postFestivalReq.getUSAGE_AMOUNT().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_USAGE_AMOUNT);
        }
        if(postFestivalReq.getMAIN_IMG_NORMAL().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_MAIN_IMG_NORMAL);
        }
        if(postFestivalReq.getMAIN_IMG_THUMB().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_MAIN_IMG_THUMB);
        }
        if(postFestivalReq.getMIDDLE_SIZE_RM1().length() > 500){
            return new BaseResponse<>(POST_FESTIVALS_INVALID_MIDDLE_SIZE_RM1);
        }
        try{
            fesivalService.postFestival(postFestivalReq);
            String result = "등록 완료";

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 축제 검색 API
     * [GET] /festivals/search
     * @return BaseResponse<GetBadge>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/search") // (GET) 127.0.0.1:9000/festivals/search
    public BaseResponse<List<GetFestivalSearch>> getFestivalSearch(@RequestParam(required = false) String festival) {
        // Get Users
        try{
            List<GetFestivalSearch> getFestivalSearches;
            //비었을시
            if(jwtService.checkJwt() == true){
                getFestivalSearches = festivalProvider.getFestivalSearch(festival);
            }else{
                int userIdxByJwt = jwtService.getUserIdx();
                getFestivalSearches = festivalProvider.getFestivalSearchJwt(userIdxByJwt, festival);
            }
            //userIdx와 접근한 유저가 같은지 확인


            return new BaseResponse<>(getFestivalSearches);
        } catch(BaseException exception){
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 축제 좋아요 API
     * [POST] /festivals/dibs
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/dibs")
    public BaseResponse<String> getFestivalDibs(@RequestBody FestivalDibsReq festivalDibsReq) {
        if(!festivalDibsReq.getStatus().equals("Active") && !festivalDibsReq.getStatus().equals("Inactive")){
            return new BaseResponse<>(POST_INVALID_DIBS_STATUS);
        }
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(festivalDibsReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            fesivalService.postFestivalDibs(festivalDibsReq);
            String result;
            if(festivalDibsReq.getStatus().equals("Active")){
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