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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class YSystemconfigurationFunctionServiceTest {

	@Resource
	YSystemconfigurationFunctionService systemconfigurationFunctionService;
	@Before
	public void before(){

	}
	
	@Test
	public void testSave() {
//		YSystemconfigurationFunction f=new YSystemconfigurationFunction();
//		f.setFid("6");
//		f.setFnumber("1111245");
//		f.setFaccount("000000134");
//		f.setFremark("测试数据");
//		systemconfigurationFunctionService.save(f);
	}

	@Test
	public void testDelete() {
//		YSystemconfigurationFunction f=systemconfigurationFunctionService.getById("6");
//		systemconfigurationFunctionService.delete(f);
	}
	@Test
	public void testupdate(){
//		YSystemconfigurationFunction f=systemconfigurationFunctionService.getById("5");
//		f.setFnumber("6666666666");
//		f.setFaccount("tttttttttttttttt");
//		systemconfigurationFunctionService.update(f);
	}
	
	@Test
	public void findByHql(){
//		List<YSystemconfigurationFunction> list=systemconfigurationFunctionService.findByHql("from YSystemconfigurationFunction as y where y.fnumber like '%1%'");
//		for(YSystemconfigurationFunction f:list){
//			System.out.println(f.getFnumber());
//		}
	}
	@Test
	public void findAll(){
//		List<YSystemconfigurationFunction> list=systemconfigurationFunctionService.findAll();
//		for(YSystemconfigurationFunction f:list){
//			System.out.println(f.getFnumber());
//		}
	}
}
