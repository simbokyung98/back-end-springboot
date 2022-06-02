package com.mycompany.backend.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

@Configuration//설정 사항이라는 뜻
//dispather root에 mtbatis 설정 사항 입력
@MapperScan(basePackages = {"com.mycompany.backend.dao"})
public class MyBatisConfig {
	
	//DataSource 의존성 주입을 하거나 매개변수를 사용하여 생성
	
	@Resource
	private DataSource dataSource;
	
	@Resource
	private WebApplicationContext wac;//객체들을 관리하는 IOC 컨테이너 역할
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
		ssfb.setDataSource(dataSource);
		ssfb.setConfigLocation(wac.getResource("classpath:mybatis/mapper-config.xml"));
		ssfb.setMapperLocations(wac.getResources("classpath:mybatis/mapper/*.xml"));
		return ssfb.getObject();
		
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory ssf) {
		//관리객체는 매개변수에 대입 가능 -> 매개변수로 생성하거나 자체로 함수 넣기도 가능
		//SqlSessionTemplate sst = new SqlSessionTemplate(sqlSessionFactory());
		SqlSessionTemplate sst = new SqlSessionTemplate(ssf);
		return sst;
	}

}
