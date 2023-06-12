package com.zjz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjz.annotaion.RedisCache;
import com.zjz.mapper.UserMapper;
import com.zjz.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@RedisCache()
	public User getUser(User user){
		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("id",user.getId());
		return userMapper.selectOne(userQueryWrapper);
	}
}
