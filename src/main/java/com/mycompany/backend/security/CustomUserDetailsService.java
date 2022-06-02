package com.mycompany.backend.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.backend.dao.MemberDao;
import com.mycompany.backend.dto.Member;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Resource
	private MemberDao memberDao;	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	  //해당 멤버를 가지고옴
		Member member = memberDao.selectByMid(username); 
		if(member == null) {//없으면 에러 처리
			throw new UsernameNotFoundException(username);
		}
		
		//권한을 추가 시켜줌
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(member.getMrole()));
		
		CustomUserDetails userDetails = new CustomUserDetails(
				member.getMid(), 
				member.getMpassword(),
				member.isMenabled(),
				authorities,
				member.getMname(),
				member.getMemail());
		
		return userDetails;
	}
}

