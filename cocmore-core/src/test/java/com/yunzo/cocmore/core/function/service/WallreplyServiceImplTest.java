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
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.model.mysql.YWallreply;

/**
 * @author：jackpeng
 * @date：2014年11月28日下午5:46:21
 * 上墙回复测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class WallreplyServiceImplTest {

	@Resource
	WallreplyService replyService;
	
	@Ignore
	@Test
	public void testSave(){
		YWallreply reply = new YWallreply();
		reply.setFcontent("111111");
		reply.setFsum(111);
		YWallactivity activity = new YWallactivity();
		activity.setFid("6c976798-6dfc-4711-8842-db11309d4f0d");
		reply.setYWallactivity(activity);
		replyService.save(reply);
	}
	
	//@Ignore
	@Test
	public void testGetById() {
		YWallreply reply = replyService.getById("54fcef3f-aa0d-4133-8ae7-5c8a66179503");
		System.out.println(reply.getFcontent());
	}

	@Ignore
	@Test
	public void testGetByHql() {
		String hql = "from YWallreply y where y.fcontent like '%1%'";
		List<YWallreply> list = replyService.getByHql(hql);
		System.out.println(list.size());
	}

	@Ignore
	@Test
	public void testDelete() {
		YWallreply reply = replyService.getById("1fc7bc44-f514-47ce-a948-f95473179527");
		replyService.delete(reply);
	}

	@Ignore
	@Test
	public void testUpdate() {
		YWallreply reply = replyService.getById("54fcef3f-aa0d-4133-8ae7-5c8a66179503");
		reply.setFcontent("neirong");
		replyService.update(reply);
	}

}
