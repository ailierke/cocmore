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
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;

/**
 * @author：jackpeng
 * @date：2014年11月28日下午3:13:16
 * 供应测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes ={AppConfig.class})
public class SupplyServiceImplTest {

	@Resource
	SupplyService supplyService;
	
	//@Ignore
	@Test
	public void testFindAll() {
		List<YBasicSocialgroupssupply> list = supplyService.findAll();
		System.out.println(list.get(0).getYBasicSocialgroups().getFid());
	}

	@Ignore
	@Test
	public void testGetById() {
		YBasicSocialgroupssupply supply = supplyService.getById("307f9a81-b0d2-4ff2-860d-c4574d035c8e");
		System.out.println(supply.getFheadline());
	}

	@Ignore
	@Test
	public void testSave() {
		YBasicSocialgroupssupply supply = new YBasicSocialgroupssupply();
		supply.setFnumber("2");
		supply.setFheadline("灰太郎123456");
		supply.setFimages("图片2");
		YBasicAssurancecontent content = new YBasicAssurancecontent();
		content.setFid("0fb9641c-7193-4786-9ee2-2be794b19a3b");
		YBasicSocialgroups group = new YBasicSocialgroups();
		group.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		supply.setYBasicSocialgroups(group);
		supplyService.save(supply);
	}

	@Ignore
	@Test
	public void testDelete() {
		//ffa4cd3a-73f8-4534-8d18-9fcc64514552
		YBasicSocialgroupssupply supply = supplyService.getById("ffa4cd3a-73f8-4534-8d18-9fcc64514552");
		supplyService.delete(supply);
	}

	@Ignore
	@Test
	public void testUpdate() {
		YBasicSocialgroupssupply supply = supplyService.getById("307f9a81-b0d2-4ff2-860d-c4574d035c8e");
		supply.setFheadline("岁月太着急");
		supplyService.update(supply);
	}

	@Ignore
	@Test
	public void testGetByHql() {
		String hql = "from YBasicSocialgroupssupply y where y.fheadline like '%岁月%'";
		List<YBasicSocialgroupssupply> list = supplyService.getByHql(hql);
		for(int i = 0;i < list.size();i++){
			System.out.println(list.get(i).getFheadline());
		}
	}

}
