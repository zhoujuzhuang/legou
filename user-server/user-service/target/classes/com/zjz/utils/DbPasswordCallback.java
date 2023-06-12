package com.zjz.utils;

import java.util.Properties;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;

/**
 * 单数据源情况下默认自动解密，多数据源需要配置回调方法手动解密
 */
public class DbPasswordCallback extends DruidPasswordCallback{

	@Override
	public void setProperties(Properties properties){
		super.setProperties(properties);
		//获取application.yml 里面配置的密码和公钥
        String password = (String) properties.get("password");
        String publickey = (String) properties.get("publicKey");
        try {
            String dbpassword = ConfigTools.decrypt(publickey, password);
            setPassword(dbpassword.toCharArray());
        } catch (Exception e) {
        }
	}
}
