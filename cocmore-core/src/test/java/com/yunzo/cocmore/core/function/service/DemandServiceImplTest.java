package com.yunzo.cocmore.core.function.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;

/**
 * @author：jackpeng
 * @date：2014年11月28日下午12:15:17
 * 需求测试
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
public class DemandServiceImplTest {

	@Resource
	DemandService demandService;
	
	//@Ignore
	@Test
	public void testFindAll() {
		List<YBasicSocialgroupsdemand> list = demandService.findAll();
		System.out.println(list.size());
	}
	
	@Ignore
	@Test
	public void testGetById() {
		YBasicSocialgroupsdemand demand = demandService.getById("1412739b-0ca2-48db-8030-d9ad0fde3ee7");
		System.out.println(demand.getFheadline());
	}

	@Ignore
	@Test
	public void testSave() {
		YBasicSocialgroupsdemand demand = new YBasicSocialgroupsdemand();
		demand.setFnumber("2");
		demand.setFheadline("情歌王");
		demand.setFimages("图片2");
		YBasicSocialgroups group = new YBasicSocialgroups();
		group.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		demand.setYBasicSocialgroups(group);
		demandService.save(demand);
	}

	@Ignore
	@Test
	public void testDelete() {
		YBasicSocialgroupsdemand demand = demandService.getById("8e71931d-8191-4517-a32b-ff1853c9a4f2");
		demandService.delete(demand);
	}

	@Ignore
	@Test
	public void testUpdate() {
		YBasicSocialgroupsdemand demand = demandService.getById("8e71931d-8191-4517-a32b-ff1853c9a4f2");
		demand.setFnumber("2");
		demandService.update(demand);
	}

	@Ignore
	@Test
	public void testGetByHql() {
		String hql = "from YBasicSocialgroupsdemand y where y.fheadline like '%情%'";
		List<YBasicSocialgroupsdemand> list = demandService.getByHql(hql);
		System.out.println(list.size());
	}

}
