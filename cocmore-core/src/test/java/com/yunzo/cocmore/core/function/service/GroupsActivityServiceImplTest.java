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
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsactivity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsActivityServiceImplTest{
	Logger logger = Logger.getLogger(GroupsActivityServiceImplTest.class);
	@Resource
	GroupsActivityService groupsActivityService;
	public YBasicSocialgroupsactivity activity;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroupActivity() {
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		YBasicType yBasicType = new YBasicType();yBasicType.setFid("Fid");
		activity  = new YBasicSocialgroupsactivity();
		activity.setFbillState(0);
		activity.setYBasicSocialgroups(yBasicSocialgroups);
		activity.setFcomment("1");
		activity.setFheadline("1");
		activity.setFmessage("1");
		activity.setFnumber("1");
		activity.setFimages("1");
		activity.setFsponsor("1");
		activity.setFsite("1");
		activity.setFpeopleNum(new Integer(1));
		activity.setFstartTime(new java.sql.Date(new Date().getTime()));
		activity.setFfinishTime(new java.sql.Date(new Date().getTime()));
		activity.setYBasicType(yBasicType);
		groupsActivityService.addActivity(activity);
	}
	@Test
//	@Ignore
	public void testFindAllGroup(){
		groupsActivityService.getAllActivityPagingList(null, null, "6f4f07da-1317-44a1-a467-366aa9068db6","");
	}
	@Test
//	@Ignore
	public void testUpdateGroup(){
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		 activity  = new YBasicSocialgroupsactivity();
		 YBasicType yBasicType = new YBasicType();yBasicType.setFid("Fid");
		 activity.setFbillState(1);
			activity.setYBasicSocialgroups(yBasicSocialgroups);
			activity.setFcomment("2");
			activity.setFheadline("2");
			activity.setFmessage("2");
			activity.setFnumber("2");
			activity.setFimages("2");
			activity.setFsponsor("1");
			activity.setFsite("2");
			activity.setFpeopleNum(new Integer(2));
			activity.setFstartTime(new java.sql.Date(new Date().getTime()));
			activity.setFfinishTime(new java.sql.Date(new Date().getTime()));
			activity.setYBasicType(yBasicType);
		 activity.setFid("bca38dc2-c24d-4740-8b07-28eb20c5f8e3");
		 groupsActivityService.updateActivity(activity);
	}
	@Test
	public void testDeleteGroupActivity(){
		activity = new YBasicSocialgroupsactivity();
		activity.setFid("bca38dc2-c24d-4740-8b07-28eb20c5f8e3");//这个的id是表中存在数据的id
		groupsActivityService.deleteActivity(activity);
		
	}
}
