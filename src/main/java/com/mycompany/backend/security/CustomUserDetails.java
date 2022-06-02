package com.mycompany.backend.security;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
  //인증 정보로 추가로 저장하로 싶은 내용 정의
	private String mname;
	private String memail;
	
	//기본적으로 시큐리티가 제공하는 정보는 id, password, 권한 정도 인데 더 많은 커스텀 정보를 포함하고 싶으면
	//이 코드를 사용하여 설정하여야해 !!!!!!!!!!!!!
	public CustomUserDetails(
			String mid, 
			String mpassword, 
			boolean menabled, 
			List<GrantedAuthority> mauthorities,
			String mname,
			String memail) {
		super(mid, mpassword, menabled, true, true, true, mauthorities);
		this.mname = mname;
		this.memail = memail;
	}
	
	public String getMname() {
		return mname;
	}

	public String getMemail() {
		return memail;
	}
}

