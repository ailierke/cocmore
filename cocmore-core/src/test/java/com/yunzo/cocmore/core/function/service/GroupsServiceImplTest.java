package com.yunzo.cocmore.core.function.service;


import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
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
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.service.GroupsService;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class GroupsServiceImplTest{
	Logger logger = Logger.getLogger(GroupsServiceImplTest.class);
	@Resource
	GroupsService groupsService;
	public YBasicSocialgroups group;
	@Before
	public void befoIanre(){
		
	}
	
	@Test
//	@Ignore
	public void testAddGroup() {
		YBasicCity yBasicCity=  new YBasicCity();yBasicCity.setFid("Fid");
		YBasicCounty yBasicCounty=  new YBasicCounty();yBasicCounty.setFid("Fid");
		YBasicDistrict yBasicDistrict=  new YBasicDistrict();yBasicDistrict.setFid("Fid");
		YBasicEmployee yBasicEmployee=  new YBasicEmployee();yBasicEmployee.setFid("Fid");
		YBasicOrganization yBasicOrganization=  new YBasicOrganization();yBasicOrganization.setFid("Fid");yBasicOrganization.setFregisteredCapital(new Double(1));
		YBasicType yBasicType=  new YBasicType();yBasicType.setFid("Fid");
		
		 group  = new YBasicSocialgroups();
		group.setFabbreviation("1");
		group.setFaddress("1");
		group.setFbillState(new Integer(1));
		group.setFclientContacts("1");
		group.setFcomment("1");
		group.setFcreaterId("1");
		group.setFcreateTime(new java.sql.Date(new Date().getTime()));
		group.setFemail("1");
		group.setFiphone("1");
		group.setFlag(new Integer(1));
		group.setFlastModifiedId("1");
		group.setFlastModifiedTime(new java.sql.Date(new Date().getTime()));
		group.setFleafNode("1");
		group.setFlevel("1");
		group.setFmodifiedId("1");
		group.setFmodifiedTime(new java.sql.Date(new Date().getTime()));
		group.setFname("jx");
		group.setFnumber("1");
		group.setFnumberPeople(new Integer(20));
		group.setFphone("1");
		group.setFpreviousNumber("1");
		group.setFregisterNum(new Integer(30));
		group.setFsignedTime(new java.sql.Date(new Date().getTime()));
		group.setFsource("1");
		group.setFsuperSocialGroupsId("1");
		group.setFynsigned("1");
		group.setYBasicCity(yBasicCity);
		group.setYBasicCounty(yBasicCounty);
		group.setYBasicDistrict(yBasicDistrict);
		group.setYBasicEmployee(yBasicEmployee);
		group.setYBasicOrganization(yBasicOrganization);
		group.setYBasicType(yBasicType);
		groupsService.addGroups(group);
	}
	@Test
//	@Ignore
	public void testFindAllGroup(){
		 groupsService.getAllGroupsPagingList(null, null,"6f4f07da-1317-44a1-a467-366aa9068db6", null, null);//查询group通过id
		 groupsService.getAllGroupsPagingList(1, 4, null, "Fid", null);//查询分页通过组织id
		 groupsService.getAllGroupsPagingList(1, 3, null, null, "jx");//查询分页通过团体name
	}
	@Test
//	@Ignore
	public void testUpdateGroup(){
		YBasicCity yBasicCity=  new YBasicCity();yBasicCity.setFid("Fid");
		YBasicCounty yBasicCounty=  new YBasicCounty();yBasicCounty.setFid("Fid");
		YBasicDistrict yBasicDistrict=  new YBasicDistrict();yBasicDistrict.setFid("Fid");
		YBasicEmployee yBasicEmployee=  new YBasicEmployee();yBasicEmployee.setFid("Fid");
		YBasicOrganization yBasicOrganization=  new YBasicOrganization();yBasicOrganization.setFid("Fid");yBasicOrganization.setFregisteredCapital(new Double(1));
		YBasicType yBasicType=  new YBasicType();yBasicType.setFid("Fid");
		
		 group  = new YBasicSocialgroups();
		group.setFabbreviation("2");
		group.setFaddress("2");
		group.setFbillState(new Integer(2));
		group.setFclientContacts("2");
		group.setFcomment("2");
		group.setFcreaterId("2");
		group.setFcreateTime(new java.sql.Date(new Date().getTime()));
		group.setFemail("2");
		group.setFiphone("2");
		group.setFlag(new Integer(2));
		group.setFlastModifiedId("2");
		group.setFlastModifiedTime(new java.sql.Date(new Date().getTime()));
		group.setFleafNode("2");
		group.setFlevel("2");
		group.setFmodifiedId("2");
		group.setFmodifiedTime(new java.sql.Date(new Date().getTime()));
		group.setFname("jx");
		group.setFnumber("2");
		group.setFnumberPeople(new Integer(20));
		group.setFphone("2");
		group.setFpreviousNumber("2");
		group.setFregisterNum(new Integer(30));
		group.setFsignedTime(new java.sql.Date(new Date().getTime()));
		group.setFsource("2");
		group.setFsuperSocialGroupsId("2");
		group.setFynsigned("2");
		group.setYBasicCity(yBasicCity);
		group.setYBasicCounty(yBasicCounty);
		group.setYBasicDistrict(yBasicDistrict);
		group.setYBasicEmployee(yBasicEmployee);
		group.setYBasicOrganization(yBasicOrganization);
		group.setYBasicType(yBasicType);
		group.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		groupsService.update(group);
	}
	@Test
	public void findGroup(){
		group.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		groupsService.findGroup(group);
	}
	
	@Test
	public void findAllGroupList(){
		groupsService.getAllGroupsPagingList(null, null, null, null, null);
	}
}
