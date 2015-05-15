package com.yunzo.cocmore.core.function.controller.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.service.AppdeviceServiceI;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MessageServiceI;
import com.yunzo.cocmore.core.function.service.MessagerecordServiceI;
import com.yunzo.cocmore.core.function.service.OrganizationService;
import com.yunzo.cocmore.core.function.util.Tree1;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;

/**
 * @author：jackpeng
 * @date：2015年3月2日下午5:14:15
 * 生活controller类
 */
@Controller
@RequestMapping("/life")
public class LifeProjectController {
	
	private static final Logger logger = Logger.getLogger(LifeProjectController.class);
	
	ObjectMapper objMapper = new ObjectMapper();
	//生活推送记录service
	@Resource(name = "lifePushService")
	private LifePushinfoSerciveI lifePushService;
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	@Resource
	GroupsInformService  groupsInformService;
	@Resource(name = "deviceService")
	private AppdeviceServiceI deviceService;
	
	@Resource(name = "msgService")
	private MessageServiceI msgService;
	
	@Resource(name = "messagerecordService")
	private MessagerecordServiceI messagerecordService;
	
	@Resource(name = "orgService")
	private OrganizationService orgService;
	
	@Resource(name = "groupsService")
	private GroupsService groupsService;
	
//	String url = "http://114.215.201.200:8085/admanagesystem-web/";
	
	/**
	 * 获取生活列表，并按点赞、评论、时间排序
	 * @param lifeTypeId 生活类型id
	 * @param searchName 查询条件
	 * @param type 请求类型（0、按点赞排序；1、按评论排序；2、按时间排序）
	 * @return
	 */
	@RequestMapping("/findLifeProjectPage")
	@ResponseBody
	@SystemControllerLog(description = "获取生活列表，并按点赞、评论、时间排序")
	public Map<String, Object> findLifeProjectPage(String lifeTypeId,String type,String start,String limit,HttpServletRequest request){
		 
		Map<String, Object> map = new HashMap<String, Object>();
		
		//先将参数放入List，再对参数进行URL编码  
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("lifeTypeId", lifeTypeId));
		params.add(new BasicNameValuePair("start", start));
		params.add(new BasicNameValuePair("limit", limit));
		  
		//对参数编码  
		String param = URLEncodedUtils.format(params, "UTF-8");  
		
		//baseUrl
		String baseUrl = null;
		//判断排序类型（0、按点赞排序；1、按评论排序；2、按时间排序）
		if(type.equals("0")){
			baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProject!doNotNeedSessionAndSecurity_findLifeProjectPraisePage.ad";
		}else if(type.equals("1")){
			baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProject!doNotNeedSessionAndSecurity_findLifeProjectCommentPage.ad";
		}else if(type.equals("2")){
			baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProject!doNotNeedSessionAndSecurity_findLifeProjectTimePage.ad";
		}
		
		//将URL与参数拼接  
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);  
		
		HttpClient httpClient = new DefaultHttpClient();  
		
		try {  
			HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
			int code = response.getStatusLine().getStatusCode();   //获取响应码
			if(code == 200){
				String str = EntityUtils.toString(response.getEntity(), "utf-8");  //获取服务器响应内容
			    JsonNode root = objMapper.readTree(str);  
			    JsonNode rows = root.path("rows"); 
			    JsonNode total = root.path("total");
			    map.put("obj", rows);
			    map.put("total", total);
				map.put("msg", "查询成功！");
				map.put("success", true);
			}
		} catch (Exception e) {  
		    e.printStackTrace();
		    map.put("msg", "查询失败！");
			map.put("success", false);  
		}
		return map;
	}
	
	/**
	 * 查询生活项目分类
	 * @return
	 */
	@RequestMapping("/findLifeProjectType")
	@ResponseBody
	@SystemControllerLog(description = "查询生活项目分类")
	public Map<String, Object> findLifeProjectType(HttpServletRequest request){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//先将参数放入List，再对参数进行URL编码  
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		  
		//对参数编码  
		String param = URLEncodedUtils.format(params, "UTF-8");  
//		String baseUrl = url + "base/lifeProjectType!doNotNeedSessionAndSecurity_findLifeProjectTypeAvailable.ad";
		//baseUrl
		String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProjectType!doNotNeedSessionAndSecurity_findLifeProjectTypeAvailable.ad";
		
		//将URL与参数拼接  
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);  
		
		HttpClient httpClient = new DefaultHttpClient();  
		
		try {  
			HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
			int code = response.getStatusLine().getStatusCode();   //获取响应码
			if(code == 200){
				String str = EntityUtils.toString(response.getEntity(), "utf-8");  //获取服务器响应内容
			    JsonNode root = objMapper.readTree(str);  
			    JsonNode rows = root.path("rows"); 
			    map.put("obj", rows);
				map.put("msg", "查询成功！");
				map.put("success", true);
			}
		} catch (Exception e) {  
		    e.printStackTrace();
		    map.put("msg", "查询失败！");
			map.put("success", false);  
		}
		return map;
	}
	
	
	/**
	 * 推送消息
	 * @param lifeId 生活id
	 * @param memberIds 会员id用逗号分割
	 * @return
	 */
	@RequestMapping("/pushLifeProject")
	@ResponseBody
	@SystemControllerLog(description = "生活推送消息")
	public Map<String, Object> pushLifeProject(String lifeIds,String memberIds,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		//先将参数放入List，再对参数进行URL编码  
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("lifeIds", lifeIds));
		//8a2169344838f63e01483e4776d1340f,8a216934491e6d74014931fe568220ff,8a2169344a32b132014a3cb6746b6812
		
		//对参数编码  
		String param = URLEncodedUtils.format(params, "UTF-8");  
		//baseUrl
		String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProject!doNotNeedSessionAndSecurity_findLifeProjectId.ad";
		
		//将URL与参数拼接  
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);  
		
		HttpClient httpClient = new DefaultHttpClient();  
		
		try {  
			HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
			int code = response.getStatusLine().getStatusCode();   //获取响应码
			if(code == 200){
				String str = EntityUtils.toString(response.getEntity(), "utf-8");  //获取服务器响应内容
			    JsonNode root = objMapper.readTree(str);  
			    JsonNode objs = root.path("obj");
			    for(JsonNode obj:objs){
			    	//获取项目标题
			    	String LifeheadLine = obj.get("fheadline").textValue();
			    	String lifeId = obj.get("fid").textValue();
			    	//调用推送方法
				    pushLife(memberIds, LifeheadLine,lifeId);
			    }
				map.put("msg", "推送成功！");
				map.put("success", true);
			}else{
				map.put("msg", "推送失败！");
				map.put("success", false);
			}
		} catch (Exception e) {  
		    e.printStackTrace();
		    map.put("msg", "推送失败！");
			map.put("success", false);  
		}
		return map;
	}
	
	
	/**
	 * 推送
	 * @param memberIds 会员id用逗号分割
	 * @param LifeheadLine 生活标题
	 * @param lifeId 
	 */
	public void pushLife(String memberIds,String LifeheadLine, String lifeId){
		String[] strs = memberIds.split(",");
		for(String memberId:strs){
			//获取会电话
			String hqlMember = "select y.fmobilePhone from YBasicMember y where y.fid = '"+memberId+"'";
			List<String> tels = memberService.getByHql(hqlMember);
			Set<String> tesSet = new HashSet<String>(tels);
			if(tels!=null&&tels.size()>0){
				Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(tesSet);
				/**
				 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
				 */
				PushThread pushThread = new PushThread(new Integer(3), deviceIdMap, "有新产品上架了", lifeId, null,lifePushService);
				pushThread.start();
			
			}else{
				logger.info("没有登录过的设备信息....");
			}
		}
	}
	
	/**
	 * 查询生活项目评论，并分页
	 * @param lifeProjectId 生活项目id
	 * @return
	 */
	@RequestMapping("/findLifeProjectComment")
	@ResponseBody
	@SystemControllerLog(description = "查询生活项目评论，并分页")
	public Map<String, Object> findLifeProjectComment(String lifeProjectId,String start,String limit,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//先将参数放入List，再对参数进行URL编码  
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("lifeProjectId", lifeProjectId));
		params.add(new BasicNameValuePair("start", start));
		params.add(new BasicNameValuePair("limit", limit));
		//对参数编码  
		String param = URLEncodedUtils.format(params, "UTF-8");  
		
		//baseUrl
		String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProjectComment!doNotNeedSessionAndSecurity_findLifeProjectCommentPage.ad";
		
		//将URL与参数拼接  
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);  
		
		HttpClient httpClient = new DefaultHttpClient();  
		
		try {  
			HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
			int code = response.getStatusLine().getStatusCode();   //获取响应码
			if(code == 200){
				String str = EntityUtils.toString(response.getEntity(), "utf-8");  //获取服务器响应内容
			    JsonNode root = objMapper.readTree(str);  
			    JsonNode rows = root.path("rows");
			    JsonNode total = root.path("total");
			    map.put("obj", rows);
			    map.put("total", total);
				map.put("msg", "查询成功！");
				map.put("success", true);
			}
		} catch (Exception e) {  
		    e.printStackTrace();
		    map.put("msg", "查询失败！");
			map.put("success", false);  
		}
		return map;
	}
	
	/**
	 * 删除生活项目评论
	 * @param commentIds 项目id用逗号分隔
	 * @return
	 */
	@RequestMapping("/deleteLifeProjectComment")
	@ResponseBody
	@SystemControllerLog(description = "删除生活项目评论")
	public Map<String, Object> deleteLifeProjectComment(String commentIds,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//先将参数放入List，再对参数进行URL编码  
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("fids", commentIds));
		  
		//对参数编码  
		String param = URLEncodedUtils.format(params, "UTF-8");  
		
		//baseUrl
		String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admanagesystem-web/base/lifeProjectComment!doNotNeedSessionAndSecurity_deleteLifeProjectComment.ad";
		
		//将URL与参数拼接  
		HttpGet getMethod = new HttpGet(baseUrl + "?" + param);  
		
		HttpClient httpClient = new DefaultHttpClient();  
		
		try {  
			HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
			int code = response.getStatusLine().getStatusCode();   //获取响应码
			if(code == 200){
//				String str = EntityUtils.toString(response.getEntity(), "utf-8");  //获取服务器响应内容
//			    JsonNode root = objMapper.readTree(str);  
//			    JsonNode obj = root.path("obj"); 
//			    map.put("obj", obj);
				map.put("msg", "删除成功！");
				map.put("success", true);
			}
		} catch (Exception e) {  
		    e.printStackTrace();
		    map.put("msg", "删除失败！");
			map.put("success", false);  
		}
		return map;
	}
	
	/**
	 * 生成树的方法
	 */
	@RequestMapping(value = "/getOrgTree")
	@ResponseBody
	@SystemControllerLog(description = "生成树的方法")
	public JSONArray getOrgTree(HttpSession session){
		logger.info("YBasicOrganization getOrgTree");
		JSONArray arrayJson = new JSONArray();
		try {
			List<Tree1> treelist = new ArrayList<Tree1>();
			createOrgTree(treelist);
			arrayJson = treeToJson(treelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayJson;
	}
	/**
	 * 创建树干
	 * @Title: createTree 
	 * @Description: TODO 
	 * @param     
	 * @return void   
	 * @throws
	 */
	public void createOrgTree(List<Tree1> treelist){
		List<YBasicOrganization> orgList = orgService.getByHql("from YBasicOrganization y where y.fbillState = 5");
		if(orgList != null&&orgList.size() > 0){
			for(YBasicOrganization o:orgList){
				Tree1 tree = new Tree1();
				tree.setId(o.getFid());
				tree.setText(o.getFname());
				tree.setLevel(1);
				treelist.add(tree);
			}
		}
	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	@RequestMapping(value = "/createTreechild")
	@ResponseBody
	@SystemControllerLog(description = "创建子树-团体")
	public JSONArray createTreechild(String id){
		List<Tree1> treelist = new ArrayList<Tree1>();
		JSONArray arrayJson = new JSONArray();
		List<YBasicSocialgroups> grolist = groupsService.getByHql("from YBasicSocialgroups as y where y.YBasicOrganization.fid='"+id+"' and y.fbillState = 5");
		if(grolist!=null&&grolist.size()>0){
			for(YBasicSocialgroups gro : grolist){
				Tree1 child = new Tree1();
				child.setId(gro.getFid());
				child.setText(gro.getFname());
				child.setLevel(2);
				treelist.add(child);
			}
		}
		arrayJson = treeToJson(treelist);
		return arrayJson;
	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	@RequestMapping(value = "/createTrees")
	@ResponseBody
	@SystemControllerLog(description = "创建子树-会员")
	public JSONArray createTrees(String id){
		JSONArray arrayJson = new JSONArray();
		List<YBasicMember> memlist = memberService.findByHql("from YBasicMember as y where y.YBasicSocialgroups.fid='"+id+"' and y.fbillState = 5");
		if(memlist!=null&&memlist.size()>0){
			for(YBasicMember mem : memlist){
				JSONObject object = new JSONObject();
				object.put("cls","");
				object.put("id",mem.getFid());
				object.put("leaf", true);
				object.put("text",mem.getFname());
				object.put("level",3);
				object.put("checked",false);
				arrayJson.add(object);
			}
		}
		return arrayJson;
	}
	/**
	 * 
	 * @Title: treeToJson 
	 * @Description: TODO 迭代生成 Tree Json
	 * @param @param tree
	 * @param @return    
	 * @return JSONArray   
	 * @throws
	 */
	public JSONArray treeToJson(List<Tree1> tree){
		JSONArray array = new JSONArray();
		for(Tree1 childrenTree : tree){
			JSONObject object = new JSONObject();
			//JSONObject o = new JSONObject();
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("leaf", childrenTree.getLeaf());
			object.put("text", childrenTree.getText());
			object.put("level",childrenTree.getLevel());
			//o.put("children", object);
			array.add(object);
		}
		return array;
	}
}
