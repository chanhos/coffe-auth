package com.coffepotato.controller;

import com.coffepotato.common.component.JWTCore;
import com.coffepotato.common.enumeration.Errorcode;
import com.coffepotato.services.UserSignInService;
import com.coffepotato.vo.common.UserSignInVO;
import com.coffepotato.vo.common.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Chan's
 * <p>
 * Description :글로벌 인증서버 코어 컨트롤러 <br>
 *              - 1. 사용자 인증 -> JWT 토큰발급 ( ACCESS,REFRESH) <br>
 *              - 2. 토큰 재발급 -> JWT 토큰 재발급 ( REFRESH 토큰 인증 )  <br>
 * </p>
 * <br>
 * Updated :
 */
@Slf4j
@RequestMapping(value = "/auth")
@CrossOrigin(value = "*")
@RestController
public class AuthenticateCoreController {

    private final UserSignInService userSignInService;

    @Autowired
    public AuthenticateCoreController(UserSignInService userSignInService){
        this.userSignInService = userSignInService;
    }

    /**
     * 사용자 인증후 JWT 토큰을 리턴한다.
     * @param signIn    인증을 위해 입력된 사용자 ID/PWD 정보 VO객체
     * @return           JWT 토큰 (Access토큰 , Refresh 토큰)
     */
    @RequestMapping(value = "/signin/coffeuser" , method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseVo> signInAndAuthenticate(
                                                            @RequestAttribute("token-authed") boolean token_authed,
                                                            @RequestAttribute("token-kind") String token_kind,
                                                            @RequestBody(required = false) UserSignInVO signIn){

        if(token_authed){
            return  chkTokenStateSignInRequest(token_kind);
        }else{
            return userSignInService.SignInCoffeUser(signIn);
        }
    }


    /**
     * 요청으로 받은 refresh 토큰을 validation 하여 새로운 Access 토큰을 재 발행한다.
     * @return           JWT 토큰 (Access토큰)
     */
    @RequestMapping(value = "/refresh" , method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseVo> refreshTokenAndReissue(
                                                    @RequestAttribute("token-authed") boolean token_authed,
                                                    @RequestAttribute("token-kind") String token_kind
                                                    )
    {
        if(token_authed){
            return chkTokenStateRefreshTokenRequest(token_kind);
        }else{
            return new ResponseEntity<>(
                        ResponseVo.builder()
                                .responseCode(Errorcode.LOGICAL_THROW_ERROR.value())
                                .responseMsg("해당 요청에는 적합한 Token이 필요합니다.").build()
                        , HttpStatus.BAD_REQUEST);
        }

    }


    private ResponseEntity<ResponseVo> chkTokenStateRefreshTokenRequest(String token_kind){
        if(token_kind.equals(JWTCore.ACCESS_TOKEN)){
            return new ResponseEntity<>(
                    ResponseVo.builder()
                            .responseCode(Errorcode.LOGICAL_THROW_ERROR.value())
                            .responseMsg("해당 요청은 AccessToken을 허용하지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }else if(token_kind.equals(JWTCore.REFRESH_TOKEN)) {
           // ReIssueToken 프로세스
            return userSignInService.reIsuueAccessKey();
        }else{
            //적합한 토큰이 아님.
            return new ResponseEntity<>(
                    ResponseVo.builder()
                            .responseCode(Errorcode.LOGICAL_THROW_ERROR.value())
                            .responseMsg(token_kind).build()
                    , HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<ResponseVo> chkTokenStateSignInRequest(String token_kind){
        if(token_kind.equals(JWTCore.ACCESS_TOKEN)){
            return new ResponseEntity<>(
                    ResponseVo.builder()
                            .responseCode(Errorcode.NOERROR.value())
                            .responseMsg("인증된 사용자 입니다. 재인증이 필요하지 않습니다.").build()
                    , HttpStatus.ACCEPTED);
        }else if(token_kind.equals(JWTCore.REFRESH_TOKEN)) {
            return new ResponseEntity<>(
                    ResponseVo.builder()
                            .responseCode(Errorcode.LOGICAL_THROW_ERROR.value())
                            .responseMsg("해당 요청은 RefreshToken을 허용하지 않습니다.").build()
                    , HttpStatus.BAD_REQUEST);
        }else{
            //적합한 토큰이 아님.
            return new ResponseEntity<>(
                    ResponseVo.builder()
                            .responseCode(Errorcode.LOGICAL_THROW_ERROR.value())
                            .responseMsg(token_kind).build()
                    , HttpStatus.BAD_REQUEST);
        }
    }

}
