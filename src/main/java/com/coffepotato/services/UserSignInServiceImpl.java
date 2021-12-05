package com.coffepotato.services;

import com.coffepotato.common.component.JWTCore;
import com.coffepotato.common.component.CoffeUserAuthenticator;
import com.coffepotato.common.enumeration.Errorcode;
import com.coffepotato.common.util.ModelWrapper;
import com.coffepotato.common.util.SessionUtil;
import com.coffepotato.config.JWTConfig;
import com.coffepotato.db.dao.CoffeUserRepository;
import com.coffepotato.db.dao.RefreshTokenKeeperRepository;
import com.coffepotato.db.entity.CoffeUser;
import com.coffepotato.db.entity.RefreshTokenKeeper;
import com.coffepotato.dto.RefreshTokenDTO;
import com.coffepotato.dto.UserDTO;
import com.coffepotato.vo.common.TokenVo;
import com.coffepotato.vo.common.UserSignInVO;
import com.coffepotato.vo.common.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserSignInServiceImpl implements UserSignInService {

    @Resource
    private JWTConfig jwtConfig;

    @Resource
    private JWTCore jwtCore;

    @Resource
    private CoffeUserRepository coffeUserRepository;

    //TODO: 인메모리 올리는 방법 다시 확인하기 .
    @Resource
    private RefreshTokenKeeperRepository refreshTokenKeeperRepository;

    @Resource
    private CoffeUserAuthenticator coffeUserAuthenticator;


    @Override
    public ResponseEntity<ResponseVo> SignInCoffeUser(UserSignInVO signIn) {


        ResponseVo res = new ResponseVo();
        try{
            if(signIn == null)
                throw new Exception("인증에 필요한 정보가 입력되지 않았습니다.");

            //Sign In 프로세스
            String userId = signIn.getUserId();
            CoffeUser signUser = coffeUserRepository.findCoffeUserByUserIdOrderByKorNameAsc(userId);


            if(signUser == null)
                throw new Exception("아이디가 잘못입력되었습니다. 아이디를 정확히 입력해 주세요.");

            UserDTO user = ModelWrapper.Mapping(signUser, new UserDTO());


            if(coffeUserAuthenticator.authenticateString(signIn.getPassword(), user.getPassword())){
                res.setData(createAllToken(userId));
            }else{
                throw  new Exception("비밀번호가 잘못입력되었습니다. 비밀번호를 정확히 입력해 주세요.");
            }
            res.setResponseCode(Errorcode.NOERROR.value());
            res.setResponseMsg("사용자 ["+ signUser.getNickName() + " " + signUser.getKorName() +  "] 님이 인증되었습니다.");
            log.info("["+ signUser.getUserId() + "(" + signUser.getNickName() + " " + signUser.getKorName() +  ")] 사용자의 토큰이 발행되었습니다.");


            return new ResponseEntity<>(res,HttpStatus.OK);
        }catch (Exception ex){
            res.setResponseCode(Errorcode.RUNTIME.value());
            res.setResponseMsg(ex.getMessage());
            log.error(ex.getMessage());
            log.error("사용자 인증 중 에러가 발생 하여 중지되었습니다.");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public Map<String, Object> createAllToken(String auth_key) throws Exception {
        Map<String,Object> res = new HashMap<>();
        //Access 토큰 생성.
        res.put("ACCESS_TOKEN" ,  TokenVo.builder()
                                    .token(jwtCore.issueAccessJWTSToken(auth_key))
                                    .auth_type("bearer")
                                    .expires_in(jwtConfig.getAccTokenExpire() / 1000)
                                    .build()
        );
        //Refresh 토큰 생성.
        RefreshTokenDTO refTokenDTO = new RefreshTokenDTO();

        String token = jwtCore.issueRefreshJWTSToken(auth_key, refTokenDTO);
        res.put("REFRESH_TOKEN" , TokenVo.builder()
                                .token(token)
                                .auth_type("bearer")
                                .expires_in(jwtConfig.getReTokenExpire() / 1000)
                                .build()
        );

        //DB에 생성된 Refresh 토큰을 저장.
        if(!refTokenDTO.getTokenId().equals("")){
            RefreshTokenKeeper refreshTokenKeeper =  ModelWrapper.Mapping(refTokenDTO, new RefreshTokenKeeper());

            refreshTokenKeeperRepository.save(refreshTokenKeeper);
        }

        return  res;
    }

    @Override
    public ResponseEntity<ResponseVo> reIsuueAccessKey(){
        //요청으로 들어온 RefreshToken 을 확인한다.
        ResponseVo res = new ResponseVo();

        String ref_token = SessionUtil.getHeaderInfo(JWTCore.AUTH_HEADER);

        Map<String,String> param = jwtCore.extractTokenDBKey(ref_token);

        RefreshTokenKeeper findToken = refreshTokenKeeperRepository.findRefreshTokenKeeperByTokenIdAndTokenOwnerAndTokenState(
                                                                        param.get("token_id"),param.get("token_owner"), (short) 0);

        if(findToken != null){
            //유효한 RefreshToken이 존재하는경우
            Map<String,Object> data = new HashMap<>();
            String auth_key = param.get("token_owner");

            data.put("ACCESS_TOKEN" ,  TokenVo.builder()
                    .token(jwtCore.issueAccessJWTSToken(auth_key))
                    .auth_type("bearer")
                    .expires_in(jwtConfig.getAccTokenExpire() / 1000)
                    .build()
            );
            res.setData(data);
            res.setResponseCode(Errorcode.NOERROR.value());
            res.setResponseMsg("사용자의 AccessToken이 갱신되었습니다.");

            return new ResponseEntity<>(res, HttpStatus.OK);
        }else{
            //저장된 RefreshToken이 없는경우  -> 만료되거나 사용중지인 토큰인경우
            res.setResponseCode(Errorcode.LOGICAL_THROW_ERROR.value());
            res.setResponseMsg("유효한 RefreshToken이 존재하지 않습니다. 재인증이 필요합니다.");

            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }


    }


}
