package com.zjz.feign;


import com.zjz.api.OrderApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * feign声明式接口。发起远程调用的。
 *
 String url = "http://FEIGN-PROVIDER/goods/findOne/"+id;
 Goods goods = restTemplate.getForObject(url, Goods.class);
 *
 * 1. 定义接口
 * 2. 接口上添加注解 @FeignClient,设置value属性为 服务提供者的 应用名称
 * 3. 编写调用接口，接口的声明规则 和 提供方接口保持一致。
 * 4. 注入该接口对象，调用接口方法完成远程调用
 *
 */

@FeignClient(value = "order-server")
public interface OrderFeignClient extends OrderApi {

}
