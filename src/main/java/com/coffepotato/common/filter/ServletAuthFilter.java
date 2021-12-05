package com.coffepotato.common.filter;

import com.coffepotato.common.component.JWTCore;
import com.coffepotato.common.object.ConfirmResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class ServletAuthFilter extends GenericFilterBean{

    private final JWTCore jwtCore;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtCore.resolveToken((HttpServletRequest) request);

        if(token != null){
            //유효한 토큰 참조인경우.
            //토큰이  인증 헤더값으로 들어온 경우만 체크

            ConfirmResponse res = jwtCore.verifyToken(token);
            //if(res.result){
                //throw new ServletException((String)res.data);Vo() , HttpStatus.BAD_REQUEST);
                //}else{
            //ResponseEntity<ResponseVo> errRes = new ResponseEntity<>(new Response

            request.setAttribute("token-authed",true);
            request.setAttribute("token-kind", res.data.toString());
            //}

        }else{
            request.setAttribute("token-authed",false);
            request.setAttribute("token-kind","");
        }

        chain.doFilter(request,response);

    }



}
