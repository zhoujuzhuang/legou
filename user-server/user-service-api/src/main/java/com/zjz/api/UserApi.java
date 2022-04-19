package com.zjz.api;

import com.zjz.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("user")
public interface UserApi {

    @GetMapping("{id}")
    User getUserById(@PathVariable Long id);

}
