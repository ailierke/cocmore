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
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDistrict;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsAboutServiceImplTest{
	Logger logger = Logger.getLogger(GroupsAboutServiceImplTest.class);
	@Resource
	GroupsAboutService groupsAboutService;
	public YBasicSocialgroupsabout groupabout;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroup() {
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		
		groupabout  = new YBasicSocialgroupsabout();
		groupabout.setContent("1");
		groupabout.setTitle("1");
		groupabout.setYBasicSocialgroups(yBasicSocialgroups);
		groupabout.setUpdateTime(new java.sql.Date(new Date().getTime()));
		groupsAboutService.addGroupsAbout(groupabout);
	}
	@Test
//	@Ignore
	public void testFindAllGroup(){
		groupsAboutService.getAllGroupsPagingList(null, null, "6f4f07da-1317-44a1-a467-366aa9068db6");
	}
	@Test
//	@Ignore
	public void testUpdateGroup(){
		YBasicSocialgroups yBasicSocialgroups=  new YBasicSocialgroups();yBasicSocialgroups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		
		 groupabout  = new YBasicSocialgroupsabout();
		 groupabout.setContent("2");
		 groupabout.setTitle("3");
		 groupabout.setYBasicSocialgroups(yBasicSocialgroups);
		 groupabout.setUpdateTime(new java.sql.Date(new Date().getTime()));
		 groupabout.setFid("7b53ccba-223d-419c-9689-7016af81c163");
		 groupsAboutService.updateGroupsAbout(groupabout);
	}
	@Test
	public void testFindGroupAbout(){
		groupabout = new YBasicSocialgroupsabout();
		groupabout.setFid("7b53ccba-223d-419c-9689-7016af81c163");
		groupsAboutService.findGroupsAbout(groupabout);
	}
	@Test
	public void testDeleteGroupAbout(){
		groupabout = new YBasicSocialgroupsabout();
		groupabout.setFid("9b1f9a6e-7fc9-4420-9e33-1f1179424d93");//这个的id是表中存在数据的id
		groupsAboutService.deleteGroupsAbout(groupabout);
		
	}
}
