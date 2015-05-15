package com.yunzo.cocmore.core.function.service;


import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.controller.system.GetSerialNumberController;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GetSerialNumberControllerTest{
	Logger logger = Logger.getLogger(GetSerialNumberControllerTest.class);
	@Resource
	GetNumberService getNumberService;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
	public void getAddSerialNumber() {
		Map<String,Object> map =getNumberService.checkExist("FX-GYGL");
		logger.info("##################"+(String)map.get("serialNumber")==null);
	}

}
