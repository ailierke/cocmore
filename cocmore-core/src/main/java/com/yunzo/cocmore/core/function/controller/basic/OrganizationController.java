package com.yunzo.cocmore.core.function.controller.basic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMenu;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.OrganizationService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.util.Tree1;

/** 
 *Description: <组织服务控制层>. <br>
 * @date:2014年11月24日 下午4:14:17
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/org")
public class OrganizationController {
	private static final Logger logger = Logger.getLogger(OrganizationController.class);
	
	@Resource(name = "orgService")
	private OrganizationService orgService;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	//此处的参数也可以是ServletRequestDataBinder类型 
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	@RequestMapping("/findOrgById")
	@ResponseBody
	@SystemControllerLog(description="查找单个组织")
	public Map<String, Object> doNotNeedSessionAndSecurity_findOrgById(@RequestParam("fid")String fid){
		logger.info("YBasicOrganization  findOrgById");
		Map<String, Object> map = new HashMap<String,Object>();
		YBasicOrganization obj = null;
		try {
			obj = orgService.getById(fid);
			map.put("msg", "查询成功");
			map.put("obj", obj);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据参数ID查询
	 * 查询出参数ID除外的所有数据
	 * 为空的话查询出所有
	 * @param superId
	 * @return
	 */
	@RequestMapping("/findAllOrgBySuperId")
	@ResponseBody
	@SystemControllerLog(description="根据参数ID查询")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllOrgBySuperId(@RequestParam(value="superId", required=false)String superId){
		logger.info("YBasicOrganization  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YBasicOrganization> list = null;
		try {
			list = orgService.findAllBySuperId(superId);
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
	 * 分页查询（默认查询全部）
	 * 在参数 searchName不为空 的情况下，根据搜索名称模糊查询
	 * @param searchName
	 * @return
	 */
	@RequestMapping("/findAllOrg")
	@ResponseBody
	@SystemControllerLog(description="分页查询（默认查询全部）")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllOrg(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit){
		logger.info("YBasicOrganization  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YBasicOrganization> list = null;
		try {
			list = orgService.findAll(searchName,start,limit);
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
	
	@RequestMapping("/saveOrg")
	@ResponseBody
	@SystemControllerLog(description="save YBasicOrganization")
	public Map<String, Object> doNotNeedSessionAndSecurity_saveOrg(@ModelAttribute("form")YBasicOrganization form){
		logger.info("save YBasicOrganization");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			orgService.save(form);
			map.put("msg", "保存成功");
			map.put("success", true);
			getNumberService.addSerialNumber("JC-ZZGL");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "保存失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/updateOrg")
	@ResponseBody
	@SystemControllerLog(description="update YBasicOrganization")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateOrg(@ModelAttribute("form")YBasicOrganization form){
		logger.info("update YBasicOrganization");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			orgService.update(form);
			map.put("msg", "修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 审核、反审核、生效和失效操作
	 * @param fid
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateOrgStatus")
	@ResponseBody
	@SystemControllerLog(description="审核、反审核、生效和失效操作组织")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateOrgStatus(@RequestParam("fids")String fids,@RequestParam("status")int status){
		logger.info("updateOrgStatus YBasicOrganization");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for(String id : fids.split(",")){
				YBasicOrganization obj = orgService.getById(id);
				obj.setFbillState(status);
				orgService.update(obj);
			}
			map.put("msg", "状态修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "状态修改失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 生成树的方法
	 */
	@RequestMapping(value = "/getOrgTree")
	@ResponseBody
	@SystemControllerLog(description="YBasicOrganization getOrgTree")
	public JSONArray getOrgTree(HttpSession session){
		logger.info("YBasicOrganization getOrgTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		String userId=(String)session.getAttribute("userId");
		try {
			if(userId.equals("1")){
				List<Tree1> treelist = new ArrayList<Tree1>();
				createOrgTree(treelist);
				arrayJson = treeToJson(treelist);
			}else{
				Map<String, Object> sessionMap = new HashMap<String, Object>();
				sessionMap = (Map<String, Object>)session.getAttribute("map");
				HashSet<String> orgSet=(HashSet<String>)sessionMap.get("orgSet");
				List<YBasicOrganization> orgList=new ArrayList<YBasicOrganization>();
				for(String fid:orgSet){
					orgList.add(orgService.getById(fid));
				}
				List<Tree1> treelist=new ArrayList<Tree1>();
				createOrgTreeByList(orgList,treelist);
				arrayJson = treeToJson(treelist);
			}
//			map.put("msg", "成功");
//			map.put("obj", arrayJson);
//			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			//map.put("msg", "失败");
			//map.put("success", false);
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
	private void createOrgTree(List<Tree1> treelist){
		List<YBasicOrganization> orglist = orgService.findAll();
		for(YBasicOrganization o:orglist){
			if(o.getFsuperiorOrganizationId()==null||o.getFsuperiorOrganizationId().equals("")){
				Tree1 tree = new Tree1();
				tree.setId(o.getFid());
				String statu = o.getFbillState()==null?"":o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":o.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
				tree.setText(o.getFname()+statu);
				createOrgTreechild(o.getFid(),tree);
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
	private void createOrgTreechild(String id,Tree1 tree){
		List<YBasicOrganization> list = orgService.getByHql("select new YBasicOrganization(y.fid,y.fname,y.fbillState,y.fsuperiorOrganizationId) from YBasicOrganization y where y.fsuperiorOrganizationId='"+id+"'");
		if(list!=null&&list.size()>0){
			for(YBasicOrganization me : list){
				Tree1 child = new Tree1();
				child.setId(me.getFid());
				String statu = me.getFbillState()==null?"":me.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":me.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
				child.setText(me.getFname()+statu);
				createOrgTreechild(me.getFid(),child);
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}
	
	/**
	 * 创建树
	 */
	public void createOrgTreeByList(List<YBasicOrganization> orgList,List<Tree1> treelist){
		for(YBasicOrganization o:orgList){
			boolean t=true;
			for(YBasicOrganization org:orgList){
				if("".equals(o.getFsuperiorOrganizationId())||null==o.getFsuperiorOrganizationId()){
					t=true;
					break;
				}else{
					if(o.getFsuperiorOrganizationId().equals(org.getFid())){
						t=false;
						break;
					}
				}
				
			}
			if(t){
				Tree1 tree = new Tree1();
				tree.setId(o.getFid());
				String statu = o.getFbillState()==null?"":o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":o.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
				tree.setText(o.getFname()+statu);
				createOrgTreelist(o.getFid(),tree,orgList);
				treelist.add(tree);
			}
		}
	
	}
	/**
	 * 创建子树
	 * @param id
	 * @param tree
	 */
	private void createOrgTreelist(String id,Tree1 tree,List<YBasicOrganization> orgList){
		List<YBasicOrganization> list = new ArrayList<YBasicOrganization>();
		for(YBasicOrganization o:orgList){
			if(!"".equals(o.getFsuperiorOrganizationId())&&null!=o.getFsuperiorOrganizationId()){
				if(o.getFsuperiorOrganizationId().equals(id)){
					list.add(o);
				}
			}
		}
		if(list!=null&&list.size()>0){
			for(YBasicOrganization me : list){
				Tree1 child = new Tree1();
				child.setId(me.getFid());
				String statu = me.getFbillState()==null?"":me.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":me.getFbillState()==Status.RELATION.value()?"(<span style='color:blue'>待审核</span>)":"";
				child.setText(me.getFname()+statu);
				createOrgTreechild(me.getFid(),child);
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
	private JSONArray treeToJson(List<Tree1> tree){
		JSONArray array = new JSONArray();
		for(Tree1 childrenTree : tree){
			JSONObject object = new JSONObject();
			//JSONObject o = new JSONObject();
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("leaf", childrenTree.getLeaf());
			object.put("text", childrenTree.getText());
			
			if(childrenTree.getChildren().size() > 0){
				object.put("children", treeToJson(childrenTree.getChildren()));
			}
			//o.put("children", object);
			array.add(object);
		}
		return array;
	}
//	/**
//	 * 生成树的方法
//	 */
//	@RequestMapping(value = "/getOrgTree")
//	@ResponseBody
//	public JSONArray getOrgTree(){
//		logger.info("YBasicOrganization getOrgTree");
//		Map<String, Object> map = new HashMap<String,Object>();
//		JSONArray arrayJson = new JSONArray();
//		try {
//			List<Tree> treelist = new ArrayList<Tree>();
//			createTree(treelist);
//			arrayJson = treeToJson(treelist);
////			map.put("msg", "成功");
////			map.put("obj", arrayJson);
////			map.put("success", true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			//map.put("msg", "失败");
//			//map.put("success", false);
//		}
//		return arrayJson;
//	}
//	/**
//	 * 创建树干
//	 * @Title: createTree 
//	 * @Description: TODO 
//	 * @param     
//	 * @return void   
//	 * @throws
//	 */
//	private void createTree(List<Tree> treelist){
//		List<YBasicOrganization> orglist = orgService.findAll();
//		
//		for(YBasicOrganization o:orglist){
//			if(o.getFsuperiorOrganizationId()==null||o.getFsuperiorOrganizationId().equals("")){
//				Tree tree = new Tree();
//				tree.setId(o.getFid());
//				tree.setText(o.getFname());
//				createTreechild(o.getFid(),tree);
//				treelist.add(tree);
//			}
//		}
//	}
//	/**
//	 * 
//	 * @Title: createTreechild 
//	 * @Description: TODO 创建子树
//	 * @param @param id
//	 * @param @param tree    
//	 * @return void   
//	 * @throws
//	 */
//	private void createTreechild(String id,Tree tree){
//		List<YBasicOrganization> list = orgService.getByHql("from YBasicOrganization as y where y.fsuperiorOrganizationId='"+id+"'");
//		if(list!=null&&list.size()>0){
//			for(YBasicOrganization me : list){
//				Tree child = new Tree();
//				child.setId(me.getFid());
//				child.setText(me.getFname());
//				createTreechild(me.getFid(),child);
//				tree.getChildren().add(child);
//			}
//		}else{
//			tree.setLeaf(true);
//		}
//	}
//	/**
//	 * 
//	 * @Title: treeToJson 
//	 * @Description: TODO 迭代生成 Tree Json
//	 * @param @param tree
//	 * @param @return    
//	 * @return JSONArray   
//	 * @throws
//	 */
//	private JSONArray treeToJson(List<Tree> tree){
//		JSONArray array = new JSONArray();
//		for(Tree childrenTree : tree){
//			JSONObject object = new JSONObject();
//			object.put("cls", childrenTree.getCls());
//			object.put("id", childrenTree.getId());
//			object.put("leaf", childrenTree.getLeaf());
//			object.put("text", childrenTree.getText());
//			if(childrenTree.getChildren().size() > 0){
//				object.put("children", treeToJson(childrenTree.getChildren()));
//			}
//			array.add(object);
//		}
//		return array;
//	}
//	class Tree {
//		private String cls = "folder";
//		private String id;
//
//		private boolean leaf = false;
//		// private boolean checked=true;
//		private String text = "";
//		private List<Tree> children = new ArrayList<Tree>();
//		private int level;
//		private String pid;
//
//		public String getCls() {
//			return cls;
//		}
//
//		public void setCls(String cls) {
//			this.cls = cls;
//		}
//
//		public String getId() {
//			return id;
//		}
//
//		public void setId(String id) {
//			this.id = id;
//		}
//
//		public boolean getLeaf() {
//			return leaf;
//		}
//
//		public void setLeaf(boolean leaf) {
//			this.leaf = leaf;
//		}
//
//		// public boolean getChecked() {
//		// return checked;
//		// }
//		// public void setChecked(boolean checked) {
//		// this.checked = checked;
//		// }
//		public String getText() {
//			return text;
//		}
//
//		public void setText(String text) {
//			this.text = text;
//		}
//
//		public List<Tree> getChildren() {
//			return children;
//		}
//
//		public void setChildren(List<Tree> children) {
//			this.children = children;
//		}
//
//		public int getLevel() {
//			return level;
//		}
//
//		public void setLevel(int level) {
//			this.level = level;
//		}
//
//		public String getPid() {
//			return pid;
//		}
//
//		public void setPid(String pid) {
//			this.pid = pid;
//		}
//	}
	
}


