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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class YFunctionentriesFunctionjournalentryServiceTest {

	@Resource
	YFunctionentriesFunctionjournalentryService functionentriesFunctionjournalentryService;
	@Before
	public void before(){

	}
	
	@Test
	public void testSave() {
//		YFunctionentriesFunctionjournalentry f=new YFunctionentriesFunctionjournalentry();
//		YSystemconfigurationFunction sf=new YSystemconfigurationFunction();
//		sf.setFid("1");
//		f.setYSystemconfigurationFunction(sf);
//		f.setFid("7");
//		f.setFfunctionName("6");
//		f.setFseq("6");
//		f.setFremark("6");
//		f.setFjscode("ddsfsfsfsdfds");
//		functionentriesFunctionjournalentryService.save(f);
	}
	@Test
	public void testUpdate(){
//		YFunctionentriesFunctionjournalentry f=functionentriesFunctionjournalentryService.getById("6");
//		YSystemconfigurationFunction sf=new YSystemconfigurationFunction();
//		sf.setFid("1");
//		f.setYSystemconfigurationFunction(sf);
//		f.setFremark("000000");
//		functionentriesFunctionjournalentryService.update(f);
	}
	
	@Test
	public void testDelete() {
//		YFunctionentriesFunctionjournalentry f=functionentriesFunctionjournalentryService.getById("5");
//		functionentriesFunctionjournalentryService.delete(f);
	}
	@Test
	public void findByHql(){
//		List<YFunctionentriesFunctionjournalentry> list=functionentriesFunctionjournalentryService.findByHql("from YFunctionentriesFunctionjournalentry as y where y.YSystemconfigurationFunction.fid='1'");
//		for(YFunctionentriesFunctionjournalentry f:list){
//			System.out.println(f.getFfunctionName());
//		}
	}
	@Test
	public void findAll(){
//		List<YFunctionentriesFunctionjournalentry> list=functionentriesFunctionjournalentryService.findAll();
//		for(YFunctionentriesFunctionjournalentry f:list){
//			System.out.println(f.getFfunctionName());
//		}
	}
	@Test
	public void testFindByFunctionID() {
		List<YFunctionentriesFunctionjournalentry> list=functionentriesFunctionjournalentryService.findByFunctionID("1");
		for(YFunctionentriesFunctionjournalentry f:list){
			System.out.println(f.getFfunctionName());
		}
	}

}
