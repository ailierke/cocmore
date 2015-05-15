package com.yunzo.cocmore.core.function.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class YRoleauthorizationServiceTest {

	@Resource
	YRoleauthorizationService roleauthorizationService;
	@Before
	public void before(){

	}
	
	@Test
	public void testSave() {
//		try {
//			YRoleauthorization r=new YRoleauthorization();
//			YCharacter yc=new YCharacter();
//			yc.setFid("2");
////			YBasicOrganization yo=new YBasicOrganization();
////			yo.setFid("2");
//			YFunctionentriesFunctionjournalentry yf=new YFunctionentriesFunctionjournalentry();
//			yf.setFid("3");
//			YSystemconfigurationFunction fs=new YSystemconfigurationFunction();
//			fs.setFid("3");
//			
//			r.setFid("3");
//			r.setFisGroups("1");
//			r.setForganizationId("2");
//			//r.setYBasicOrganization(yo);
//			r.setYCharacter(yc);
//			r.setYFunctionentriesFunctionjournalentry(yf);
//			r.setYSystemconfigurationFunction(fs);
//			roleauthorizationService.save(r);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

	@Test
	public void testUpdate() {
//		try {
//			YRoleauthorization r=roleauthorizationService.getById("6");
//			r.setFisGroups("99");
//			roleauthorizationService.update(r);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

	@Test
	public void testDelete() {
//		try {
//			YRoleauthorization r=roleauthorizationService.getById("6");
//			
//			roleauthorizationService.delete(r);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void testFindByCharacterId() {
//		List<YRoleauthorization> list=roleauthorizationService.findByCharacterId("2");
//		System.out.println(list.size());
//		for(YRoleauthorization y:list){
//			System.out.println(y.getFisGroups());
//		}
	}
	@Test
	public void testfindByHql(){
//		List<YRoleauthorization> list=roleauthorizationService.findByHql("from YRoleauthorization as y where y.fisGroups='0'");
//		//System.out.println(list.size());
//		for(YRoleauthorization y:list){
//			System.out.println(y.getFisGroups());
//		}
		
		List<String> ss=new ArrayList<String>();
		ss.add("1,2,3,4,5,6,7,8,9,0,11,55");
		ss.add("44,2,22,3,7,8,9,6,4,55,6,4,45");
		ss.add("88,8,7,9,4,4,45,6,1,2,8,5,2,7,3,0,75");
		
		HashSet<String> hSet=new HashSet();
		for(String s:ss){
			String[] h=s.split(",");
			for(String t:h){
				hSet.add(t);
			}
		}
		for(String g:hSet){
			System.out.println("       "+g);
		}
	}
}
