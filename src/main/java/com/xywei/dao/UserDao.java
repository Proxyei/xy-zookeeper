package com.xywei.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author future
 * @Datetime 2019年9月3日 下午5:55:44<br/>
 * @Description 测试一下log4j
 */
public class UserDao {

	private static final Logger log = LoggerFactory.getLogger(UserDao.class);

	public static void main(String[] args) throws Exception {
		log.debug("helloworld debug ");
		log.info("helloworld info ");
		log.warn("helloworld warn ");
		log.error("helloworld error");
		helloworldMethod();
	}

	public static void helloworldMethod() {
		log.debug("xxxx");
		log.info("xxxxxoooooo");
		log.info("用户{} 登录了", "xy");
		try {
			int a = 1 / 0;
			System.out.println(a);
		} catch (Exception e) {
			log.warn("异常信息：{}，位置:{}", e.getMessage(), e.getStackTrace());
		}
	}

}
