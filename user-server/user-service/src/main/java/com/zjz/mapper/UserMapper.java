package com.zjz.mapper;

import com.zjz.pojo.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
	List<User> selectUser(User user);
}
