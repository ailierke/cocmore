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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupscontact;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsContactServiceImplTest{
	Logger logger = Logger.getLogger(GroupsContactServiceImplTest.class);
	@Resource
	GroupsContactService groupsContactService;
	public YBasicSocialgroupscontact groupcontact;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroupContact() {
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		
		groupcontact  = new YBasicSocialgroupscontact();
		groupcontact.setAdress("1");
		groupcontact.setMail("1");
		groupcontact.setTell("1");
		groupcontact.setUri("1");
		groupcontact.setWechat("1");
		groupcontact.setYBasicSocialgroups(yBasicSocialgroups);
		groupcontact.setUpdateTime(new java.sql.Date(new Date().getTime()));
		groupsContactService.addGroupsContact(groupcontact);
	}
	@Test
//	@Ignore
	public void testFindAllGroupContact(){
		groupsContactService.getAllGroupsPagingList(null, null, "6f4f07da-1317-44a1-a467-366aa9068db6");
	}
	@Test
//	@Ignore
	public void testUpdateGroupContact(){
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		
		 groupcontact  = new YBasicSocialgroupscontact();
			groupcontact.setAdress("2");
			groupcontact.setMail("2");
			groupcontact.setTell("3");
			groupcontact.setUri("2");
			groupcontact.setWechat("2");
		 groupcontact.setYBasicSocialgroups(yBasicSocialgroups);
		 groupcontact.setUpdateTime(new java.sql.Date(new Date().getTime()));
		 groupcontact.setFid("f7ea4caf-1b52-4ee1-96a3-3ac8604b6e55");
		 groupsContactService.updateGroupsContact(groupcontact);
	}
	@Test
	public void testFindGroupContact(){
		groupcontact = new YBasicSocialgroupscontact();
		groupcontact.setFid("f7ea4caf-1b52-4ee1-96a3-3ac8604b6e55");
		groupsContactService.findGroupsContact(groupcontact);
	}
	@Test
	public void testDeleteGroupContact(){
		groupcontact = new YBasicSocialgroupscontact();
		groupcontact.setFid("f7ea4caf-1b52-4ee1-96a3-3ac8604b6e55");//这个的id是表中存在数据的id
		groupsContactService.deleteGroupsContact(groupcontact);
		
	}
}
