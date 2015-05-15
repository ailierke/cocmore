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
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.util.PagingList;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
//使用servlet2.0使用配置文件@ContextConfiguration({"classpath:config/context/applicationContext-AppConfig.xml","classpath:config/context/applicationContext-CachingConfig.xml","classpath:config/context/applicationContext-DaoConfig.xml","classpath:config/context/applicationContext-DataSourceConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml","classpath:config/context/applicationContext-MvcConfig.xml" })
//@Transactional  
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true) 
public class TNewsHeadlineServiceTest {

	@Resource
	TNewsHeadlineService newsHeadlineService;
	@Before
	public void before(){
		
	}
	@Test
	public void testSave() {
//		TNewsHeadline nh=new TNewsHeadline();
//		nh.setFtid("5");
//		nh.setFtitle("测试1");
//		nh.setFimageUrl("12231");
//		nh.setFtImageUrl("123212213");
//		nh.setFnewsContent("wewiojrnsnfkdsfn,msnfsnfksfsnfsfdsjfskf");
//		nh.setFsource("1010100011011");
//		nh.setFreleaseTime(new Date());
//		nh.setFdetailsUrl("21322132312331232");
//		nh.setFclassification("8978984631321");
//		nh.setFdescribe("99999999999999999999");
//		nh.setFtype(99);
//		nh.setFcreateTime(new Date());
//		nh.setFisPush(6666);
//		newsHeadlineService.save(nh);
		
	}

	@Test
	public void testUpdate() {
//		TNewsHeadline nh=newsHeadlineService.getById("5");
//		nh.setFtitle("修改测试142");
//		newsHeadlineService.update(nh);
	}

	@Test
	public void testDelete() {
//		TNewsHeadline nh=newsHeadlineService.getById("5");
//		newsHeadlineService.delete(nh);
	}

	@Test
	public void testFindAll() {
//		List<TNewsHeadline>  list=newsHeadlineService.findAll();
//		for(TNewsHeadline n:list){
//			System.out.println(n.getFtitle());
//		}
	}

//	@Test
//	public void testGetAllDynamicPagingList() {
//		PagingList<TNewsHeadline> list=newsHeadlineService.getAllDynamicPagingList(0, 3);
//		for(TNewsHeadline n:list.getList()){
//			System.out.println(n.getFtitle());
//		}
//	}

}
