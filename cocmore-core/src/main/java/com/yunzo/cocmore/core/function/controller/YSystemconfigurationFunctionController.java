package com.yunzo.cocmore.core.function.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMenu;
import com.yunzo.cocmore.core.function.service.YFunctionentriesFunctionjournalentryService;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationFunctionService;


/**
 * @ClassName: YSystemconfigurationFunctionController 
 * @Description: TODO 模块表Controller 
 * @date 2014年11月24日 下午5:55:00 
 * @author Ian
 *
 */
@Controller
@RequestMapping("/systemconfigurationFunction")
public class YSystemconfigurationFunctionController {
	private static final Logger logger = Logger.getLogger(YSystemconfigurationFunctionController.class);
	
	@Resource(name = "systemconfigurationFunctionService")
	private YSystemconfigurationFunctionService systemconfigurationFunctionService;
	
	@Resource(name = "functionentriesFunctionjournalentryService")
	private YFunctionentriesFunctionjournalentryService functionentriesFunctionjournalentryService;
	
	@RequestMapping(value="/save")
	@ResponseBody
	@SystemControllerLog(description="systemconfigurationFunctionService save")
	public Map<String, Object> save(@ModelAttribute("form")YSystemconfigurationFunction function){
		logger.info("systemconfigurationFunctionService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			systemconfigurationFunctionService.save(function);
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
	@SystemControllerLog(description="systemconfigurationFunctionService delete")
	public Map<String, Object> delete(@ModelAttribute("form")YSystemconfigurationFunction function){
		logger.info("systemconfigurationFunctionService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			systemconfigurationFunctionService.delete(function);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping(value="/getById")
	@ResponseBody
	@SystemControllerLog(description="systemconfigurationFunctionService getById")
	public Map<String, Object> getById(@RequestParam("functionid")String functionid){
		logger.info("systemconfigurationFunctionService getById");
		Map<String, Object> map = new HashMap<String,Object>();
		YSystemconfigurationFunction function=null;
		try {
			function=systemconfigurationFunctionService.getById(functionid);
			map.put("msg", "成功");
			map.put("obj", function);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	} 
	@RequestMapping(value="/findAll")
	@ResponseBody
	@SystemControllerLog(description="systemconfigurationFunctionService findAll")
	public Map<String, Object> findAll(){
		logger.info("systemconfigurationFunctionService findAll");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YSystemconfigurationFunction> list=new ArrayList<YSystemconfigurationFunction>();
		try {
			list=systemconfigurationFunctionService.findAll();
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
	
	@RequestMapping(value="/functionTree")
	@ResponseBody
	public JSONArray functionTree(){
		logger.info("systemconfigurationFunction functionTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			List<YSystemconfigurationFunction> list = systemconfigurationFunctionService.findByHql("from YSystemconfigurationFunction");
			for(YSystemconfigurationFunction l:list){
				Tree tree = new Tree();
				tree.setId(l.getFid());
				tree.setText(l.getFaccount());
				tree.setLeaf(true);
				treelist.add(tree);
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
	
	@RequestMapping(value="/getFunctionTree")
	@ResponseBody
	@SystemControllerLog(description="systemconfigurationFunctionService getFunctionTree")
	public JSONArray getFunctionTree(){
		logger.info("systemconfigurationFunction getFunctionTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			createTree(treelist);
			
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
	private void createTree(List<Tree> treelist){
		List<YSystemconfigurationFunction> list = systemconfigurationFunctionService.findByHql("from YSystemconfigurationFunction");
		for(YSystemconfigurationFunction l:list){
			Tree tree = new Tree();
			tree.setId(l.getFid());
			tree.setText(l.getFaccount());
			createTreechild(l,tree);
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
	private void createTreechild(YSystemconfigurationFunction YSystemconfigurationFunction,Tree tree){
		List<YFunctionentriesFunctionjournalentry> list=functionentriesFunctionjournalentryService.findByFunctionID(YSystemconfigurationFunction.getFid());
		if(list.size()>0){
			for(YFunctionentriesFunctionjournalentry y:list){
				Tree child = new Tree();
				child.setId(y.getFid());
				child.setText(y.getFfunctionName());
				child.setLeaf(true);
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
