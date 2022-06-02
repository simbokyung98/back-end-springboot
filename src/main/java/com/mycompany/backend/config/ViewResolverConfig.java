package com.mycompany.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Configuration
public class ViewResolverConfig {
	//해당 calss로 만들거나 application.properties 에서 설정
	@Bean
	  public ViewResolver internalResourceViewResolver() {
	    log.info("실행");
	    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	    //spring viewResolver 설정은 xml로 했는데 property 의 name은 setter 주입임
	    
	    viewResolver.setViewClass(JstlView.class);
	    viewResolver.setPrefix("/WEB-INF/views/");
	    viewResolver.setSuffix(".jsp");
	    return viewResolver;
	  }


}
