package com.mycompany.backend.security;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
//@Component -> 시큐리티에 bean을 생성해줘서 사용하므로 관리 객체 x 
public class JwtAuthenticationFilter extends OncePerRequestFilter{
  //요청시 한번만 실행되는 필터[내부적으로 서블릿 dispatch 될 경우 실행 안함]
  //일반적으로 필터는 내부적으로 서블릿 dispatch가 될 경우에도 실행됨
 
  private RedisTemplate redisTemplate;//시큐리티 Bean에서 생성하면서 redisTemplate을 주입받움
  public void setRedisTemplate(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  //필터 생성
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    log.info("실행");
    //요청 헤더로부터 Authorization 헤더 값 얻기
    String authorization = request.getHeader("Authorization");
    //AccessToken 추출
    String accessToken = Jwt.getAccessToken(authorization);//Bearer 빼기
    // log.info(accessToken);
    
    
    
    
    //검증 작업
    //accessToken이 없을 경우도 고려  && 토큰이 유효한지 검사
    if(accessToken != null   && Jwt.validateToken(accessToken)) {
    //추가 부분
      //Redis에 존재 여부 확인
      //accesstoken이 날라오더라도 redis 존재하지 않으면 인증할 수 없다고 생각함[로그아웃 시 필요]
      ValueOperations<String, String> vo = redisTemplate.opsForValue();
      String redisRefreshToken = vo.get(accessToken);
      if(redisRefreshToken != null) {
      //인증 처리
        Map<String, String> userInfo = Jwt.getUserInfo(accessToken);//사용자 정보 얻기
        String mid = userInfo.get("mid");
        String authority = userInfo.get("authority");//인증 처리시 해당 사용자가 어떤 권한을 가지고 있는지 알리기 위해 설정시 넣어줌
        //직접 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(mid,null,
                                                                            AuthorityUtils.createAuthorityList(authority));
        SecurityContext securityContext = SecurityContextHolder.getContext(); //시큐리티 실행 환경 객체 생성
        securityContext.setAuthentication(authentication);//인증 객체를 넣어줌
      }
      
      
    }
    
    //해당 필터 다음 필터를 실행하도록 함
    //로그인 인증을 해준다는게 뭐지
    filterChain.doFilter(request, response);
    
  }
}
