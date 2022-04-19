package com.zjz.api;

import com.zjz.pojo.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("order")
public interface OrderApi {

    @GetMapping("/{id}")
    Order getOrderById(@PathVariable Long id);
}
