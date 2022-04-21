package com.zjz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableDiscoveryClient // 开启EurekaClient功能
@EnableFeignClients   //开启Feign的功能
@MapperScan(basePackages = "com.zjz.mapper")
public class UserServiceApplication{

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean(name = "messageSource")
	public MessageSource initReloadableResourceBundleMessageSource() {
		ReloadableResourceBundleMessageSource rrbms = new ReloadableResourceBundleMessageSource();
		rrbms.setDefaultEncoding("UTF-8");
		rrbms.setBasename("classpath:lang/message");
		return rrbms;
	}
}
