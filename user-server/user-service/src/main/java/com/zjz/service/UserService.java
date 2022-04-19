package com.zjz.service;

import com.zjz.annotaion.RedisCache;
import com.zjz.mapper.UserMapper;
import com.zjz.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@RedisCache()
	public User getUser(User record){
		return userMapper.select(record).get(0);
	}
	
	@RedisCache()
	public List<User> getUsers(User record){
		return userMapper.select(record);
	}
	
	@RedisCache()
	public Map<String,List<User>> getUserMap(User record){
		 List<User> userList = userMapper.select(record);
		 Map<String, List<User>> map = new HashMap<String,List<User>>();
		 map.put("userList", userList);
		 return map;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void addUser(User record){
		userMapper.insert(record);
	}
}
