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
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;

/**
 * @author：jackpeng
 * @date：2014年11月28日下午4:20:12
 * 上墙活动测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={AppConfig.class})
public class WallActivityServiceImplTest {

	@Resource
	WallActivityService avtivityService;
	
	@Ignore
	@Test
	public void testFindAll() {
		List<YWallactivity> list = avtivityService.findAll();
		for(int i = 0;i < list.size();i++){
			System.out.println(list.get(i).getFtheme());
		}
	}

	@Ignore
	@Test
	public void testGetById() {
		YWallactivity activity = avtivityService.getById("6ada5dd8-e3cb-4a71-836f-f299104f71ba");
		System.out.println(activity.getYBasicSocialgroups().getFid());
	}

	@Ignore
	@Test
	public void testSave() {
		YWallactivity activity = new YWallactivity();
		activity.setFtheme("主题2222");
		activity.setFtheUrl("网址2");
		YBasicSocialgroups group = new YBasicSocialgroups();
		group.setFid("6f4f07da-1317-44a1-a467-366aa9068db6");
		activity.setYBasicSocialgroups(group);
		avtivityService.save(activity);
	}

	@Ignore
	@Test
	public void testDelete() {
		YWallactivity activity = avtivityService.getById("96756c84-5348-4b09-b43e-cec36d9be268");
		avtivityService.delete(activity);
	}

	@Ignore
	@Test
	public void testUpdate() {
		YWallactivity activity = avtivityService.getById("6ada5dd8-e3cb-4a71-836f-f299104f71ba");
		activity.setFtheme("zhuti");
		avtivityService.update(activity);
	}

	@Ignore
	@Test
	public void testGetByHql() {
		String hql = "from YWallactivity y where y.ftheme = 'zhuti'";
		List<YWallactivity> list = avtivityService.getByHql(hql);
		System.out.println(list.get(0).getFtheUrl());
	}

}
