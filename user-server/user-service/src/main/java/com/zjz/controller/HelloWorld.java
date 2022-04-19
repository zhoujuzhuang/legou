package com.zjz.controller;

import com.zjz.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorld {

	@Autowired
    private ReloadableResourceBundleMessageSource messageSource;
	
	@GetMapping("hello")
	@ResponseBody
	public String Hello(String lang){
		return lang;
	}
	
	@GetMapping("i18n")
	public String i18n(String key){
		messageSource.setCacheMillis(0);
		String message2 = messageSource.getMessage(key, null, null);
		System.out.println(message2);
		
		return "i18n";
	}
	
	@GetMapping("params")
	public String params(User user){
		System.out.println(user);
		return "i18n";
	}
	
	@GetMapping("restResponse")
	public ResponseEntity<User> restResponse(){
		User user = new User();
		user.setId(1L);
		user.setName("zhangsan");
		user.setAge(18);
		return ResponseEntity.status(209).body(user);
	}
	
	
}
