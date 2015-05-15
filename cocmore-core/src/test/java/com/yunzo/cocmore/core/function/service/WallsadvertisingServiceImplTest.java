package com.yunzo.cocmore.core.function.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yunzo.cocmore.core.config.AppConfig;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.model.mysql.YWallsadvertising;

/**
 * @author：jackpeng
 * @date：2014年11月28日下午6:15:17
 * 上墙广告测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class WallsadvertisingServiceImplTest {

	@Resource
	WallsadvertisingService singService;
	
	@Ignore
	@Test
	public void testGetByHql() {
		String hql = "from YWallsadvertising y where y.ftitle like '%1%'";
		List<YWallsadvertising> list = singService.getByHql(hql);
		System.out.println(list.size());
	}

	@Ignore
	@Test
	public void testSave() {
		YWallsadvertising sing = new YWallsadvertising();
		sing.setFtitle("111111");
		sing.setFcreationTime(new Date());
		YWallactivity activity = new YWallactivity();
		activity.setFid("6c976798-6dfc-4711-8842-db11309d4f0d");
		sing.setYWallactivity(activity);
		singService.save(sing);
	}

	@Ignore
	@Test
	public void testDelete() {
		YWallsadvertising sing = singService.getById("203be23d-6a58-4918-a51a-c3c517852c70");
		singService.delete(sing);
	}

	@Ignore
	@Test
	public void testGetById() {
		YWallsadvertising sing = singService.getById("f12e6a36-7c9a-44ed-935b-16d5b3df6638");
		System.out.println(sing.getFtitle());
	}

}
