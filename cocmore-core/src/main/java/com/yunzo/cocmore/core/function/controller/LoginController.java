package com.yunzo.cocmore.core.function.controller;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.utils.base.MD5Util;

/** 
 *Description: <用户登录控制层>. <br>
 * @date:2014年11月27日 下午4:52:38
 * @author beck
 * @version V1.0                             
 */
@Controller
public class LoginController {
private static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Resource(name = "userInfo")
	private UserService userService;
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	

	/**
	 * 用户登录
	 * @param faccount
	 * @param fuserPassword
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	@SystemControllerLog(description ="用户登录")
	public Map<String, Object> login(String faccount,String fuserPassword,String type,HttpSession session) {
		logger.info("login");
		Map<String, Object> map = new HashMap<String, Object>();
		YSystemUsers user = null;
		try {
			user = userService.getByNameAndPassWord(faccount, MD5Util.md5(BEGINSTRING + fuserPassword),type);
			if(user != null&&!user.equals("")){
				session.setAttribute("user", user);
				map.put("success", true);
				map.put("obj", user);
			}else{
				map.put("success", false);
				map.put("msg", "登录失败请检查你输入的信息是否有误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "用户名或密码不正确");
		}
		return map;
	}
}
