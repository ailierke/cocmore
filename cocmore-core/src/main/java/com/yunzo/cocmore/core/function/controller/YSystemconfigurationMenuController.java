package com.yunzo.cocmore.core.function.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;
import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMenu;
import com.yunzo.cocmore.core.function.service.YFunctionentriesFunctionjournalentryService;
import com.yunzo.cocmore.core.function.service.YOrganizationauthorizedService;
import com.yunzo.cocmore.core.function.service.YRoleauthorizationService;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationFunctionService;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationMenuService;

/**
 * @ClassName: YSystemconfigurationMenuController
 * @Description: TODO 菜单Controller
 * @date 2014年11月24日 下午5:05:15
 * @author Ian
 * 
 */
@RestController
@RequestMapping("/systemconfigurationMenu")
public class YSystemconfigurationMenuController {
	private static final Logger logger = Logger
			.getLogger(YSystemconfigurationMenuController.class);
	@Resource(name = "menuService")
	private YSystemconfigurationMenuService menuService;

	@Resource(name = "organizationauthorizedService")
	private YOrganizationauthorizedService organizationauthorizedService;

	@Resource(name = "roleauthorizationService")
	private YRoleauthorizationService roleauthorizationService;

	@Resource(name = "systemconfigurationFunctionService")
	private YSystemconfigurationFunctionService systemconfigurationFunctionService;

	@Resource(name = "functionentriesFunctionjournalentryService")
	private YFunctionentriesFunctionjournalentryService functionentriesFunctionjournalentryService;

	@RequestMapping(value = "/findAll")
	@ResponseBody
	public Map<String, Object> findAll() {
		logger.info("YSystemconfigurationMenu findAll");
		Map<String, Object> map = new HashMap<String, Object>();
		List<YSystemconfigurationMenu> list = null;
		try {
			list = menuService.findAll();
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
	 * 创建第一集菜单
	 * @param adminId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getMenu")
	@ResponseBody
	@SystemControllerLog(description="创建第一集菜单")
	public List<menu> getMenu(String adminId,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<YSystemconfigurationMenu> list = new ArrayList<YSystemconfigurationMenu>();
		//List<YSystemconfigurationMenu> menulist = null;
		List<menu> menus = new ArrayList<YSystemconfigurationMenuController.menu>();
		session.setAttribute("userId", adminId);
		try {
			if(adminId.equals("1")){
				list = menuService
						.findByHql("from YSystemconfigurationMenu as m where m.fsuperiorId=null");
				// menulist=menuService.findByHql("from YSystemconfigurationMenu");
				if (list.size() > 0) {
					for (int i = 1; i <= list.size(); i++) {
						menu m = new menu();
						m.setBorder(false);
						m.setContentEl("root" + list.get(i - 1).getFid() + "01");
						m.setIconCls("nav");
						m.setTitle(list.get(i - 1).getFmenuName());
						m.setMid(list.get(i - 1).getFid());
						menus.add(m);
					}
				}
			}else{
				map=getroleandorgByuserId(adminId);
				session.setAttribute("map", map);
				List<YSystemconfigurationMenu> ms=new ArrayList<YSystemconfigurationMenu>();
				ms=getMenuList(session);//获取用户拥有权限的菜单集合
				session.setAttribute("menulist", ms);
				for(YSystemconfigurationMenu m:ms){
					//System.out.println("------------------"+m.getFid()+"-----------------------");
					if(null==m.getFsuperiorId()||"".equals(m.getFsuperiorId())){//判断是否是第一级菜单
						list.add(m);
					}
				}
				// menulist=menuService.findByHql("from YSystemconfigurationMenu");
				if (list.size() > 0) {
					for (int i = 1; i <= list.size(); i++) {
						menu m = new menu();
						m.setBorder(false);
						m.setContentEl("root" + list.get(i - 1).getFid() + "01");
						m.setIconCls("nav");
						m.setTitle(list.get(i - 1).getFmenuName());
						m.setMid(list.get(i - 1).getFid());
						menus.add(m);
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menus;
	}
	
	/**
	 * 根据第一级菜单id加载下面的菜单树
	 * @param menuId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/createMenutree")
	@ResponseBody
	@SystemControllerLog(description="根据第一级菜单id加载下面的菜单树")
	public JSONArray createMenutree(String menuId,HttpSession session) {

		logger.info("YSystemconfigurationMenu createMenutree");
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray arrayJson = new JSONArray();
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			List<YSystemconfigurationMenu> menulist=new  ArrayList<YSystemconfigurationMenu>();
			String userId=(String)session.getAttribute("userId");
			List<YSystemconfigurationMenu> ms=new ArrayList<YSystemconfigurationMenu>();
			if(userId.equals("1")){
				menulist = menuService.findByHql("from YSystemconfigurationMenu as y where y.fsuperiorId='"
						+ menuId + "'");
			}else{
				ms=(List<YSystemconfigurationMenu>)session.getAttribute("menulist");
				for(YSystemconfigurationMenu m:ms){//获取用户拥有的菜单
					if(menuId.equals(m.getFsuperiorId())){
						menulist.add(m);
					}
				}
			}
			//生成菜单树
			for (YSystemconfigurationMenu m : menulist) {
				Tree tree = new Tree();
				tree.setId(m.getFid());
				tree.setText(m.getFmenuName());
				tree.setUrl(m.getFpath());
				createTreechild(m.getFid(), tree ,userId,ms);
				treelist.add(tree);
			}
			arrayJson = treeToJson(treelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayJson;
	}

	// /**
	// * 生成树的方法
	// */
	// @RequestMapping(value = "/getMenuTree")
	// @ResponseBody
	// public JSONArray getMenuTree(){
	// logger.info("YSystemconfigurationMenu getMenuTree");
	// Map<String, Object> map = new HashMap<String,Object>();
	// JSONArray arrayJson = new JSONArray();
	// try {
	// List<Tree> treelist = new ArrayList<Tree>();
	// createTree(treelist);
	// arrayJson = treeToJson(treelist);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return arrayJson;
	// }
	// /**
	// * 创建树干
	// * @Title: createTree
	// * @Description: TODO
	// * @param
	// * @return void
	// * @throws
	// */
	// private void createTree(List<Tree> treelist){
	// List<YSystemconfigurationMenu> menulist =
	// menuService.findByHql("from YSystemconfigurationMenu");
	//
	// for(YSystemconfigurationMenu m:menulist){
	// if(m.getFsuperiorId()==null||m.getFsuperiorId().equals("")){
	// Tree tree = new Tree();
	// tree.setId(m.getFid());
	// tree.setText(m.getFmenuName());
	// createTreechild(m.getFid(),tree);
	// treelist.add(tree);
	// }
	// }
	// }
	
	/**
	 * 根据页面加载出工具栏
	 * @param functionId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getfunctionjs")
	@ResponseBody
	@SystemControllerLog(description="根据页面加载出工具栏")
	public List<String> getfunctionjs(String functionId,HttpSession session) {
		logger.info("YSystemconfigurationMenu getfunctionjs");
		List<String> jslist = new ArrayList<String>();
		List<YFunctionentriesFunctionjournalentry> functionlist=new ArrayList<YFunctionentriesFunctionjournalentry>();
		String userId=(String)session.getAttribute("userId");//获取登录用户的id
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(userId==null){
			return null;
		}
		if(userId.equals("1")){
			functionlist = functionentriesFunctionjournalentryService.findByFunctionID(functionId);
		}else{
			map=(Map<String, Object>)session.getAttribute("map");//获取session中的map集合
			List<YFunctionentriesFunctionjournalentry> fflist=new ArrayList<YFunctionentriesFunctionjournalentry>();
			fflist=(List<YFunctionentriesFunctionjournalentry>)map.get("fflist");//获取功能集合
			for(YFunctionentriesFunctionjournalentry f:fflist){//获取功能点的集合
				if(f.getYSystemconfigurationFunction().getFid().equals(functionId)){
					functionlist.add(f);
				}
			}
		}
		//对拥有的功能点进行排序输出
		Collections.sort(functionlist, new Comparator<YFunctionentriesFunctionjournalentry>() {  
			            public int compare(YFunctionentriesFunctionjournalentry arg0, YFunctionentriesFunctionjournalentry arg1) {  
			                int hits0 = Integer.parseInt(arg0.getFseq());  
			                int hits1 = Integer.parseInt(arg1.getFseq());  
			                if (hits1 < hits0) {  
			                    return 1;  
			                } else if (hits1 == hits0) {  
			                    return 0;  
			                } else {  
			                    return -1;  
			                }  
			            }  
			        });  
		//获取每个功能的js代码	          
		for (int i = 0; i < functionlist.size(); i++) {
			jslist.add(functionlist.get(i).getFjscode());
		}
		return jslist;
	}
	/**
	 * 根据用户所拥有的权限查询出所能访问的菜单集合
	 * @param session
	 * @return
	 */
	public List<YSystemconfigurationMenu> getMenuList(HttpSession session) {
		List<YSystemconfigurationMenu> menuList = new ArrayList<>();
		List<YSystemconfigurationMenu> menus = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = (Map<String, Object>)session.getAttribute("map");//getroleandorgByuserId(adminId);
		//session.setAttribute("map", map);
		
		//List<YFunctionentriesFunctionjournalentry> fflist = (List<YFunctionentriesFunctionjournalentry>) map.get("fflist");
		List<YSystemconfigurationFunction> sflist = (List<YSystemconfigurationFunction>) map.get("sflist");
		//得到集合中的菜单数据
		for (YSystemconfigurationFunction s : sflist) {
			menuList.add(s.getYSystemconfigurationMenu());
		}
		//遍历所有的上级菜单为后面生成树做准备
		for(YSystemconfigurationMenu m:menuList){
			menus.add(m);
			createmenu(menus,m.getFsuperiorId());
		}
		//循环去重
		for (int i = 0; i < menus.size() - 1; i++) {
			for (int j = menus.size() - 1; j > i; j--) {
				if (menus.get(j).getFid().equals(menus.get(i).getFid())) {
					menus.remove(j);
				}
			}
		}
		return menus;
	}
	/**
	 * 循环遍历出所有的上级菜单
	 * @param menuList
	 * @param mid
	 */
	private void createmenu(List<YSystemconfigurationMenu> menuList,String mid){
		YSystemconfigurationMenu y=new YSystemconfigurationMenu();
		y=menuService.getById(mid);//根据id查询菜单对象
		menuList.add(y);
		//判断是否拥有上级菜单如果有上级菜单就递归查找上级菜单直到最上级为止
		if(null!=y.getFsuperiorId()&&!"".equals(y.getFsuperiorId())){
			createmenu(menuList,y.getFsuperiorId());
		}
	}
	/**
	 * 根据用户id查询用户所拥有的模块和功能的集合
	 */
	private Map<String, Object> getroleandorgByuserId(String adminId) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<YOrganizationauthorized> organlist = organizationauthorizedService
				.findByHql("from YOrganizationauthorized as y where y.YSystemUsers.fid='"
						+ adminId + "'");
		List<YRoleauthorization> rolelist = roleauthorizationService
				.findByHql("from YRoleauthorization as y where y.YSystemUsers.fid='"
						+ adminId + "'");

		List<YFunctionentriesFunctionjournalentry> fflist = new ArrayList<YFunctionentriesFunctionjournalentry>();
		List<YSystemconfigurationFunction> sflist = new ArrayList<YSystemconfigurationFunction>();
		
		List<String> orglist = new ArrayList<String>();
		List<String> grouplist = new ArrayList<String>();
		HashSet<String> characterset=new HashSet<String>();
		//获取用户权限表中的功能数据
		for (YOrganizationauthorized yo : organlist) {
			if (null == yo.getYFunctionentriesFunctionjournalentry()
					|| "".equals(yo.getYFunctionentriesFunctionjournalentry())) {
				sflist.add(yo.getYSystemconfigurationFunction());
			} else {
				sflist.add(yo.getYSystemconfigurationFunction());
				fflist.add(yo.getYFunctionentriesFunctionjournalentry());
			}
		}
		///获取角色权限表中的数据
		for (YRoleauthorization ro : rolelist) {
			if (null == ro.getYFunctionentriesFunctionjournalentry()
					|| "".equals(ro.getYFunctionentriesFunctionjournalentry())) {
				sflist.add(ro.getYSystemconfigurationFunction());
			} else {
				sflist.add(ro.getYSystemconfigurationFunction());
				fflist.add(ro.getYFunctionentriesFunctionjournalentry());
			}
			if(null !=ro.getfGroupsId() &&!"".equals(ro.getfGroupsId())){
				grouplist.add(ro.getfGroupsId());                   //获取团体id
			}
			if(null !=ro.getForganizationId() &&!"".equals(ro.getForganizationId())){
				orglist.add(ro.getForganizationId());               //获取组织id
			}
			if(null !=ro.getYCharacter()&&!"".equals(ro.getYCharacter())){
				characterset.add(ro.getYCharacter().getFid());
			}
			
		}
		//循环去重
		for (int i = 0; i < sflist.size() - 1; i++) {
			for (int j = sflist.size() - 1; j > i; j--) {
				if (sflist.get(j).getFid().equals(sflist.get(i).getFid())) {
					sflist.remove(j);
				}
			}
		}
		//循环去重
		for (int i = 0; i < fflist.size() - 1; i++) {
			for (int j = fflist.size() - 1; j > i; j--) {
				if (fflist.get(j).getFid().equals(fflist.get(i).getFid())) {
					fflist.remove(j);
				}
			}
		}
		HashSet<String> oSet=new HashSet<String>();
		oSet.addAll(orglist);
		HashSet<String> orgSet=new HashSet<String>();
		for(String org:oSet){
			String[] o=org.split(",");
			for(String oid:o){
				orgSet.add(oid);
			}
		}
		HashSet<String> gSet=new HashSet<String>();
		gSet.addAll(grouplist);
		HashSet<String> groupSet=new HashSet<String>();
		for(String group:gSet){
			String[] g=group.split(",");
			for(String gid:g){
				groupSet.add(gid);
			}
		}
		
		map.put("groupSet", groupSet);
		map.put("orgSet", orgSet);
		map.put("sflist", sflist);
		map.put("fflist", fflist);
		map.put("characterset", characterset);
		// map.put("sflist", removeDuplicate(sflist));
		// map.put("fflist", removeDuplicate(fflist));
		return map;
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
	private void createTreechild(String id, Tree tree,String userid,List<YSystemconfigurationMenu> mlist) {
		List<YSystemconfigurationMenu> list =new  ArrayList<YSystemconfigurationMenu>(); 
		//判断是否是管理员如果是就直接查询所有的菜单 如果不是就判断用户所有拥有的菜单
		if(userid.equals("1")){
			list = menuService.findByHql("from YSystemconfigurationMenu as y where y.fsuperiorId='"+ id + "'");
		}else{
			for(YSystemconfigurationMenu m:mlist){
				if(id.equals(m.getFsuperiorId())){
					list.add(m);
				}
			}
		}
		if (list != null && list.size() > 0) {
			for (YSystemconfigurationMenu me : list) {
				Tree child = new Tree();
				child.setId(me.getFid());
				child.setText(me.getFmenuName());
				child.setUrl(me.getFpath());
				createTreechild(me.getFid(), child,userid,mlist);
				tree.getChildren().add(child);
			}
		} else {
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
	private JSONArray treeToJson(List<Tree> tree) {
		JSONArray array = new JSONArray();
		for (Tree childrenTree : tree) {
			JSONObject object = new JSONObject();
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("url", childrenTree.getUrl());
			object.put("leaf", childrenTree.getLeaf());
			object.put("text", childrenTree.getText());
			if (childrenTree.getChildren().size() > 0) {
				object.put("children", treeToJson(childrenTree.getChildren()));
			}
			array.add(object);
		}
		return array;
	}

	class menu {
		private String contentEl = "root101";
		private String title = "团体管理";
		private boolean border = false;
		private String iconCls = "nav";
		private String mid;

		public String getContentEl() {
			return contentEl;
		}

		public void setContentEl(String contentEl) {
			this.contentEl = contentEl;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public boolean isBorder() {
			return border;
		}

		public void setBorder(boolean border) {
			this.border = border;
		}

		public String getIconCls() {
			return iconCls;
		}

		public void setIconCls(String iconCls) {
			this.iconCls = iconCls;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

	}

	class Tree {
		private String cls = "folder";
		private String id;
		private String url;
		private boolean leaf = false;
		// private boolean checked=true;
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

		// public boolean getChecked() {
		// return checked;
		// }
		// public void setChecked(boolean checked) {
		// this.checked = checked;
		// }
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

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}
}
