package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMenu;

/**
 * @ClassName: YSystemconfigurationMenuService 
 * @Description: TODO菜单表接口 
 * @date 2014年11月24日 下午4:35:46 
 * @author Ian
 *
 */
public interface YSystemconfigurationMenuService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 添加菜单
	 * @param @param menu    
	 * @return void   
	 * @throws
	 */
	public void save(YSystemconfigurationMenu menu);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改菜单
	 * @param @param menu    
	 * @return void   
	 * @throws
	 */
	public void update(YSystemconfigurationMenu menu);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除菜单
	 * @param @param menu    
	 * @return void   
	 * @throws
	 */
	public void delete(YSystemconfigurationMenu menu);
	/**
	 * 
	 * @Title: findAll 
	 * @Description: TODO 查询所有的菜单
	 * @param @return    
	 * @return List<YSystemconfigurationMenu>   
	 * @throws
	 */
	public List<YSystemconfigurationMenu> findAll();
	/**
	 * 
	 * @Title: findByHql 
	 * @Description: TODO 根据hql语句查询菜单
	 * @param @param hql
	 * @param @return    
	 * @return List<YSystemconfigurationMenu>   
	 * @throws
	 */
	public List<YSystemconfigurationMenu> findByHql(String hql);
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询菜单
	 * @param @param fid
	 * @param @return    
	 * @return YSystemconfigurationMenu   
	 * @throws
	 */
	public YSystemconfigurationMenu getById(String fid);
}
