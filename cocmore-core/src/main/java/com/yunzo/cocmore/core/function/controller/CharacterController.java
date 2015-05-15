package com.yunzo.cocmore.core.function.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.store.disk.ods.AATreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;
import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;
import com.yunzo.cocmore.core.function.service.CharacterService;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.service.YRoleauthorizationService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Tree;

/**
 * @ClassName: CharacterController 
 * @Description: TODO 角色Controller 
 * @date 2014年11月24日 下午5:30:59 
 * @author Ian
 *
 */
@Controller
@RequestMapping("/character")
public class CharacterController {
	private static final Logger logger = Logger.getLogger(CharacterController.class);
	
	@Resource(name = "characterService")
	private CharacterService characterService;
	
	@Resource(name = "getNumberService")
	private GetNumberService getNumberService;
	
	@Resource(name = "roleauthorizationService")
	private YRoleauthorizationService roleauthorizationService;
	
	@RequestMapping("/save")
	@ResponseBody
	@SystemControllerLog(description = "新增角色")
	public Map<String, Object> save(@ModelAttribute("form")YCharacter character){
		
		logger.info("characterService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			characterService.save(character);
			map.put("msg", "成功");
			map.put("success", true);
			getNumberService.addSerialNumber("QX-JSGL");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}

		return map;
	}
	@RequestMapping("/delete")
	@ResponseBody
	@SystemControllerLog(description = "删除角色")
	public Map<String, Object> delete(@ModelAttribute("form")YCharacter character){
		
		logger.info("characterService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			characterService.delete(character);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/update")
	@ResponseBody
	@SystemControllerLog(description = "修改角色")
	public Map<String, Object> update(@ModelAttribute("form")YCharacter character){
		
		logger.info("characterService update");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			characterService.update(character);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "修改失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 获取所有的角色信息
	 * @return
	 */
	@RequestMapping("/findAll")
	@ResponseBody
	@SystemControllerLog(description = "获取所有的角色信息")
	public JSONArray findAll(HttpSession session,String id){
		logger.info("characterService findAll");
		List<YCharacter> list=null;
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		String userId=(String)session.getAttribute("userId");
		try {
			HashSet<String> characterSet=new HashSet<String>();
			if(id!=null&&!"".equals(id)){
				characterSet=getcharacterByuserId(id);
			}
			
			
			if(userId.equals("1")){//根据登录用户id判断是否是管理员
				list=characterService.findAll();
				List<Tree> treelist = new ArrayList<Tree>();
				if(null!=characterSet&&!"".equals(characterSet)){
					createCharacterTree(list,treelist,characterSet);
				}else{
					createCharacterTree(list,treelist,null);
				}
				
				arrayJson = treeToJson(treelist);
			}else{
				Map<String, Object> sessionMap = new HashMap<String, Object>();
				sessionMap = (Map<String, Object>)session.getAttribute("map");
				HashSet<String> characterset=(HashSet<String>)sessionMap.get("characterset");//根据登录用户获取用户拥有的角色权限
				List<YCharacter> characterList=new ArrayList<YCharacter>();
				for(String fid:characterset){
					characterList.add(characterService.getById(fid));
				}
				List<Tree> treelist=new ArrayList<Tree>();
				if(null!=characterSet&&!"".equals(characterSet)){
					createCharacterTree(characterList,treelist,characterSet);
				}else{
					createCharacterTree(characterList,treelist,null);
				}
				
				arrayJson = treeToJson(treelist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayJson;
	}
	/**
	 * 创建树的方法
	 */
	private void createCharacterTree(List<YCharacter> characterList,List<Tree> treelist,HashSet<String> characerset){
		
		for(YCharacter c:characterList){
			Tree tree = new Tree();
			tree.setId(c.getFid());
			tree.setText(c.getFname());
			if(null!=characerset&&!"".equals(characerset)&&characerset.size()>0){
				for(String character:characerset){
					if(c.getFid().equals(character)){
						tree.setChecked(true);
						break;
					}
				}
			}
			tree.setLeaf(true);
			treelist.add(tree);
			
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
	
	@RequestMapping("/getAllDynamicPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询全部角色并分页，或根据条件查询角色")
	public Map<String, Object> getAllDynamicPagingList(@RequestParam("start")Integer start,@RequestParam("limit")Integer limit,String searchName){
		logger.info("characterService getAllDynamicPagingList");
		PagingList<YCharacter> pageList =null;
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			pageList = characterService.getAllDynamicPagingList(start, limit,searchName);
			
			map.put("msg", "成功");
			map.put("obj", pageList);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/updateState")
	@ResponseBody
	@SystemControllerLog(description = "修改角色状态")
	public Map<String,Object> updateState(String[] fids,String stateId){
		logger.info("characterService updateState");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			for(String id:fids){
				YCharacter character=characterService.getById(id);
				character.setFstate(Integer.parseInt(stateId));
				characterService.update(character);
			}
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	@RequestMapping("/getByorgOrgroup")
	@ResponseBody
	@SystemControllerLog(description = "根据组织id或团体id查询角色")
	public Map<String,Object> getByorgOrgroup(String groupId,String orgId){
		logger.info("characterService getByorgOrgroup");
		List<YCharacter> list=null;
		Map<String, Object> map = new HashMap<String,Object>();
		System.out.println("groupId **************"+groupId+"orgId***********"+orgId);
		try {
			if("".equals(groupId)||null==groupId){
				list=characterService.getByHql("from YCharacter as y where y.fisGroups=0 and y.YBasicOrganization.fid='"+orgId+"'");
			}else{
				list=characterService.getByHql("from YCharacter as y where y.fisGroups=1 and y.fgroupsID='"+groupId+"'");
			}
			
			map.put("msg", "成功");
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
	 * 根据用户id查询用户所拥有角色
	 */
	private HashSet<String> getcharacterByuserId(String adminId) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<YRoleauthorization> rolelist = roleauthorizationService
				.findByHql("from YRoleauthorization as y where y.YSystemUsers.fid='"
						+ adminId + "'");
		
		HashSet<String> characterset=new HashSet<String>();
		
		///获取角色权限表中的数据
		for (YRoleauthorization ro : rolelist) {
			if(null !=ro.getYCharacter()&&!"".equals(ro.getYCharacter())){
				characterset.add(ro.getYCharacter().getFid());
			}
			
		}
		
		return characterset;
	}
}
