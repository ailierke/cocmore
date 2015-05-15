package com.yunzo.cocmore.core.function.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yunzo.cocmore.core.function.model.mysql.YBasicVerification;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.RandomArray;
import com.yunzo.cocmore.utils.base.SMSUtils;
import com.yunzo.cocmore.utils.base.mail.MailSenderInfo;
import com.yunzo.cocmore.utils.base.mail.SimpleMailSender;
import com.yunzo.cocmore.utils.http.HttpClientUtil;

/**
 * Description: <用户控制层>. <br>
 * <p>
 * <用户控制层>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Resource(name = "userInfo")
	private UserService userService;

	/**
	 * 保存新用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/saveUser")
	@ResponseBody
	@SystemControllerLog(description ="保存新用户")
	public Map<String, Object> doNotNeedSessionAndSecurity_saveUser(@ModelAttribute("form")YSystemUsers user){
		logger.info("save YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userService.save(user);
			map.put("success", true);
			map.put("msg", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "保存失败");
		}
		return map;
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/updateUser")
	@ResponseBody
	@SystemControllerLog(description="修改用户方法")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateUser(@ModelAttribute("form")YSystemUsers user){
		logger.info("update YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userService.update(user);
			map.put("success", true);
			map.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "修改失败");
		}
		return map;
	}
	
	/**
	 * 删除用户
	 * @param fid
	 * @return
	 */
	@RequestMapping("/deleteUser")
	@ResponseBody
	@SystemControllerLog(description ="删除用户")
	public Map<String, Object> doNotNeedSessionAndSecurity_deleteUser(String fid){
		logger.info("delete YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			YSystemUsers user = userService.getById(fid);
			userService.delete(user);
			map.put("success", true);
			map.put("msg", "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "删除失败");
		}
		return map;
	}
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findUserById")
	@ResponseBody
	@SystemControllerLog(description ="根据ID查询数据")
	public Map<String, Object> doNotNeedSessionAndSecurity_findUserById(String fid){
		logger.info("YSystemUsers  findOrgById");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			YSystemUsers user = userService.getById(fid);
			map.put("success", true);
			map.put("obj", user);
			map.put("msg", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "查询失败");
		}
		return map;
	}
	
	/**
	 * 查询全部用户
	 * @return
	 */
	@RequestMapping("/findUserAll")
	@ResponseBody
	@SystemControllerLog(description ="查询全部用户")
	public Map<String, Object> doNotNeedSessionAndSecurity_findUserAll(@RequestParam(value="searchName", required=false)String searchName,@RequestParam(value="type", required=false)String type,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YSystemUsers  handleList");
		Map<String, Object> map = new HashMap<String, Object>();
		PagingList<YSystemUsers> list = null;
		try {
			list = userService.findAll(searchName,type,start,limit);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 检查用户账号是否已存在
	 * @param faccount
	 * @return
	 */
	@RequestMapping("/checkUserFaccount")
	@ResponseBody
	@SystemControllerLog(description ="检查用户账号是否已存在")
	public Map<String, Object> doNotNeedSessionAndSecurity_checkUserFaccount(String faccount){
		logger.info("checkUserFaccount YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(userService.checkByFaccount(faccount)){
				map.put("success", true);
				map.put("msg", "该账号可以使用");
			}else{
				map.put("success", false);
				map.put("msg", "该账号已经被使用");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "该账号已经被使用");
		}
		return map;
	}
	
	/**
	 * 检查用户输入的验证码是否正确
	 * @param faccount
	 * @return
	 */
	@RequestMapping("/checkInputCode")
	@ResponseBody
	@SystemControllerLog(description =" 检查用户输入的验证码是否正确")
	public Map<String, Object> doNotNeedSessionAndSecurity_checkInputCode(String inputCode,HttpSession session){
		logger.info("checkUserFaccount YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String code = (String) session.getAttribute("code");
			if(inputCode.equalsIgnoreCase(code)){
				map.put("success", true);
			}else{
				map.put("success", false);
				map.put("msg", "验证码输入错误");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "该账号已经被使用");
		}
		return map;
	}
	
	/**
	 * 修改密码时，检查用户原密码是否输入正确
	 * @param faccount
	 * @return
	 */
	@RequestMapping("/checkUserPassword")
	@ResponseBody
	@SystemControllerLog(description =" 修改密码时，检查用户原密码是否输入正确")
	public Map<String, Object> doNotNeedSessionAndSecurity_checkUserPassword(String fid,String password){
		logger.info("checkUserPassword YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(userService.checkUserPassword(fid, password)){
				map.put("success", true);
				map.put("msg", "原密码输入正确");
			}else{
				map.put("success", false);
				map.put("msg", "原密码输入不正确，请核对后正确输入！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "原密码输入不正确，请核对后正确输入！");
		}
		return map;
	}
	
	/**
	 * 修改用户状态
	 * @param user
	 * @return
	 */
	@RequestMapping("/updateUserStatus")
	@ResponseBody
	@SystemControllerLog(description ="  修改用户状态")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateUserStatus(@RequestParam("fids")String fids,@RequestParam("status")int status){
		logger.info("updateUserStatus YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for(String id : fids.split(",")){
				YSystemUsers obj = userService.getById(id);
				obj.setFbillState(status);
				userService.update(obj);
			}
			map.put("success", true);
			map.put("msg", "修改状态成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "修改状态失败");
		}
		return map;
	}
	
	/**
	 * 修改用户密码
	 * @param user
	 * @return
	 */
	@RequestMapping("/updateUserPassword")
	@ResponseBody
	@SystemControllerLog(description ="修改用户密码")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateUserPassword(@RequestParam("fid")String fid,@RequestParam("newpwd")String newpwd){
		logger.info("updateUserStatus YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userService.updatePassword(fid, newpwd);
			map.put("success", true);
			map.put("msg", "修改密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "修改密码失败");
		}
		return map;
	}
	
	/**
	 * 发送邮箱验证
	 * @param user
	 * @return
	 */
	@RequestMapping("/sendMail")
	@ResponseBody
	@SystemControllerLog(description ="  发送邮箱验证")
	public Map<String, Object> doNotNeedSessionAndSecurity_sendMail(String faccount,String url){
		logger.info("sendMail YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		try {
				YSystemUsers user = userService.getByFaccount(faccount);
			 //这个类主要是设置邮件   
		      MailSenderInfo mailInfo = new MailSenderInfo();      
		      mailInfo.setValidate(true);      
		      mailInfo.setToAddress("724941972@qq.com");   
		      mailInfo.setSubject("找回密码");    
		      sb.append("尊敬的用户 <font style=\"font-weight:bold\">" + faccount + "</font>:<br/>");
		      sb.append(url + "changePass.jsp?fid="+user.getFid() + "<br/>");
		      sb.append("请点击链接，重新设置新的密码！");
		      mailInfo.setContent(sb.toString());    
		         //这个类主要来发送邮件   
		      SimpleMailSender sms = new SimpleMailSender();   
		      sms.sendHtmlMail(mailInfo);//发送文体格式
		      
		      map.put("success", true);
		      
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 发送手机验证
	 * @param user
	 * @return
	 */
	@RequestMapping("/sendTel")
	@ResponseBody
	@SystemControllerLog(description =" 发送手机验证")
	public Map<String, Object> doNotNeedSessionAndSecurity_sendTel(String faccount){
		logger.info("sendMail YSystemUsers");
		Map<String, Object> map = new HashMap<String, Object>();
		if(userService.sendTel(faccount) == 1){
			map.put("success", true);
		}else{
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 发送手机验证
	 * @param user
	 * @return
	 */
	@RequestMapping("/checkCode")
	@ResponseBody
	public Map<String, Object> doNotNeedSessionAndSecurity_checkCode(String faccount,String code){
		logger.info("checkCode YSystemUsers");
		return userService.checkCode(faccount, code);
	}
	
	
	

	/**
	 * 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 * <@AuthenticationPrincipal,负责绑定验证后的用户>
	 * </p>
	 * 
	 * @param user
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/{id}.do")
	public String userinfo(@PathVariable("id") String id, Model model) {
		logger.info("user02  id==" + id);
		model.addAttribute("id", id);
		return "User";
	}

//	@RequestMapping("/findall")
//	public String handleList(Model model) {
//		logger.info("userinfo  handleList");
//		List<YSystemUsers> list = userService.findAll();
//		model.addAttribute("results", list);
//		return "User";
//	}

	@RequestMapping(value = "/xml/write/{id}.xml", produces = { MediaType.APPLICATION_XML_VALUE
			+ ";charset=UTF-8" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public YSystemUsers handleWriteXML(@PathVariable("id") String fid, Model model) {
		logger.info("handleWriteXML  id==" + fid);
		YSystemUsers user = userService.getById(fid);

		// @DeclareParents
		// UserDeclareParentsService declareParents =
		// (UserDeclareParentsService)userService;
		logger.info("用记帐号：" + user.getFaccount());

		return user;
	}

//	@RequestMapping(value = "/xml/write/{id}/{name}.do", produces = { MediaType.APPLICATION_XML_VALUE
//			+ ";charset=UTF-8" })
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	public YSystemUsers handleWriteXMLDo(@PathVariable("id") String faccount,
//			@PathVariable("name") String fuserPassword, Model model) {
//		logger.info("handleWriteXMLDo  id==" + faccount);
//		YSystemUsers user = userService.getByNameAndPassWord(faccount, fuserPassword);
//		userService.save(user);// 捕获异常
//		return user;
//	}

	@RequestMapping(value = "/json/write/{id}.json", produces = { MediaType.APPLICATION_JSON_VALUE
			+ ";charset=UTF-8" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public YSystemUsers handleWriteJSON(@PathVariable("id") String id, Model model) {
		logger.info("handleWriteJSON  id==" + id);
		YSystemUsers user = userService.getById(id);
		return user;
	}

	@RequestMapping("/jsonmock")
	public void handleJsonMock() {
		// 因为使用了属性拦截器对日期字符串yyyy-MM-dd自动转换为date类型
		String requestBody = "{\"fid\":\"12\",\"faccount\":\"admin\",\"fpassword\":\"wqvbkb\",\"fbillstate\":1\"}";
		try {
			String result = HttpClientUtil.httpPost(
					"http://localhost:8080/cocmore-web/userrest/save",
					requestBody, MediaType.APPLICATION_JSON_VALUE, "utf-8");
			logger.info("result======" + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/xmlmock")
	public void handleXmlMock() {
		// 因为使用了属性拦截器对日期字符串yyyy-MM-dd自动转换为date类型
		String requestBody = "<user><fid>15</fid><faccount>skykill</faccount><fpassword>123456</fpassword><fbillstate>0</fbillstate></user>";
		try {
			String result = HttpClientUtil.httpPost(
					"http://localhost:8080/cocmore-web/userrest/savexml",
					requestBody, MediaType.APPLICATION_XML_VALUE, "utf-8");
			logger.info("result======" + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/flashcache")
	public void handleFlashCache() {
		logger.info("==========handleFlashCache==============");
		userService.delete(new YSystemUsers());

	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String handleSave(YSystemUsers demo, Model model) {
		logger.info("==========handleSave==============");
		userService.save(demo);
		model.addAttribute("user", demo);
		model.addAttribute("fid", demo.getFid());
		return "demo";

	}

	@RequestMapping(value = "/presave")
	public String handlePreSave(Model model) {
		logger.info("==========handlePreSave==============");

		return "savedemo";

	}

}
