package com.yunzo.cocmore.core.function.controller;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.User;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.UserService;

@RestController
@RequestMapping("/userrest")
public class UserRestController {
	private static final Logger logger = Logger
			.getLogger(UserController.class);
	
	@Resource(name="userInfo")
	private UserService userService;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
	@SystemControllerLog(description="DemoRestController findById id")
	public YSystemUsers findById(@PathVariable("id") String id) {
		logger.info("DemoRestController findById id==" + id);
		return userService.getById("11");
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8",MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8"})
	@SystemControllerLog(description="save YSystemUsers")
	public YSystemUsers save(@RequestBody YSystemUsers demo) {
		// update by id
		userService.save(demo);
		return demo;
	}
	
	@RequestMapping(value = "/savexml", method = RequestMethod.POST, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8"})
	public YSystemUsers saveXml(@RequestBody YSystemUsers demo) {
		// update by id
		userService.save(demo);
		return demo;
	}
}
