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
import com.yunzo.cocmore.core.function.model.mysql.FAwardSetting;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;

/**
 * @author：jackpeng
 * @date：2014年11月28日下午3:07:29
 * 抽奖设置测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class SettingServiceImplTest {

	@Resource
	SettingService settingService;
	
	@Ignore
	@Test
	public void testFindAll() {
		List<FAwardSetting> list = settingService.findAll();
		for(int i = 0;i < list.size();i++){
			System.out.println(list.get(i).getFprizeName());
		}
	}

	@Ignore
	@Test
	public void testGetById() {
		FAwardSetting setting = settingService.getById("dcf46575-0894-4e64-9822-a36e0e4e99d6");
		System.out.println(setting.getFawardName());
	}

	@Ignore
	@Test
	public void testSave() {
		FAwardSetting setting = new FAwardSetting();
		setting.setFawardName("5873278");
		setting.setFprizeName("3288742");
		YWallactivity tivity = new YWallactivity();
		tivity.setFid("6c976798-6dfc-4711-8842-db11309d4f0d");
		setting.setYWallactivity(tivity);
		settingService.save(setting);
	}

	//@Ignore
	@Test
	public void testDelete() {
		FAwardSetting setting = settingService.getById("fe03fffe-f619-4f6d-87e3-632564de7cda");
		settingService.delete(setting);
	}

	@Ignore
	@Test
	public void testUpdate() {
		FAwardSetting setting = settingService.getById("dcf46575-0894-4e64-9822-a36e0e4e99d6");
		setting.setFawardName("jiangxiangmingchen");
		settingService.update(setting);
	}

}
