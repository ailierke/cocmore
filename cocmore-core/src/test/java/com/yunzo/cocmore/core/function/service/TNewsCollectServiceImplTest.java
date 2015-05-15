package com.yunzo.cocmore.core.function.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.util.PagingList;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class TNewsCollectServiceImplTest {

	@Resource
	TNewsCollectService newsCollectService;
	@Before
	public void before(){
		
	}
	
	@Test
	public void testSave() {
//		TNewsCollect newcollect=new TNewsCollect();
//		TNewsHeadline n=new TNewsHeadline();
//		n.setFtid("1");
//		newcollect.setFtid("4");
//		newcollect.setFtel("66666666");
//		newcollect.setFcreateTime(new Date());
//		newcollect.setTNewsHeadline(n);
//		newsCollectService.save(newcollect);
	}

	@Test
	public void testUpdate() {
//		TNewsCollect nc=newsCollectService.getById("5");
//		nc.setFtel("测试");
//		newsCollectService.update(nc);
	}

	@Test
	public void testDelete() {
//		TNewsCollect nc=newsCollectService.getById("5");
//		newsCollectService.delete(nc);
	}

	@Test
	public void testFindAll() {
//		List<TNewsCollect> list=newsCollectService.findAll();
//		for(TNewsCollect c:list){
//			System.out.println(c.getFtel());
//		}
	}

	@Test
	public void testGetAllDynamicPagingList() {
		PagingList<TNewsCollect> list = newsCollectService.getAllDynamicPagingList(0,3);
		for(TNewsCollect c:list.getList()){
			System.out.println(c.getFtel());
		}
	}

}
