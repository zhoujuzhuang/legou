package com.zjz.controller;

import com.zjz.pojo.Order;
import com.zjz.feign.OrderFeignClient;
import com.zjz.pojo.User;
import com.zjz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrderFeignClient orderFeignClient;
	
	@GetMapping("{id}")
	public User getUserById(@PathVariable Long id){
		User user = new User();
		user.setId(id);
		return userService.getUser(user);
	}

	@GetMapping("order/{orderid}")
	public Order getOrderById(@PathVariable Long orderid){
		return orderFeignClient.getOrderById(orderid);
	}
}
