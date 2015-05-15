package com.yunzo.cocmore.core.function.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform;
import com.yunzo.cocmore.core.function.model.mysql.YReplytocomment;
import com.yunzo.cocmore.core.function.service.GroupsDynamicService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.ReplytocommentServiceI;

@Controller
@RequestMapping(value="/test")
public class TestController {
	@Resource
	private ReplytocommentServiceI replyTocommentService;
	private static final Logger logger = Logger.getLogger(CharacterController.class);
	@Resource(name="groupsInformService")
	GroupsInformService groupsInformService;
	
	@Resource(name="groupsDynamicService")
	GroupsDynamicService groupsDynamicService;
	/**
	 * 测试跳转到视图
	 * @param character
	 * @return
	 */
	@RequestMapping(value="/forword",method = RequestMethod.POST)
	@ResponseBody
	public String save(HttpServletResponse response, MultipartFile file){
		try {
			file.transferTo(new File("D:/xx.txt"));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "test2";
	}              
	
	/**
	 * 测试事物传播
	 * @param character
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/testTransaction")
	@Transactional(propagation=Propagation.REQUIRED)
	public String testTrancation(HttpServletResponse response) throws Exception{
		YBasicSocialgroupsinform inform  = new YBasicSocialgroupsinform();
		inform.setFbillState(0);
		inform.setFcomment("测试事物传播机制一");
		inform.setFdetailAddress("1");
		inform.setFheadline("1");
		inform.setFlogoImage("1");
		inform.setFmessage("1");
		inform.setFnumber("1");
		inform.setFinformPeopleNum(new Integer(1));
		inform.setFparticipationNum(new Integer(1));
		inform.setFstartTime(new java.sql.Date(new Date().getTime()));
		inform.setFfinishTime(new java.sql.Date(new Date().getTime()));
		groupsInformService.addInform(inform,null);
		YBasicSocialgroupsdynamic dynamic  = new YBasicSocialgroupsdynamic();
		dynamic.setFbillState(0);
		dynamic.setFcomment("测试事物传播机制二");
		dynamic.setFdetailAddress("1");
		dynamic.setFheadline("1");
		dynamic.setFlogoImage("1");
		dynamic.setFmessage("1");
		dynamic.setFnumber("1");
		groupsDynamicService.addDynamic(dynamic);
		logger.info("DynamicService save");
//		throw new RuntimeException();
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "ttxxgl";
		
	}
	
	
	/**
	 * 测试评论回复表里面的缓存机制
	 * @param character
	 * @return
	 */
	@RequestMapping(value="/saveCached")
	@ResponseBody
	@Cacheable(value="commonCache",key="#reply.fid")
	public  void save(YReplytocomment reply){
		replyTocommentService.save(reply);
	} 
	

	/**
	 * 测试评论回复表里面的缓存机制
	 * @param character
	 * @return
	 */
	@RequestMapping(value="/putCached",method=RequestMethod.POST)
	@ResponseBody
	public List<YReplytocomment> get(String id){
		YReplytocomment reply =replyTocommentService.findReplyById(id) ;
		List<YReplytocomment> list = new ArrayList<YReplytocomment>();
		list.add(reply);
		return list;
	} 
	/**
	 * 测试评论回复表里面的缓存机制
	 * @param character
	 * @return
	 */
	@RequestMapping(value="/delCached")
	@ResponseBody
	public void del(@RequestParam String id){
		replyTocommentService.del(id) ;
	} 
	/**
	 * 测试评论回复表里面的缓存机制
	 * @param character
	 * @return
	 */
	@RequestMapping(value="/updateCached")
	@ResponseBody
	public void update(YReplytocomment reply){
		replyTocommentService.update(reply);
	} 
}
