package com.coffepotato.common.component;

import com.coffepotato.config.JWTConfig;
import com.coffepotato.db.dao.UserServiceAuthRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import com.coffepotato.common.object.ConfirmResponse;
import com.coffepotato.dto.RefreshTokenDTO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * @author Chan's
 * <p>
 * Description : JWT 토큰을 발행 및 관리하는 코어 모듈 <br>
 * </p>
 * <br>
 * Updated : 2021/11/17
 */
@Slf4j
public class JWTCore {

    @Resource
    private JWTConfig jwtConfig;

    @Resource
    private UserServiceAuthRepository UserServiceAuthRepository;

    private final JWTSecureManager jwtSecureManager;

    public static final String AUTH_HEADER = "Authorization" ;

    public static final String JWTS_TYPE = "JWTS";

    public static final String ACCESS_TOKEN = "ACC";

    public static final String REFRESH_TOKEN = "REF";

    public JWTCore(JWTSecureManager jwtSecureManager) {
        this.jwtSecureManager = jwtSecureManager;
    }


    /**
     * 인증된 사용자의 claim을 실은(Paylodd) Access 토큰을 발행한다.
     * @param withUserKey    인증된 사용자의 Key값
     * @return                AccessToken
     */
    public String issueAccessJWTSToken(String withUserKey){
        String tokenString = "";

        Map<String,Object> claims = getAuthServicesClaims(withUserKey);
        claims.putAll(getCommonAccClaims());

        tokenString = Jwts.builder().setHeaderParam("typ",JWTS_TYPE)                  //JWT타입 설정
                                    .setHeaderParam("attr" , ACCESS_TOKEN)        //토큰구분(Access토큰)
                                    .setClaims(claims)
                                    .signWith(jwtSecureManager.getServer_privateKey())  //개인키 싸인.
                                    .compact();

        return tokenString;
    }

    /**
     * 인증된 사용자의 Refresh 토큰을 발행한다.
     * @param withUserKey       인증된 사용자의 Key값
     * @param refDTO            리프레쉬토큰을 관리(Manage)하기 위한 DTO
     * @return
     */
    public String issueRefreshJWTSToken(String withUserKey, RefreshTokenDTO refDTO){
        String tokenString = "";

        Map<String,Object> claims = getManagedClaims(withUserKey);
        claims.putAll(getCommonRefClaims());

        tokenString = Jwts.builder().setHeaderParam("typ", JWTS_TYPE)                   //JWT타입 설정
                                    .setHeaderParam("attr" , REFRESH_TOKEN)         //토큰구분(REFRESH토큰)
                                    .setClaims(claims)
                                    .signWith(jwtSecureManager.getServer_privateKey())
                                    .compact();



        //claim을 DTO 객체로 맵핑.
        refDTO.setTokenId(claims.get("jti").toString());
        refDTO.setToken(tokenString);
        refDTO.setTokenOwner(claims.get("usr").toString());
        refDTO.setCreateAt((Date)claims.get("iat"));
        refDTO.setExpireOn((Date)claims.get("exp"));


        return  tokenString;
    }

    /**
     * 인증된 사용자의키를 통해서 접근가능한 서비스와 그에 따른 권한을 조회한다.
     * @param withUserKey       인증된 사용자의 Key값
     * @return                  JWT 적재용 claim
    */
    public Map<String,Object> getAuthServicesClaims(String withUserKey) {
        Map<String, Object> authServices = new HashMap<>();

        UserServiceAuthRepository.findUserServiceAuthByIdUserIdEquals(withUserKey).forEach(auths -> {
            authServices.put(auths.getId().getAuthServiceUrl(), auths.getAuthAccessLevel());
        });
        authServices.put("usr" , withUserKey);

        return authServices;
    }


    /**
     * 관리가 가능한(Managed) 토큰을 위해 토큰키를 생성한다.
     * @param withUserKey
     * @return
     */
    public Map<String,Object> getManagedClaims(String withUserKey){
        Map<String,Object> managed = new HashMap<>();
        managed.put("jti",UUID.randomUUID() );
        managed.put("usr", withUserKey);

        return  managed;
    }


    /**
     * Access토큰에 적용가능한 공통 Claim을 세팅한다.
     * @return      공통 Claim
     */
    private Map<String,Object> getCommonAccClaims(){
        Map<String, Object> common = new HashMap<>();

        Date now = new Date();
        common.put("iat", now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MILLISECOND , jwtConfig.getAccTokenExpire().intValue());

        Date expireDate = cal.getTime();
        common.put("exp",expireDate);
        common.put("iss", jwtConfig.getIssuer());

        return common;
    }

    /**
     * Refresh토큰에 적용가능한 공통 Claim을 세팅한다.
     * @return          공통 Claim
     */
    private Map<String,Object> getCommonRefClaims(){
        Map<String, Object> common = new HashMap<>();

        Date now = new Date();
        common.put("iat", now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MILLISECOND , jwtConfig.getReTokenExpire().intValue());

        Date expireDate = cal.getTime();
        common.put("exp",expireDate);
        common.put("iss", jwtConfig.getIssuer());

        return common;
    }


    /**
     * 요청된 HttpRequest Header 에서 토큰을 추출한다.
     * @param request    현재 HttpServletRequest
     * @return           토큰 문자열.
     */
    public String resolveToken(HttpServletRequest request){
        return  request.getHeader(JWTCore.AUTH_HEADER);
    }


    /**
     * 토큰을 공개키를 통해 Validataion을 진행하고 토큰의 지정된 claim들을 전달한다.
     * @param jwtToken      JWT토큰
     * @return              claims
     */
    private Jws<Claims> validateToken(String jwtToken) throws Exception{

        return  Jwts.parserBuilder().setSigningKey(jwtSecureManager.getServer_publicKey()).build()
                .parseClaimsJws(jwtToken);

    }

    /**
     * 토큰 인증작업을 수행한다.
     * @param jwtToken  JWT 토큰
     * @return          인증결과
     */
    public ConfirmResponse verifyToken(String jwtToken){

        ConfirmResponse res = new ConfirmResponse();
        
        try {
            Jws<Claims> claims = validateToken(jwtToken);

            if (!claims.getBody().getExpiration().before(new Date())) {

                if(claims.getHeader().containsKey("attr")){
                    //키 구분으로 확인.
                    String KEY_KIND =  claims.getHeader().get("attr").toString();

                    if(KEY_KIND.equals(ACCESS_TOKEN) || KEY_KIND.equals(REFRESH_TOKEN)) {
                        res.data = KEY_KIND;
                    }
                    res.result = true;
                }else{
                    //키구분이 없는경우
                    res.data ="유효한 JWT토큰이 아닙니다.";
                    res.result =true;
                }

            } else {
                res.data = "만료된 토큰입니다.";
                res.result = true;
            }
        }catch(ExpiredJwtException|MalformedJwtException|SignatureException|UnsupportedJwtException|IllegalArgumentException ex){
            //만료된 토큰 / 잘못된 형식의 토큰 / 서명오류 / 지원되지않는 JWT 형식 등등..
            log.error(ex.getMessage());
            res.data = ex.getMessage();
            res.result = true;
        }catch(Exception e) {
            log.error(e.getMessage());
            res.data = "예상치 못한 에러가 발생하였습니다. \r\n" + e.getMessage() ;
            res.result = true;
        }
        return  res;
    }


    /**
     * 전달된 토큰에서 Refresh 토큰을 관리하는 키값을 추출한다. ( jti, usr)
     * @param jwtToken      Refresh토큰
     * @return              키값.
     */
    public Map<String,String> extractTokenDBKey(String jwtToken){
        Map<String,String> keyInfo = new HashMap<>();

        try{
            Jws<Claims> claims = validateToken(jwtToken);

            String usr = claims.getBody().get("usr",String.class);
            String tokenId  = claims.getBody().get("jti" , String.class);

            keyInfo.put("token_owner",usr);
            keyInfo.put("token_id",tokenId);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }

        return keyInfo;
    }
}
