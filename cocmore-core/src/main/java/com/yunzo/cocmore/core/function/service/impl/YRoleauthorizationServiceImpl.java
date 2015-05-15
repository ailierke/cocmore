package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.service.YRoleauthorizationService;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.util.Tree;
import com.yunzo.cocmore.core.function.util.Tree1;
import com.yunzo.cocmore.core.function.vo.GroupTreeVo;

/**
 * @ClassName: YRoleauthorizationServiceImpl 
 * @Description: TODO 角色授权关系接口实现类 
 * @date 2014年11月25日 下午4:33:13 
 * @author Ian
 *
 */
@Service("roleauthorizationService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class YRoleauthorizationServiceImpl implements YRoleauthorizationService {

	private static final Logger logger = Logger.getLogger(YRoleauthorizationServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "新增角色授权关系")
	public void save(YRoleauthorization roleauthorization) {
		dao.save(roleauthorization);
	}

	@Override
	@SystemServiceLog(description = "修改角色授权关系")
	public void update(YRoleauthorization roleauthorization) {
		dao.update(roleauthorization);
	}

	@Override
	@SystemServiceLog(description = "删除角色授权关系")
	public void delete(YRoleauthorization roleauthorization) {
		dao.delete(roleauthorization);
	}

	@Override
	@SystemServiceLog(description = "根据角色id查询角色授权关系")
	public List<YRoleauthorization> findByCharacterId(String characterId) {
		return (List<YRoleauthorization>)dao.findAllByHQL("from YRoleauthorization as y where y.YCharacter.fid='"+characterId+"'");
	}

	@Override
	@SystemServiceLog(description = "根据id角色授权关系")
	public YRoleauthorization getById(String fid) {
		return (YRoleauthorization)dao.findById(YRoleauthorization.class, fid);
	}

	@Override
	@SystemServiceLog(description = "hql查询角色授权关系")
	public List<YRoleauthorization> findByHql(String hql) {
		return (List<YRoleauthorization>)dao.findAllByHQL(hql);
	}

	
	/**
	 * 管理员用户创建树干
	 * @Title: createTree 
	 * @Description: TODO 
	 * @param     
	 * @return void   
	 * @throws
	 */
	public  void createTree(List<Tree> treelist,String orgId){
		//通过组织或者groupName来查询所有的企且拼装成树结构
		List<YBasicSocialgroups> groupList = (List<YBasicSocialgroups>) dao.findAllByHQL("from YBasicSocialgroups as y where y.YBasicOrganization.fid='"+orgId+"'");
		System.out.println(groupList);
		for(YBasicSocialgroups o:groupList){
			if(o.getFsuperSocialGroupsId()==null||o.getFsuperSocialGroupsId().equals("")){
				Tree tree = new Tree();
				tree.setId(o.getFid());
				tree.setText(o.getFname());
				createTreechild(o.getFid(),tree);
				treelist.add(tree);
			}
		}
	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建管理员用户子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	private void createTreechild(String id,Tree tree){
		List<YBasicSocialgroups> list = (List<YBasicSocialgroups>)dao.findAllByHQL("from YBasicSocialgroups as g where g.fsuperSocialGroupsId='"+id+"'");
		if(list!=null&&list.size()>0){
			for(YBasicSocialgroups me : list){
				Tree child = new Tree();
				child.setId(me.getFid());
				child.setText(me.getFname());
				createTreechild(me.getFid(),child);
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}

	/**
	 * 非管理员用户创建树干
	 */
	@Override
	public void creategroupTree(List<Tree> list, String id,HashSet<String> groupSet) {
		//得到权限内的团体集合
		List<YBasicSocialgroups> grouplist=new ArrayList<YBasicSocialgroups>();
		for(String fid:groupSet){
			grouplist.add((YBasicSocialgroups)dao.findById(YBasicSocialgroups.class,fid));
		}
		//得到权限内的所属组织的团体集合
		List<YBasicSocialgroups> groups=new ArrayList<YBasicSocialgroups>();
		for(YBasicSocialgroups group:grouplist){
			if(group.getYBasicOrganization().getFid().equals(id)){
				groups.add(group);
			}
		}
		
		for(YBasicSocialgroups group:groups){
			boolean t=true;
			for(YBasicSocialgroups g:groups){
				if("".equals(group.getFsuperSocialGroupsId())||null==group.getFsuperSocialGroupsId()){
					t=true;
					break;
				}else{
					if(group.getFsuperSocialGroupsId().equals(g.getFid())){
						t=false;
						break;
					}
				}
			}
			if(t){
				Tree tree = new Tree();
				tree.setId(group.getFid());
				tree.setText(group.getFname());
				creategroupTreelist(group.getFid(),tree,groups);
				list.add(tree);
			}
		}
		
	}
	/**
	 * 创建子树
	 * @param id
	 * @param tree
	 * @param groups
	 */
	private void creategroupTreelist(String id,Tree tree,List<YBasicSocialgroups> groups){
		List<YBasicSocialgroups> list = new ArrayList<YBasicSocialgroups>();
		for(YBasicSocialgroups g:groups){
			if(!"".equals(g.getFsuperSocialGroupsId())&&null!=g.getFsuperSocialGroupsId()){
				if(g.getFsuperSocialGroupsId().equals(id)){
					list.add(g);
					
				}
			}
		}
		if(list!=null&&list.size()>0){
			for(YBasicSocialgroups g:list){
				//groups.remove(g);
				Tree child = new Tree();
				child.setId(g.getFid());
				child.setText(g.getFname());
				creategroupTreelist(g.getFid(),child,groups);
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 主要用来查询团体树
	 */
	public List<GroupTreeVo> getAllGroupsPagingListByTree(String groupId,String orgId) {
		List<Object> values = new ArrayList<Object>();
		List<GroupTreeVo> list = new ArrayList<GroupTreeVo>();
		StringBuffer sql= new StringBuffer("select fid,fname,fbillState,fsuperSocialGroupsId from y_basic_socialgroups groups "); 
		/**
		 * 通过groupId
		 */
		if(null!=groupId&&!"".equals(groupId)){
			sql.append(" where groups.FSuperSocialGroupsID='"+groupId+"'");
			values.add(groupId);
		}
		/**
		 * 如果前台传来组织id就查询组织下面的所有团体
		 */
		if(null!=orgId&&!"".equals(orgId)){
			if(values.size()>0){
				sql.append(" and ");
			}else{
				sql.append(" where ");
			}
			sql.append("groups.FCompaniesID='"+orgId+"'");
			values.add(orgId);
		}
			/**
			 * 获得此页数据
			 */
		try {
			list = (List<GroupTreeVo>) dao.getListBySqlVO(sql.toString(),GroupTreeVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 主要用来查询团体树
	 */
	public GroupTreeVo getIDByTree(String groupId) {
		List<Object> values = new ArrayList<Object>();
		List<GroupTreeVo> list = new ArrayList<GroupTreeVo>();
		StringBuffer sql= new StringBuffer("select fid,fname,fbillState,fsuperSocialGroupsId from y_basic_socialgroups groups "); 
		/**
		 * 通过groupId
		 */
		if(null!=groupId&&!"".equals(groupId)){
			sql.append(" where groups.fid='"+groupId+"'");
			values.add(groupId);
		}
		try {
			list = (List<GroupTreeVo>) dao.getListBySqlVO(sql.toString(),GroupTreeVo.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.get(0);
	}
//	/**
//	 * 管理员用户创建树干
//	 * @Title: createTree 
//	 * @Description: TODO 
//	 * @param     
//	 * @return void   
//	 * @throws
//	 */
//	public  void createTree(List<Tree> treelist,String orgId){
//		//通过组织或者groupName来查询所有的企且拼装成树结构
//		List<GroupTreeVo> groupList = (List<GroupTreeVo>)getAllGroupsPagingListByTree(null,orgId);
//		System.out.println(groupList);
//		for(GroupTreeVo o:groupList){
//			if(o.getFsuperSocialGroupsId()==null||o.getFsuperSocialGroupsId().equals("")){
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
//	 * @Description: TODO 创建管理员用户子树
//	 * @param @param id
//	 * @param @param tree    
//	 * @return void   
//	 * @throws
//	 */
//	private void createTreechild(String id,Tree tree){
//		List<GroupTreeVo> list = (List<GroupTreeVo>)getAllGroupsPagingListByTree(id,null);
//		if(list!=null&&list.size()>0){
//			for(GroupTreeVo me : list){
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
//
//	/**
//	 * 非管理员用户创建树干
//	 */
//	@Override
//	public void creategroupTree(List<Tree> list, String id,HashSet<String> groupSet) {
//		//得到权限内的团体集合
//		List<GroupTreeVo> grouplist=new ArrayList<GroupTreeVo>();
//		for(String fid:groupSet){
//			grouplist.add((GroupTreeVo)dao.findById(YBasicSocialgroups.class,fid));
//		}
//		//得到权限内的所属组织的团体集合
//		List<GroupTreeVo> groups=new ArrayList<GroupTreeVo>();
//		for(GroupTreeVo group:grouplist){
//			if(group.getYBasicOrganization().getFid().equals(id)){
//				groups.add(group);
//			}
//		}
//		
//		for(GroupTreeVo group:groups){
//			boolean t=true;
//			for(GroupTreeVo g:groups){
//				if("".equals(group.getFsuperSocialGroupsId())||null==group.getFsuperSocialGroupsId()){
//					t=true;
//					break;
//				}else{
//					if(group.getFsuperSocialGroupsId().equals(g.getFid())){
//						t=false;
//						break;
//					}
//				}
//			}
//			if(t){
//				Tree tree = new Tree();
//				tree.setId(group.getFid());
//				tree.setText(group.getFname());
//				creategroupTreelist(group.getFid(),tree,groups);
//				list.add(tree);
//			}
//		}
//		
//	}
//	/**
//	 * 创建子树
//	 * @param id
//	 * @param tree
//	 * @param groups
//	 */
//	private void creategroupTreelist(String id,Tree tree,List<GroupTreeVo> groups){
//		List<GroupTreeVo> list = new ArrayList<GroupTreeVo>();
//		for(GroupTreeVo g:groups){
//			if(!"".equals(g.getFsuperSocialGroupsId())&&null!=g.getFsuperSocialGroupsId()){
//				if(g.getFsuperSocialGroupsId().equals(id)){
//					list.add(g);
//					
//				}
//			}
//		}
//		if(list!=null&&list.size()>0){
//			for(GroupTreeVo g:list){
//				//groups.remove(g);
//				Tree child = new Tree();
//				child.setId(g.getFid());
//				child.setText(g.getFname());
//				creategroupTreelist(g.getFid(),child,groups);
//				tree.getChildren().add(child);
//			}
//		}else{
//			tree.setLeaf(true);
//		}
//	}
	
	
	/**
	 * 创建树干
	 * @Title: createTree 
	 * @Description: TODO 
	 * @param     
	 * @return void   
	 * @throws
	 */
	@Override
	public void createTree1(List<Tree> treelist,String orgId,String userId,HashSet<String> groupSet,HashSet<String> groupset){
		//通过组织或者groupName来查询所有的企且拼装成树结构
		List<GroupTreeVo> groupList = this.getAllGroupsPagingListByTree(null, orgId);
		List<GroupTreeVo> groupsList = new ArrayList<GroupTreeVo>();
		if(userId.equals("1")){
			groupsList=groupList;
		}else{
			for(GroupTreeVo o:groupList){
				for(String groupId:groupSet){
					if(o.getFid().equals(groupId)){
						groupsList.add(o);
					}
				}
			}
		}
//		if(type!=null&&type.equals("search")){//如果是查询，显示的就没有层级关系
//			for(Object object:groupsList){
//					GroupTreeVo  o = (GroupTreeVo)object;
//					Tree tree = new Tree();
//					String statu = o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":"";
//					tree.setId(o.getFid());
//					tree.setText(o.getFname()+ statu);
//					tree.setLeaf(true);
//					treelist.add(tree);
//			}
//		}else{
			try {

				for(GroupTreeVo o:groupsList){
					try {
					String statu = o.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":"";
					if(o.getFsuperSocialGroupsId()==null||o.getFsuperSocialGroupsId().equals("")){
						Tree tree = new Tree();
						tree.setId(o.getFid());
						tree.setText(o.getFname()+ statu);
						if(null!=groupset&&!"".equals(groupset)&&groupset.size()>0){
							for(String g:groupset){
								if(g.equals(o.getFid())){
									tree.setChecked(true);
									break;
								}
							}
						}
						createTreechild1(o.getFid(),tree,userId,groupSet,groupset);
						treelist.add(tree);
					}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
//	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	@SuppressWarnings("unused")
	private  void createTreechild1(String id,Tree tree,String userId,HashSet<String> groupSet,HashSet<String> groupset){
		List<GroupTreeVo> list = getAllGroupsPagingListByTree(id,null);
		List<GroupTreeVo> lists = new ArrayList<GroupTreeVo>();
		if(userId.equals("1")){
			lists=list;
		}else{
			for(GroupTreeVo o:lists){
				for(String groupId:groupSet){
					if(o.getFid().equals(groupId)){
						lists.add(o);
					}
				}
			}
		}
		if(lists!=null&&lists.size()>0){
			for(GroupTreeVo me : lists){
				Tree child = new Tree();
				String statu = me.getFbillState()==Status.UNEFFECT.value()?"(<span style='color:red'>封存</span>)":"";
				
				child.setId(me.getFid());
				child.setText(me.getFname()+ statu);
				if(null!=groupset&&!"".equals(groupset)&&groupset.size()>0){
					for(String g:groupset){
						if(g.equals(me.getFid())){
							tree.setChecked(true);
							break;
						}
					}
				}
				createTreechild1(me.getFid(),child,userId,groupSet,groupset);
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}
	
}
