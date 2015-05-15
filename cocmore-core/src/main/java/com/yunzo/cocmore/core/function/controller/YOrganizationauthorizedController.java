package com.yunzo.cocmore.core.function.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
//import com.yunzo.cocmore.core.function.controller.YRoleauthorizationController.Tree;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;
import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers.Status;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.service.YFunctionentriesFunctionjournalentryService;
import com.yunzo.cocmore.core.function.service.YOrganizationauthorizedService;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationFunctionService;
import com.yunzo.cocmore.core.function.util.Tree;

/**
 * @ClassName: YOrganizationauthorizedController 
 * @Description: TODO 用户关系Controller 
 * @date 2014年11月25日 下午4:47:48 
 * @author Ian
 *
 */
@Controller
@RequestMapping("/organizationauthorized")
public class YOrganizationauthorizedController {
	private static final Logger logger = Logger.getLogger(YOrganizationauthorizedController.class);
	
	@Resource(name = "organizationauthorizedService")
	private YOrganizationauthorizedService organizationauthorizedService;
	
	@Resource(name = "systemconfigurationFunctionService")
	private YSystemconfigurationFunctionService systemconfigurationFunctionService;
	
	@Resource(name = "functionentriesFunctionjournalentryService")
	private YFunctionentriesFunctionjournalentryService functionentriesFunctionjournalentryService;
	
	@Resource(name = "userInfo")
	private UserService userService;
	
	/**
	 * 给用户授权
	 * @param ids
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	@SystemControllerLog(description="给用户授权")
	public Map<String, Object> save(String[] ids,String userId){
		logger.info("organizationauthorizedService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YSystemUsers user=new YSystemUsers();
			user.setFid(userId);
			List<YFunctionentriesFunctionjournalentry> functionlentryList=null;
			functionlentryList=functionentriesFunctionjournalentryService.findAll();
			List<YOrganizationauthorized> orglist=new ArrayList<YOrganizationauthorized>();
			for(String id:ids){
				YFunctionentriesFunctionjournalentry ff=null;
				YOrganizationauthorized o=new YOrganizationauthorized();
				boolean s=true;
				for(YFunctionentriesFunctionjournalentry f:functionlentryList){
					if(f.getFid().equals(id)){
						ff=f;
						s=false;
						break;
					}
				}
				YSystemconfigurationFunction sf=new YSystemconfigurationFunction();
				if(s){
					sf.setFid(id);
					o.setYSystemUsers(user);
					o.setYSystemconfigurationFunction(sf);
					o.setYFunctionentriesFunctionjournalentry(null);
				}else{
					//sf.setFid(ff.getYSystemconfigurationFunction().getFid());
					o.setYSystemUsers(user);
					o.setYFunctionentriesFunctionjournalentry(ff);
					o.setYSystemconfigurationFunction(ff.getYSystemconfigurationFunction());
				}
				orglist.add(o);
				//organizationauthorizedService.save(o);
			}
			List<YOrganizationauthorized> organlist = organizationauthorizedService
					.findByHql("from YOrganizationauthorized as y where y.YSystemUsers.fid='"
							+ userId + "'");
			//获取没有的权限  添加
			for(YOrganizationauthorized org:orglist){
				boolean t=true;
				for(YOrganizationauthorized or:organlist){
					if(org.getYFunctionentriesFunctionjournalentry()==null){
						if(or.getYFunctionentriesFunctionjournalentry()==null){
							if(org.getYSystemconfigurationFunction().getFid().equals(or.getYSystemconfigurationFunction().getFid())){
								t=false;
								break;
							}
						}
					}else{
						if(or.getYFunctionentriesFunctionjournalentry()!=null){
							if(or.getYSystemconfigurationFunction().getFid().equals(org.getYSystemconfigurationFunction().getFid())&&or.getYFunctionentriesFunctionjournalentry().getFid().equals(org.getYFunctionentriesFunctionjournalentry().getFid())){
								t=false;
								break;
							}
						}
					}
				}
				if(t){
					organizationauthorizedService.save(org);
				}
			}
			//获取取消了的权限 删除
			for(YOrganizationauthorized o:organlist){
				boolean x =true;
				for(YOrganizationauthorized or:orglist){
					if(o.getYFunctionentriesFunctionjournalentry()==null){
						if(or.getYFunctionentriesFunctionjournalentry()==null){
							if(o.getYSystemconfigurationFunction().getFid().equals(or.getYSystemconfigurationFunction().getFid())){
								x=false;
								break;
							}
						}
					}else{
						if(or.getYFunctionentriesFunctionjournalentry()!=null){
							if(or.getYSystemconfigurationFunction().getFid().equals(o.getYSystemconfigurationFunction().getFid())&&or.getYFunctionentriesFunctionjournalentry().getFid().equals(o.getYFunctionentriesFunctionjournalentry().getFid())){
								x=false;
								break;
							}
						}
					}
				}
				if(x){
					organizationauthorizedService.delete(o);
				}
			}
			YSystemUsers u=new YSystemUsers();
			u=userService.getById(userId);
			//y_system_user和y_charater 中FPrivilege 0是未赋权1是赋权 2赋予角色权限 3是赋予用户权限  4是角色和功能都赋予 5待定
			//ADDNEW(0),SAVE(1),SUBMIT(2),AUDIT(3),UNAUDIT(4),EFFECT(5);
			if(ids!=null&&ids.length>0){
				if(u.getFprivileges()==Status.UNAUDIT.value()){//角色和功能都赋予
					u.setFprivileges(Status.UNAUDIT.value());
				}else if(u.getFprivileges()==Status.SUBMIT.value()){//赋予角色权限
					u.setFprivileges(Status.UNAUDIT.value());
				}else{
					u.setFprivileges(Status.AUDIT.value());
				}
			}else{
				if(u.getFprivileges()==Status.UNAUDIT.value()){
					u.setFprivileges(Status.SUBMIT.value());
				}else if(u.getFprivileges()==Status.SUBMIT.value()){
					u.setFprivileges(Status.SUBMIT.value());
				}else{
					u.setFprivileges(Status.ADDNEW.value());
				}
			}
			userService.update(u);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 批量给用户授权
	 * @param ids
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/saves")
	@ResponseBody
	@SystemControllerLog(description="organizationauthorizedService save")
	public Map<String, Object> saves(String[] ids,String[] userIds){
		logger.info("organizationauthorizedService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			if(userIds!=null&&userIds.length>0){
				for(String userId:userIds){
					YSystemUsers user=new YSystemUsers();
					user.setFid(userId);
					List<YFunctionentriesFunctionjournalentry> functionlentryList=null;
					functionlentryList=functionentriesFunctionjournalentryService.findAll();
					List<YOrganizationauthorized> orglist=new ArrayList<YOrganizationauthorized>();
					for(String id:ids){
						YFunctionentriesFunctionjournalentry ff=null;
						YOrganizationauthorized o=new YOrganizationauthorized();
						boolean s=true;
						for(YFunctionentriesFunctionjournalentry f:functionlentryList){
							if(f.getFid().equals(id)){
								ff=f;
								s=false;
								break;
							}
						}
						YSystemconfigurationFunction sf=new YSystemconfigurationFunction();
						if(s){
							sf.setFid(id);
							o.setYSystemUsers(user);
							o.setYSystemconfigurationFunction(sf);
							o.setYFunctionentriesFunctionjournalentry(null);
						}else{
							//sf.setFid(ff.getYSystemconfigurationFunction().getFid());
							o.setYSystemUsers(user);
							o.setYFunctionentriesFunctionjournalentry(ff);
							o.setYSystemconfigurationFunction(ff.getYSystemconfigurationFunction());
						}
						orglist.add(o);
						//organizationauthorizedService.save(o);
					}
					List<YOrganizationauthorized> organlist = organizationauthorizedService
							.findByHql("from YOrganizationauthorized as y where y.YSystemUsers.fid='"
									+ userId + "'");
					//获取没有的权限  添加
					for(YOrganizationauthorized org:orglist){
						boolean t=true;
						for(YOrganizationauthorized or:organlist){
							if(org.getYFunctionentriesFunctionjournalentry()==null){
								if(or.getYFunctionentriesFunctionjournalentry()==null){
									if(org.getYSystemconfigurationFunction().getFid().equals(or.getYSystemconfigurationFunction().getFid())){
										t=false;
										break;
									}
								}
							}else{
								if(or.getYFunctionentriesFunctionjournalentry()!=null){
									if(or.getYSystemconfigurationFunction().getFid().equals(org.getYSystemconfigurationFunction().getFid())&&or.getYFunctionentriesFunctionjournalentry().getFid().equals(org.getYFunctionentriesFunctionjournalentry().getFid())){
										t=false;
										break;
									}
								}
							}
						}
						if(t){
							organizationauthorizedService.save(org);
						}
					}
					//获取取消了的权限 删除
					for(YOrganizationauthorized o:organlist){
						boolean x =true;
						for(YOrganizationauthorized or:orglist){
							if(o.getYFunctionentriesFunctionjournalentry()==null){
								if(or.getYFunctionentriesFunctionjournalentry()==null){
									if(o.getYSystemconfigurationFunction().getFid().equals(or.getYSystemconfigurationFunction().getFid())){
										x=false;
										break;
									}
								}
							}else{
								if(or.getYFunctionentriesFunctionjournalentry()!=null){
									if(or.getYSystemconfigurationFunction().getFid().equals(o.getYSystemconfigurationFunction().getFid())&&or.getYFunctionentriesFunctionjournalentry().getFid().equals(o.getYFunctionentriesFunctionjournalentry().getFid())){
										x=false;
										break;
									}
								}
							}
						}
						if(x){
							organizationauthorizedService.delete(o);
						}
					}
					YSystemUsers u=new YSystemUsers();
					u=userService.getById(userId);
					//y_system_user和y_charater 中FPrivilege 0是未赋权1是赋权 2赋予角色权限 3是赋予用户权限  4是角色和功能都赋予 5待定
					//ADDNEW(0),SAVE(1),SUBMIT(2),AUDIT(3),UNAUDIT(4),EFFECT(5);
					if(ids!=null&&ids.length>0){
						if(u.getFprivileges()==Status.UNAUDIT.value()){//角色和功能都赋予
							u.setFprivileges(Status.UNAUDIT.value());
						}else if(u.getFprivileges()==Status.SUBMIT.value()){//赋予角色权限
							u.setFprivileges(Status.UNAUDIT.value());
						}else{
							u.setFprivileges(Status.AUDIT.value());
						}
					}else{
						if(u.getFprivileges()==Status.UNAUDIT.value()){
							u.setFprivileges(Status.SUBMIT.value());
						}else if(u.getFprivileges()==Status.SUBMIT.value()){
							u.setFprivileges(Status.SUBMIT.value());
						}else{
							u.setFprivileges(Status.ADDNEW.value());
						}
					}
					userService.update(u);
				}
				map.put("msg", "成功");
				map.put("success", true);
			}else{
				map.put("msg", "失败");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping(value="/update")
	@ResponseBody
	@SystemControllerLog(description="organizationauthorizedService update")
	public Map<String, Object> update(@ModelAttribute("form")YOrganizationauthorized organizationauthorized){
		logger.info("organizationauthorizedService update");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			organizationauthorizedService.update(organizationauthorized);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping(value="/delete")
	@ResponseBody
	@SystemControllerLog(description="organizationauthorizedService delete")
	public Map<String, Object> delete(@ModelAttribute("form")YOrganizationauthorized organizationauthorized){
		logger.info("organizationauthorizedService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			organizationauthorizedService.delete(organizationauthorized);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping(value="/findByUserID")
	@ResponseBody
	@SystemControllerLog(description="organizationauthorizedService findByUserID")
	public Map<String, Object> findByUserID(@RequestParam("userid")String userid){
		List<YOrganizationauthorized> list=null;
		logger.info("organizationauthorizedService findByUserID");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			list=organizationauthorizedService.findByUserID(userid);
			map.put("msg", "成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 用户授权加载功能树
	 * @param yhid
	 * @return
	 */
	@RequestMapping(value="/getByBasicFunctionTree")
	@ResponseBody
	@SystemControllerLog(description="用户授权加载功能树")
	public JSONArray getByBasicFunctionTree(String yhid){
		logger.info("organizationauthorizedService getByBasicFunctionTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			Map<String,Object> orgmap=new HashMap<String, Object>();
			HashSet<String> ffset=new HashSet<String>();
			HashSet<String> sfset=new HashSet<String>();
			if(null!=yhid&&!"".equals(yhid)){
				orgmap=getorgByuserId(yhid);
				ffset=(HashSet<String>)orgmap.get("ffset");
				sfset=(HashSet<String>)orgmap.get("sfset");
			}
			if(null!=orgmap&&!"".equals(orgmap)){
				createTree(treelist,ffset,sfset);
			}else{
				createTree(treelist,null,null);
			}
			
			
			arrayJson = treeToJson(treelist);
			//writeJson(arrayJson);
			
			map.put("msg", "成功");
			map.put("obj", arrayJson);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
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
	private void createTree(List<Tree> treelist,HashSet<String> ffset,HashSet<String> sfset){
		List<YSystemconfigurationFunction> list = systemconfigurationFunctionService.findByHql("from YSystemconfigurationFunction");
		for(YSystemconfigurationFunction l:list){
			Tree tree = new Tree();
			tree.setId(l.getFid());
			tree.setText(l.getFaccount());
			if(null!=sfset&&!"".equals(sfset)&&sfset.size()>0){
				for(String id:sfset){
					if(l.getFid().equals(id)){
						tree.setChecked(true);
						break;
					}
				}
			}
			createTreechild(l,tree,ffset);
			treelist.add(tree);
			
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
	private void createTreechild(YSystemconfigurationFunction YSystemconfigurationFunction,Tree tree,HashSet<String> ffset){
		List<YFunctionentriesFunctionjournalentry> list=functionentriesFunctionjournalentryService.findByHql("from YFunctionentriesFunctionjournalentry as y where y.YSystemconfigurationFunction.fid='"+YSystemconfigurationFunction.getFid()+"' and y.fIsBasic='0'");
		if(list.size()>0){
			for(YFunctionentriesFunctionjournalentry y:list){
				Tree child = new Tree();
				child.setId(y.getFid());
				child.setText(y.getFfunctionName());
				child.setLeaf(true);
				if(null!=ffset&&!"".equals(ffset)&&ffset.size()>0){
					for(String id:ffset){
						if(id.equals(y.getFid())){
							child.setChecked(true);
							break;
						}
					}
				}
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
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
	private JSONArray treeToJson(List<Tree> tree){
		JSONArray array = new JSONArray();
		for(Tree childrenTree : tree){
			JSONObject object = new JSONObject();
			//JSONObject o = new JSONObject();
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("leaf", childrenTree.getLeaf());
			object.put("text", childrenTree.getText());
			object.put("checked", childrenTree.getChecked());
			
			if(childrenTree.getChildren().size() > 0){
				object.put("children", treeToJson(childrenTree.getChildren()));
			}
			//o.put("children", object);
			array.add(object);
		}
		return array;
	}
	/**
	 * 获取用户的权限
	 * @param adminId
	 * @return
	 */
	private Map<String, Object> getorgByuserId(String adminId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<YOrganizationauthorized> organlist = organizationauthorizedService
				.findByHql("from YOrganizationauthorized as y where y.YSystemUsers.fid='"
						+ adminId + "'");
		HashSet<String> ffset=new HashSet<String>();
		HashSet<String> sfset=new HashSet<String>();
		///获取用户权限表中的数据
		for (YOrganizationauthorized org : organlist) {
			if (null == org.getYFunctionentriesFunctionjournalentry()
					|| "".equals(org.getYFunctionentriesFunctionjournalentry())) {
				sfset.add(org.getYSystemconfigurationFunction().getFid());
			} else {
				sfset.add(org.getYSystemconfigurationFunction().getFid());
				ffset.add(org.getYFunctionentriesFunctionjournalentry().getFid());
			}
			
		}
		map.put("sfset", sfset);
		map.put("ffset", ffset);
		return map;
	}
	
}
