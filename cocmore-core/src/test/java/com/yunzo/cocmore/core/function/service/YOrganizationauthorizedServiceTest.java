package com.yunzo.cocmore.core.function.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class YOrganizationauthorizedServiceTest {

	@Resource
	YOrganizationauthorizedService organizationauthorizedService;
	@Before
	public void before(){

	}
	
	@Test
	public void testSave() {
//		try {
//			YOrganizationauthorized oa=new YOrganizationauthorized();
//			YSystemUsers su=new YSystemUsers();
//			su.setFid("1b5309c2-fbe2-47be-806b-039e7586766b");
//			YFunctionentriesFunctionjournalentry yf=new YFunctionentriesFunctionjournalentry();
//			yf.setFid("3");
//			YSystemconfigurationFunction sf=new YSystemconfigurationFunction();
//			sf.setFid("1");
//			
//			oa.setFid("9");
//			oa.setYFunctionentriesFunctionjournalentry(yf);
//			oa.setYSystemconfigurationFunction(sf);
//			oa.setYSystemUsers(su);
//			organizationauthorizedService.save(oa);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void testUpdate() {
//		try {
//			YOrganizationauthorized oa=organizationauthorizedService.getById("2");
//			YSystemUsers su=new YSystemUsers();
//			su.setFid("1d93d6a6-14e3-4c5d-9f04-1c344b365930");
//			oa.setYSystemUsers(su);
//			organizationauthorizedService.update(oa);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
	}

	@Test
	public void testDelete() {
//		try {
//			YOrganizationauthorized oa=organizationauthorizedService.getById("9");
//			organizationauthorizedService.delete(oa);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void testFindByUserID() {
//		try {
//			List<YOrganizationauthorized> list=organizationauthorizedService.findByUserID("1d93d6a6-14e3-4c5d-9f04-1c344b365930");
//			for(YOrganizationauthorized y:list){
//				System.out.println(y.getFid());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	@Test
	public void testfindByHql(){
//		try {
//			List<YOrganizationauthorized> list=organizationauthorizedService.findByHql("from YOrganizationauthorized as y where y.YSystemUsers.fid='1d93d6a6-14e3-4c5d-9f04-1c344b365930'");
//			for(YOrganizationauthorized y:list){
//				System.out.println(y.getFid());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
