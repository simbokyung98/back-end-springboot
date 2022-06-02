package com.mycompany.backend.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.swing.border.EtchedBorder;

import org.json.JSONObject;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.backend.config.RedisConfig;
import com.mycompany.backend.dto.Member;
import com.mycompany.backend.security.Jwt;
import com.mycompany.backend.service.MemberService;
import com.mycompany.backend.service.MemberService.JoinResult;
import com.mycompany.backend.service.MemberService.LoginResult;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/member")
public class MemberController {
  @Resource
  private MemberService memberService;
  
  @Resource
  private PasswordEncoder passwordEncoder;
  
  @Resource
  private RedisTemplate redisTemplate;

  @PostMapping("/join")             //요청 body에 json 형태가 들어가야함
  public Map<String, Object> join( @RequestBody Member member){
    //계정 활성화
    member.setMenabled(true);
    //패스워드 암호화
    member.setMpassword(passwordEncoder.encode(member.getMpassword()));
    //회원가입 처리
    JoinResult joinResult = memberService.join(member);
    //응답 내용 설정
   
    Map<String, Object> map = new HashMap<>();
    if(joinResult == joinResult.SUCCESS) {
      map.put("result", "success");
    }else if(joinResult == joinResult.DUPLICATED) {
      map.put("result", "duplicated");
    }else {
      map.put("result", "error");
    }
   
    return map;
  }
  
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Member member){
    log.info("실행");
    //mid와 mpasword 가 없을 경우
    if(member.getMid() == null | member.getMpassword() == null) {
      return ResponseEntity
                .status(401)
                .body("mid or password cannot be null");
    }
    //로그인 결과 얻기
    LoginResult loginResult = memberService.login(member);
    if(loginResult != loginResult.SUCCESS) {
      return ResponseEntity.status(401)
          .body("mid or mpassword is wrong");
    }
    Member dbMember = memberService.getMember(member.getMid());
    String accessToken = Jwt.createAccessToken(member.getMid(), dbMember.getMrole());
    String refreshToken = Jwt.createRefreshToken(member.getMid(), dbMember.getMrole());
    
    //Redis 에 저장
    ValueOperations<String, String> vo = redisTemplate.opsForValue();
    vo.set(accessToken, refreshToken, Jwt.REFRESH_TOKEN_DURATION, TimeUnit.MILLISECONDS);
    //refreshToken 만료 기간과 동일하게 설정
    
    //쿠키 설정
    String refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                                              .httpOnly(true)
                                              .secure(false)
                                              .path("/")
                                              .maxAge(Jwt.REFRESH_TOKEN_DURATION/1000)
                                              .domain("localhost")
                                              .build()
                                              .toString();
    
    //본문 생성
    String json = new JSONObject()
            .put("accessToken", accessToken)
            .put("mid", member.getMid())
            .toString();
    
    //응답 설정
    return ResponseEntity//응답 코드: 200
            .ok() 
            //응답 헤더 추가
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            //응답 바디 추가
            .body(json);
    
  }
  
  @GetMapping("/refreshToken")              
  public ResponseEntity<String> refreshToken(
        @RequestHeader("Authorization") String authorization, 
        @CookieValue("refreshToken") String refreshToken){
    
    //AccessToken 얻기
    String accessToken = Jwt.getAccessToken(authorization);//header에 있는 권한에 따라 accesstoken 가져오기
    if(accessToken == null) {//access 토큰 없으면
      return ResponseEntity.status(401).body("no access token");
    }
    
    //RefreshToken 여부
    if(refreshToken == null) {
      return ResponseEntity.status(401).body("no refresh token");
    }
    
    //동일한 토큰인지 확인
    ValueOperations<String, String> vo = redisTemplate.opsForValue();
    String redisRefreshToken = vo.get(accessToken);
    if(redisRefreshToken == null) {//access 토큰이 잘못되었다면
      return ResponseEntity.status(401).body("wrong access token");
    }
    
    //클라이언트 refreshToken과 redisRefresh 토큰 일치 여부 확인
    if(!refreshToken.equals(redisRefreshToken)) {
      return ResponseEntity.status(401).body("wrong refresh token");
    }
    
    
    
    //새로운 AccessToekn 생성
    //refreshToken을 사용하여 새로운 유저 정보 생성
    Map<String, String> userInfo = Jwt.getUserInfo(refreshToken);
    String mid = userInfo.get("mid");
    String authority = userInfo.get("authority");
    //새로 받은 인증 정보 이용하여 accesstoken 생성
    String newAccessToken = Jwt.createAccessToken(mid, authority);
    
    //Redis에 저장된 기존 정보 삭제
    redisTemplate.delete(accessToken);
    
    //Redis에 새로운 정보를 저장
    //vo.set(accessToken, refreshToken, Jwt.REFRESH_TOKEN_DURATION, TimeUnit.MILLISECONDS); 
    //이게 틀리는 이유 : 로그인시 refreshToken 만료기간과 설정기간이 동일해서 가능
                     //하지만 지금 시점은 refresh을 사용하다가 새로운 정보를 저장하기 때문에 남은 시간으로 설정해야함 
    
    Date expiration = Jwt.getExpiration(refreshToken);
    vo.set(newAccessToken, refreshToken, expiration.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
    
    
    
    //응답 설정
    String json = new JSONObject()
                                .put("accessToken", newAccessToken)
                                .put("mid", mid)
                                .toString();
    return ResponseEntity.ok()
                         .header(HttpHeaders.CONTENT_TYPE, "application/json")
                         .body(json);
  }
  
  @GetMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization){
    //Access Token 얻기
    String accessToken  = Jwt.getAccessToken(authorization);
    if(accessToken == null ) {
      return ResponseEntity.status(401).body("invalide access token");
    }
    
    //Redis에 저장된 인증 정보 삭제
    redisTemplate.delete(accessToken);
    
  //RefreshToken 쿠키 삭제
    String refreshTokenCookie = ResponseCookie.from("refreshToken", accessToken)
                                              .httpOnly(true)
                                              .secure(false)
                                              .path("/")
                                              .maxAge(0)
                                              .domain("localhost")
                                              .build()
                                              .toString();
    
    return ResponseEntity.ok()
                          .header(HttpHeaders.CONTENT_TYPE, "application/json")
                          .body("success");
  }
  
}
