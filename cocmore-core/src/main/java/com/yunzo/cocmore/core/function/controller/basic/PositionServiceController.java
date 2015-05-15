package com.yunzo.cocmore.core.function.controller.basic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesEmployeedistribution;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.service.EmployeeServiceI;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.MemberdistributionServiceI;
import com.yunzo.cocmore.core.function.service.PositionServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <职位控制层>. <br>
 * @date:2014年11月25日 下午4:39:01
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/pos")
public class PositionServiceController {
	private static final Logger logger = Logger.getLogger(PositionServiceController.class);
	
	@Resource(name = "posService")
	private PositionServiceI posService;
	
	@Resource(name = "empService")
	private EmployeeServiceI empService;
	
	@Resource(name = "memdisService")
	MemberdistributionServiceI memdisService;
	
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
	
	@RequestMapping("/findPosById")
	@ResponseBody
	@SystemControllerLog(description="YBasicPosition  findOrgById")
	public Map<String, Object> doNotNeedSessionAndSecurity_findPosById(@RequestParam("fid")String fid){
		logger.info("YBasicPosition  findOrgById");
		Map<String, Object> map = new HashMap<String,Object>();
		YBasicPosition obj = null;
		try {
			obj = posService.getById(fid);
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
	 * 查询上级职位，排除本身职位
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findSuper")
	@ResponseBody
	@SystemControllerLog(description="查询上级职位，排除本身职位")
	public Map<String, Object> doNotNeedSessionAndSecurity_findSuper(@RequestParam(value="selfFid", required=false)String selfFid,
			@RequestParam(value="groupId", required=false)String groupId,String orgId){
		logger.info("YBasicPosition  findSuper");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YBasicPosition> list = null;
		try {
			list = posService.findSuper(selfFid,groupId,orgId);
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
	
	
	
	@RequestMapping("/findAllPos")
	@ResponseBody
	@SystemControllerLog(description="YBasicPosition  handleList")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllPos(@RequestParam(value="searchName", required=false)String searchName,
			@RequestParam(value="start", required=false)Integer start,@RequestParam(value="limit", required=false)Integer limit,String groupId,String orgId){
		logger.info("YBasicPosition  handleList");
		Map<String, Object> map = new HashMap<String,Object>();
		PagingList<YBasicPosition> list = null;
		try {
			list = posService.findAll(searchName, start, limit, groupId, orgId);
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
	
	@RequestMapping("/savePos")
	@ResponseBody
	@SystemControllerLog(description="save YBasicPosition")
	public Map<String, Object> doNotNeedSessionAndSecurity_savePos(@ModelAttribute("form")YBasicPosition form){
		logger.info("save YBasicPosition");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			posService.save(form);
			map.put("msg", "保存成功");
			map.put("success", true);
			getNumberService.addSerialNumber("JC-ZWGL");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "保存失败");
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/updatePos")
	@ResponseBody
	@SystemControllerLog(description="update YBasicPosition")
	public Map<String, Object> doNotNeedSessionAndSecurity_updatePos(@ModelAttribute("form")YBasicPosition form){
		logger.info("update YBasicPosition");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			posService.update(form);
			map.put("msg", "修改成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	
	
	@RequestMapping("/deletePos")
	@ResponseBody
	@SystemControllerLog(description="deletePos YBasicPosition")
	public Map<String, Object> doNotNeedSessionAndSecurity_deletePos(String fid){
		logger.info("deletePos YBasicPosition");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			posService.delete(posService.getById(fid));
			map.put("msg", "删除成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "删除失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 作废
	 * @param fid
	 * @param fbillState
	 * @param comment
	 * @return
	 */
	@RequestMapping("/Invalid")
	@ResponseBody
	@SystemControllerLog(description="Invalid YBasicPosition")
	public Map<String, Object> doNotNeedSessionAndSecurity_Invalid(String fid,String fbillState,String comment){
		logger.info("Invalid YBasicPosition");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicPosition pos=new YBasicPosition();
			pos=posService.getById(fid);
			pos.setFbillState(Integer.parseInt(fbillState));
			pos.setFcomment(comment);
			posService.update(pos);
			map.put("msg", "作废成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "作废失败");
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 生成树的方法
	 */
	@RequestMapping(value = "/getPositionTree")
	@ResponseBody
	@SystemControllerLog(description="posService getPositionTree")
	public JSONArray getPositionTree(String groupId){
		logger.info("posService getPositionTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		logger.info("************#########################"+groupId);
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			createTree(treelist,groupId);
			arrayJson = treeToJson(treelist);
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
	private void createTree(List<Tree> treelist,String groupId){
		List<YBasicPosition> poslist = posService.findByHql("from YBasicPosition as y where y.fsocialGroupsId='"+groupId+"' and y.fbillState=5 order by  y.fseq");
		for(YBasicPosition pos:poslist){
			if(null==pos.getFsuperPositonId()||"".equals(pos.getFsuperPositonId())){
				Tree tree = new Tree();
				tree.setId(pos.getFid());
				tree.setText(pos.getFname());
				createTreechild(pos.getFid(),tree,groupId);
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
	private void createTreechild(String id,Tree tree,String groupId){
		List<YBasicPosition> list = posService.findByHql("from YBasicPosition as y where y.fsuperPositonId='"+id+"' and y.fsocialGroupsId='"+groupId+"'");
		Treechild(id,tree);
		if(list!=null&&list.size()>0){
			tree.setLeaf(false);
			for(YBasicPosition me : list){
				Tree child = new Tree();
				child.setId(me.getFid());
				child.setText(me.getFname());
				
				createTreechild(me.getFid(),child,groupId);
				
				tree.getChildren().add(child);
				
			}
		}
	}
	private void Treechild(String id,Tree tree){
		List<YBasicentriesMemberdistribution> poslist=memdisService.findPosByEdID(id);
		if(poslist !=null&& poslist.size()>0){
			tree.setLeaf(false);
			for(YBasicentriesMemberdistribution ye:poslist){
				if(ye.getYBasicMember().getFbillState()==5)
				{
					Tree child = new Tree();
					child.setId(ye.getYBasicMember().getFid());
					child.setText(ye.getYBasicMember().getFname());
					child.setLeaf(true);
					
					tree.getChildren().add(child);
				}
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
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("leaf", childrenTree.getLeaf());
			object.put("text", childrenTree.getText());
			object.put("checked", childrenTree.getChecked());
			if(childrenTree.getChildren().size() > 0){
				object.put("children", treeToJson(childrenTree.getChildren()));
			}
			array.add(object);
		}
		return array;
	}
	class Tree {
		private String cls = "folder";
		private String id;

		private boolean leaf = false;
		private boolean checked=false;
		private String text = "";
		private List<Tree> children = new ArrayList<Tree>();
		private int level;
		private String pid;

		public String getCls() {
			return cls;
		}

		public void setCls(String cls) {
			this.cls = cls;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean getLeaf() {
			return leaf;
		}

		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}

		 public boolean getChecked() {
		 return checked;
		 }
		 public void setChecked(boolean checked) {
		 this.checked = checked;
		 }
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public List<Tree> getChildren() {
			return children;
		}

		public void setChildren(List<Tree> children) {
			this.children = children;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}
	}
	
	
}
