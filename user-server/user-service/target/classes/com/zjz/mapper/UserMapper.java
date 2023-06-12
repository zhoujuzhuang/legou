package com.zjz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjz.pojo.User;

public interface UserMapper extends BaseMapper<User> {
    User selectUser(User user);
}
