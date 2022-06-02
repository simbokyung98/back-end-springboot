package com.mycompany.backend.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mycompany.backend.security.JwtAuthenticationFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableWebSecurity//해당 어노테이션 생성시 @configuration 이 생성됨 그래서 @Bean 가능
public class SecurityConfig extends WebSecurityConfigurerAdapter{
 //추가 부분 
//  @Resource
//  private JwtAuthenticationFilter jwtAuthenticationFilter;
  
  //추가 부분
  @Resource
  private RedisTemplate redisTemplate;
  
  //http 관련 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.info("실행");
    //서버 세션 비활성화
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //폼 로그인 비활성화
    http.formLogin().disable();
    //사이트 간 요청위조 방지 비활성화
    http.csrf().disable();
    //요청 경로 권한 설정
    http.authorizeRequests()
        //[board 및 요청은 모두 인증된 사람만 사용 가능]
        .antMatchers("/board/**").authenticated()
        //[그 외 경로는 모두 가능]
        .antMatchers("/**").permitAll();
    
    //CORS 설정: 다른 도메인의 JS로 접근할 수 있도록 허용
    //[REST API 에서 필요 : Front 와 Back 이 다른 도메인인 경우]
    http.cors();//cors 활성화
    
    
    //JWT 인증 필터 추가 [위치가 중요, form 인증 필터 앞에 추가]  //폼 로그인을 disable 했기 때문에 하단 위치에서 인증이 일어나야함
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); 
  }
  
  //추가 부분
  //해당 메소드 생성하면 관리객체를 등록하는 @Component 과 같은 효과
  //뭐가 더 장점일까???                                                      //관리객체라서 매개변수에 자동 주입 가능
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
    jwtAuthenticationFilter.setRedisTemplate(redisTemplate);
    return jwtAuthenticationFilter;
  }
  
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    log.info("실행");
    
    //MPA 폼 인증 방식에서 사용(JWT 인증 방식에서는 사용하지 않음)
    /* DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    //DB에서 어떤 정보를 가져올 것인가 결정
    provider.setUserDetailsService(new CustomUserDetailsService());
    //패스워드 인코더 무엇을 이용했는가
    provider.setPasswordEncoder(passwordEncoder());//passwordEncoder는 재정의 되어있기 때문에 매개변수 설정할 수 없음
    auth.authenticationProvider(provider);*/ 
    
  }
  
  //permitAll() vs ignoring()
  // permitAll() : 누구든지 요청할 수 있음[시큐리티 동작 x]
  // ignoring() : 시큐리티가 관여함
  
  @Override
  public void configure(WebSecurity web) throws Exception {
    log.info("실행");
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
    defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchyImpl());   
    web.expressionHandler(defaultWebSecurityExpressionHandler);
    
    //mpa에서 시큐리티를 적용하지 않는 경로 설정
    // rest api 에서는 ui가 없기 때문에 설정할 필요 없음
    /* web.ignoring()
    .antMatchers("/images/**")
    .antMatchers("/css/**")
    .antMatchers("/js/**")
    .antMatchers("/bootstrap/**")
    .antMatchers("/jquery/**")
    .antMatchers("/favicon.ico");*/
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    //해당 메소드 있어야 암호화된 비밀번호 해석이 가능하다
    //암호화 기술은 게속 변함 -> DB 내부에 다양한 암호화 방식을 사용하 비번 저장됨 -> 암호화된 방식을 알아야 비교하여 해석 가능
    
    //return new BCryptPasswordEncoder();//비밀번호 암호화 방삭을 저장하지 않고 내용만 저장함
    return PasswordEncoderFactories.createDelegatingPasswordEncoder(); //비밀번호 암호화 방식과 내용 함께 저장
  }
  
  //계층 권한 설정
  @Bean
  public RoleHierarchyImpl roleHierarchyImpl() {
     log.info("실행");
     RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
     roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
     return roleHierarchyImpl;
  }
  
  //각 프론트에서 브라우저 요청 시 어떤 요청을 받아들일지 결정
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    log.info("실행");
      CorsConfiguration configuration = new CorsConfiguration();
      //모든 요청 사이트 허용 [어떤 프론트에서 js 요청을 ajax로 보내더라도 받겠다]
      configuration.addAllowedOrigin("*");
      //모든 요청 방식 허용 [ get, post, put, delete 모두 허용]
      configuration.addAllowedMethod("*");
      //모든 요청 헤더 허용 [  ]
      configuration.addAllowedHeader("*");
      //모든 URL 요청에 대해서 위 내용을 적용
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }

}
