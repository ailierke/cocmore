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
import com.yunzo.cocmore.core.function.service.AssurancecontentService;

/**
 * @author：jackpeng
 * @date：2014年11月28日上午11:09:53
 * 担保内容测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration 
@ContextConfiguration(classes={AppConfig.class})//使用servlet3.0xin特性使用这个类
public class AssurancecontentServiceImplTest {

	@Resource
	AssurancecontentService contentService;//供应担保内容
	
	
	@Test
	public void testFindAll() {
		List<YBasicAssurancecontent> list = contentService.findAll();
		System.out.println(list.size());
	}

	@Ignore
	@Test
	public void testGetById() {
		YBasicAssurancecontent content = contentService.getById("2271cdea-4d5c-4712-b369-f6cf2789947f");
		System.out.println(content.getFcontent());
	}

	@Ignore
	@Test
	public void testSave() {
		YBasicAssurancecontent content = new YBasicAssurancecontent();
		content.setFcontent("损坏赔偿");
		YBasicSocialgroups groups = new YBasicSocialgroups();
		groups.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		content.setYBasicSocialgroups(groups);
		contentService.save(content);
	}

	@Ignore
	@Test
	public void testDelete() {
		YBasicAssurancecontent content = contentService.getById("2271cdea-4d5c-4712-b369-f6cf2789947f");
		contentService.delete(content);
	}

	@Ignore
	@Test
	public void testUpdate() {
		YBasicAssurancecontent content = contentService.getById("2271cdea-4d5c-4712-b369-f6cf2789947f");
		content.setFcontent("损坏赔偿");
		contentService.update(content);
	}

}
