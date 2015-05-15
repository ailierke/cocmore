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


import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsDynamicServiceImplTest{
	Logger logger = Logger.getLogger(GroupsDynamicServiceImplTest.class);
	@Resource
	GroupsDynamicService groupsDynamicService;
	public YBasicSocialgroupsdynamic dynamic;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroupDynamic() {
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		YBasicType yBasicType = new YBasicType();yBasicType.setFid("Fid");
		dynamic  = new YBasicSocialgroupsdynamic();
		dynamic.setFbillState(0);
		dynamic.setYBasicSocialgroups(yBasicSocialgroups);
		dynamic.setFcomment("1");
		dynamic.setFdetailAddress("1");
		dynamic.setFheadline("1");
		dynamic.setFlogoImage("1");
		dynamic.setFmessage("1");
		dynamic.setFnumber("1");
		dynamic.setYBasicType(yBasicType);
		groupsDynamicService.addDynamic(dynamic);
	}
	@Test
//	@Ignore
	public void testFindAllGroup(){
		groupsDynamicService.getAllDynamicPagingList(null, null, "6f4f07da-1317-44a1-a467-366aa9068db6","");
	}
	@Test
//	@Ignore
	public void testUpdateGroup(){
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		
		 dynamic  = new YBasicSocialgroupsdynamic();
		 YBasicType yBasicType = new YBasicType();yBasicType.setFid("Fid");
		 dynamic.setFbillState(1);
			dynamic.setYBasicSocialgroups(yBasicSocialgroups);
			dynamic.setFcomment("2");
			dynamic.setFdetailAddress("2");
			dynamic.setFheadline("2");
			dynamic.setFlogoImage("2");
			dynamic.setFmessage("2");
			dynamic.setFnumber("2");
			dynamic.setYBasicType(yBasicType);
		 dynamic.setFid("4431ba8f-9f6b-4358-91a3-72d3051e88f8");
		 groupsDynamicService.updateDynamic(dynamic);
	}
	@Test
	public void testDeleteGroupDynamic(){
		dynamic = new YBasicSocialgroupsdynamic();
		dynamic.setFid("d435207f-b4ce-480d-b441-6ad82151582c");//这个的id是表中存在数据的id
		groupsDynamicService.deleteDynamic(dynamic);
		
	}
}
