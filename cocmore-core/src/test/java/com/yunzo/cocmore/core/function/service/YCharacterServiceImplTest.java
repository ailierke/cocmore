package com.yunzo.cocmore.core.function.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDistrict;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.service.CharacterService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.util.PagingList;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class YCharacterServiceImplTest{
	@Resource
	CharacterService characterService;
	@Before
	public void before(){

	}
	
	@Test
	public void testAddGroup() {
//		List<YCharacter> list=null;
//		list=characterService.findAll();
//		System.out.println(list.size());
//		for(YCharacter c:list){
//			System.out.println(c.getFname());
//		}
	}
	@Test
	public void testsave(){
//		try {
//			YCharacter character=new YCharacter();
//			YBasicOrganization o=new YBasicOrganization();
//			o.setFid("1");
//			character.setFid("4");
//			character.setYBasicOrganization(o);
//			character.setFname("测试");
//			character.setFnumber("4");
//			character.setFstate(10);
//			character.setFremark("ssssssssssss");
//			character.setFcreaterId("1");
//			character.setFlag(6);
//			character.setFcreateTime(new Date());
//			characterService.save(character);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	@Test
	public void testupdate(){
//		YCharacter character=characterService.getById("4");
//		character.setFname("修改测试");
//		character.setFnumber("1231213123");
//		character.setFstate(8);
//		characterService.update(character);
	}
	@Test
	public void testdelete(){
//		YCharacter character=characterService.getById("5");
//		characterService.delete(character);
	}
	@Test
	public void testfindByOrganizationID(){
//		List<YCharacter> list=characterService.findByOrganizationID("1");
//		for(YCharacter c:list){
//			System.out.println(c.getFname());
//		}
	}
	@Test
	public void testupdateState(){
//		characterService.updateState("4", 11);
	}
	@Test
	public void testgetAllDynamicPagingList(){
//		PagingList<YCharacter> list = characterService.getAllDynamicPagingList(0,3);
//		for(YCharacter c:list.getList()){
//			System.out.println(c.getFname());
//		}
		
//		String images="1231231313211,211545612315641,";
//		//String images=",211545612315641";
//		 String img[]= images.split(",");
//		 System.out.println("               "+img.length);
//		 System.out.println("     "+img[0]);
//		 for(String i:img){
//			 System.out.println(i);
//		 }
	}
}
