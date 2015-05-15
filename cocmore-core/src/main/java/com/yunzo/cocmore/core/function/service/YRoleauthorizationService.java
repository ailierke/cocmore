package com.yunzo.cocmore.core.function.service;

import java.util.HashSet;
import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.util.Tree;

/**
 * @ClassName: YRoleauthorizationService 
 * @Description: TODO角色授权关系 
 * @date 2014年11月25日 下午3:24:56 
 * @author Ian
 *
 */
public interface YRoleauthorizationService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 新增
	 * @param @param roleauthorization    
	 * @return void   
	 * @throws
	 */
	public void save(YRoleauthorization roleauthorization);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改
	 * @param @param roleauthorization    
	 * @return void   
	 * @throws
	 */
	public void update(YRoleauthorization roleauthorization);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除
	 * @param @param roleauthorization    
	 * @return void   
	 * @throws
	 */
	public void delete(YRoleauthorization roleauthorization);
	/**
	 * 
	 * @Title: findByCharacterId 
	 * @Description: TODO 根据角色id查询角色关联关系信息
	 * @param @param characterId
	 * @param @return    
	 * @return List<YRoleauthorization>   
	 * @throws
	 */
	public List<YRoleauthorization> findByCharacterId(String characterId);
	
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询对象
	 * @param @param fid
	 * @param @return    
	 * @return YRoleauthorization   
	 * @throws
	 */
	public YRoleauthorization getById(String fid);
	
	/**
	 * 
	 * @Title: findByHql 
	 * @Description: TODO 根据hql语句查询列表
	 * @param @param hql
	 * @param @return    
	 * @return List<YRoleauthorization>   
	 * @throws
	 */
	public List<YRoleauthorization> findByHql(String hql);
	
	/**
	 * 生成团体
	 * @param treelist
	 * @param orgId
	 */
	public  void createTree(List<Tree> treelist,String orgId);
	
	/**
	 * 根据组织生成团体
	 * @param list
	 * @param id
	 * @param groupSet
	 */
	public void creategroupTree(List<Tree> list,String id,HashSet<String> groupSet);
	
	public void createTree1(List<Tree> treelist,String orgId,String userId,HashSet<String> groupSet,HashSet<String> groupset);
}
