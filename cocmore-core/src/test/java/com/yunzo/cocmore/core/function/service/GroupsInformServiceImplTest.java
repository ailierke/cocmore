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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsInformServiceImplTest{
	Logger logger = Logger.getLogger(GroupsInformServiceImplTest.class);
	@Resource
	GroupsInformService groupsInformService;
	public YBasicSocialgroupsinform inform;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroupInform() {
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		YBasicType yBasicType = new YBasicType();yBasicType.setFid("Fid");
		inform  = new YBasicSocialgroupsinform();
		inform.setFbillState(0);
		inform.setYBasicSocialgroups(yBasicSocialgroups);
		inform.setFcomment("1");
		inform.setFdetailAddress("1");
		inform.setFheadline("1");
		inform.setFlogoImage("1");
		inform.setFmessage("1");
		inform.setFnumber("1");
		inform.setFinformPeopleNum(new Integer(1));
		inform.setFparticipationNum(new Integer(1));
		inform.setFstartTime(new java.sql.Date(new Date().getTime()));
		inform.setFfinishTime(new java.sql.Date(new Date().getTime()));
		inform.setYBasicType(yBasicType);
//		groupsInformService.addInform(inform,null);
	}
	@Test
//	@Ignore
	public void testFindAllGroup(){
		groupsInformService.getAllInformPagingList(null, null, "6f4f07da-1317-44a1-a467-366aa9068db6","");
	}
	@Test
//	@Ignore
	public void testUpdateGroup(){
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		
		 inform  = new YBasicSocialgroupsinform();
		 YBasicType yBasicType = new YBasicType();yBasicType.setFid("Fid");
		 inform.setFbillState(1);
			inform.setYBasicSocialgroups(yBasicSocialgroups);
			inform.setFcomment("2");
			inform.setFdetailAddress("2");
			inform.setFheadline("2");
			inform.setFlogoImage("2");
			inform.setFmessage("2");
			inform.setFnumber("2");
			inform.setFinformPeopleNum(new Integer(2));
			inform.setFparticipationNum(new Integer(2));
			inform.setFstartTime(new java.sql.Date(new Date().getTime()));
			inform.setFfinishTime(new java.sql.Date(new Date().getTime()));
			inform.setYBasicType(yBasicType);
		 inform.setFid("7bea7579-9c2e-466b-bb1c-b0e6fcd34441");
//		 groupsInformService.updateInform(inform,null);
	}
	@Test
	public void testDeleteGroupInform(){
		inform = new YBasicSocialgroupsinform();
		inform.setFid("c9c8b5b1-98d3-4d23-b868-5e33711a3b49");//这个的id是表中存在数据的id
		groupsInformService.deleteInform(inform);
		
	}
}
