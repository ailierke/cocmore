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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMenu;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class YSystemconfigurationMenuServiceTest {

	@Resource
	YSystemconfigurationMenuService menuService;
	@Before
	public void before(){

	}
	@Test
	public void testSave() {
//		YSystemconfigurationMenu m=new YSystemconfigurationMenu();
//		m.setFid("99");
//		m.setFmenuName("测试");
//		m.setFnumber("88888");
//		menuService.save(m);
	}

	@Test
	public void testUpdate() {
//		YSystemconfigurationMenu m=menuService.getById("99");
//		m.setFmenuName("123456");
//		menuService.update(m);
	}

	@Test
	public void testDelete() {
//		YSystemconfigurationMenu m=menuService.getById("100");
//		menuService.delete(m);
	}

	@Test
	public void testFindAll() {
//		List<YSystemconfigurationMenu> list=menuService.findAll();
//		for(YSystemconfigurationMenu m:list){
//			System.out.println(m.getFmenuName());
//		}
	}

	@Test
	public void testFindByHql() {
//		List<YSystemconfigurationMenu> list=menuService.findByHql("from YSystemconfigurationMenu as y where y.fsuperiorId='"+6+"'");
//		for(YSystemconfigurationMenu m:list){
//			System.out.println(m.getFmenuName());
//		}
	}

}
