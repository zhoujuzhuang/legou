package com.zjz.service;

import com.zjz.pojo.Order;
import com.zjz.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public Order getOrder(Order record){
        return orderMapper.selectOne(record);
    }
}
