package com.mycompany.backend.security;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Jwt {
  // 상수
  private static final String JWT_SECRET_KEY = "kosa12345";
  private static final long ACCESS_TOKEN_DURATION = 1000*10;// 30분
  public static final long REFRESH_TOKEN_DURATION = 1000 * 60 * 60 * 24;// 24시간
  
  //AccessToken 생성(JWT에 뭘 넣었냐? : 사용자의 정보와 권한을 넣었다!)
  public static String createAccessToken(String mid, String authority) {// 사용자에 대한 정보와 권한 을 넣음 -> why? 인증하기 위해
    log.info("실행");
    String accessToken = null;
    
    try {
      // JwtBuilder를 리턴함!
      accessToken = Jwts.builder()
                         //헤더 설정
                        .setHeaderParam("alg", "HS256")
                        .setHeaderParam("typ", "JWT")
                        //페이로드
                        .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_DURATION)) //만료기간 설정
                        .claim("mid", mid)
                        .claim("authority", authority) //얘는 ROLE_ADMIN이 들어감
                        //서명 설정
                        .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY.getBytes("UTF-8")) //비밀키에 대한 배열
                        .compact();
    }catch(Exception e) {
      log.error(e.getMessage());
    }
    return accessToken;
  }

//refreshToken 생성(JWT에 뭘 넣었냐? : 사용자의 정보와 권한을 넣었다!)
  public static String createRefreshToken(String mid, String authority) {
    log.info("실행");
    String refreshToken = null;

    try {
      // JwtBuilder를 리턴함!
      refreshToken = Jwts.builder()
          // 헤더 설정
          .setHeaderParam("alg", "HS256").setHeaderParam("typ", "JWT")
          // 페이로드
          .setExpiration(new Date(new Date().getTime() + REFRESH_TOKEN_DURATION)) // 만료기간 설정
          .claim("mid", mid).claim("authority", authority) // 얘는 ROLE_ADMIN이 들어감
          // 서명 설정
          .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY.getBytes("UTF-8")) // 비밀키에 대한 배열
          .compact();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return refreshToken;
  }
  
  //토큰 유효성 검사 방법
  public static boolean validateToken(String token) {
    log.info("실행");
    boolean result = false;
    
    try {
      result = Jwts.parser()
                  .setSigningKey(JWT_SECRET_KEY.getBytes("UTF-8")) //비밀키와 맞느냐!!
                  .parseClaimsJws(token) //크레임 객체를 리턴함! 위에서 만든 claim 객체를 리턴함!
                  .getBody() //body를 얻음
                  .getExpiration() //만료기간을 얻음
                  .after(new Date());
    }catch(Exception e) {
      log.error(e.getMessage());
    }
    
    return result;
  }
  

  
  //토근 만료 시간 얻기
  public static Date getExpiration(String token) {
    log.info("실행");
    Date result = null;
    
    try {
      result = Jwts.parser()
                  .setSigningKey(JWT_SECRET_KEY.getBytes("UTF-8")) //비밀키와 맞느냐!!
                  .parseClaimsJws(token) //크레임 객체를 리턴함! 위에서 만든 claim 객체를 리턴함!
                  .getBody() //body를 얻음
                  .getExpiration(); //만료기간을 얻음
    }catch(Exception e) {
      log.error(e.getMessage());
    }
    
    return result;
  }

  
  //인증 사용자 정보 얻기
  public static Map<String, String> getUserInfo(String token){
    log.info("실행");
    Map<String, String> result = new HashMap<>();
    
    try {
      Claims claims = Jwts.parser()
                  .setSigningKey(JWT_SECRET_KEY.getBytes("UTF-8")) //비밀키와 맞느냐!!
                  .parseClaimsJws(token) //크레임 객체를 리턴함! 위에서 만든 claim 객체를 리턴함!
                  .getBody(); //body를 얻음
      result.put("mid", claims.get("mid", String.class));
      result.put("authority", claims.get("authority", String.class));
                  
    }catch(Exception e) {
      log.error(e.getMessage());
    }
    
    return result;
  }

  
  //요청 authorization 헤더 값에서 AccessToken 얻기
  //Bearer xxxxxxxxxxxxxxx.xxxxxxxxxxxxxxxxxxxx.xxxxxxxxxxxxxxxxxx ->x 부분만 얻어내는 방법
  public static String getAccessToken(String authorization) {
    String accessToken = null;
    
    if(authorization != null && authorization.startsWith("Bearer ")) {
      accessToken = authorization.substring(7);
    }
    
    
    return accessToken;
  }
  
  
  
//토큰 생성 테스트
  /*public static void main(String[] args) throws Exception {
  
  String accessToken = createAccessToken("user", "ROLE_USER");
  log.info(accessToken);//토근 암호화된 정보 얻기
  System.out.println(validateToken(accessToken));//토근이 유효한지 확인
  
  Date expiration = getExpiration(accessToken);//유효 기간 확인
  System.out.println(expiration);
  
  Map<String, String> userInfo = getUserInfo(accessToken);//인증 사용자 정보 확인 
  System.out.println(userInfo);
  
  }*/


}
