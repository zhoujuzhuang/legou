package com.zjz.mapper;


import com.zjz.pojo.Order;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {
	List<Order> selectOrder(Order order);
}
