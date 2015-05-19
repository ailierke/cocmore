package com.yunzo.cocmore.core.function.service;


import java.util.Date;

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


//import com.yunzo.cocmore.core.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
//@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsDynamicServiceImplTest{
	Logger logger = Logger.getLogger(GroupsDynamicServiceImplTest.class);
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroupDynamic() {
	}
	@Test
//	@Ignore
	public void testFindAllGroup(){
	}
	@Test
//	@Ignore
	public void testUpdateGroup(){
	}
	@Test
	public void testDeleteGroupDynamic(){
		
	}
}
