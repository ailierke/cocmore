package com.yunzo.cocmore.core.function.service; 

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @ClassName: CharacterService 
 * @Description: TODO(角色Service) 
 * @date 2014年11月24日 上午11:14:56 
 * @author Ian
 *
 */
public interface CharacterService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 新增角色
	 * @param @param character    
	 * @return void   
	 * @throws
	 */
	public void save(YCharacter character);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改角色
	 * @param @param character    
	 * @return void   
	 * @throws
	 */
	public void update(YCharacter character);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除角色信息
	 * @param @param character    
	 * @return void   
	 * @throws
	 */
	public void delete(YCharacter character);

	/**
	 * 
	 * @Title: findAll 
	 * @Description: TODO 查询所有的角色信息
	 * @param @return    
	 * @return List<YCharacter>   
	 * @throws
	 */
	public List<YCharacter> findAll();
	/**
	 * 
	 * @Title: getAllDynamicPagingList 
	 * @Description: TODO 分页显示所有的角色信息
	 * @param @param page
	 * @param @param pageSize
	 * @param @return    
	 * @return PagingList<YCharacter>   
	 * @throws
	 */
	public PagingList<YCharacter> getAllDynamicPagingList(Integer page,Integer pageSize,String searchName);
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询角色
	 * @param @param fid
	 * @param @return    
	 * @return YCharacter   
	 * @throws
	 */
	public YCharacter getById(String fid);
	
	/**
	 * 
	 * @Title: getByHql 
	 * @Description: TODO 根据hql查询角色
	 * @param @param hql
	 * @param @return    
	 * @return List<YCharacter>   
	 * @throws
	 */
	public List<YCharacter> getByHql(String hql);
}
