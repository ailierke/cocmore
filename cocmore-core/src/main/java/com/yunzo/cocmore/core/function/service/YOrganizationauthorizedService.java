package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;

/**
 * @ClassName: YOrganizationauthorizedService 
 * @Description: TODO 用户授权关系接口 
 * @date 2014年11月25日 下午3:24:27 
 * @author Ian
 *
 */
public interface YOrganizationauthorizedService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 添加
	 * @param @param organizationauthorized    
	 * @return void   
	 * @throws
	 */
	public void save(YOrganizationauthorized organizationauthorized);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改
	 * @param @param organizationauthorized    
	 * @return void   
	 * @throws
	 */
	public void update(YOrganizationauthorized organizationauthorized);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除
	 * @param @param organizationauthorized    
	 * @return void   
	 * @throws
	 */
	public void delete(YOrganizationauthorized organizationauthorized);
	/**
	 * 
	 * @Title: findByUserID 
	 * @Description: TODO 根据用户id查询关系
	 * @param @param userid
	 * @param @return    
	 * @return List<YOrganizationauthorized>   
	 * @throws
	 */
	public List<YOrganizationauthorized> findByUserID(String userid);
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询关系对象
	 * @param @param fid
	 * @param @return    
	 * @return YOrganizationauthorized   
	 * @throws
	 */
	public YOrganizationauthorized getById(String fid);
	/**
	 * 
	 * @Title: findByHql 
	 * @Description: TODO 根据hql语句查询关系列表
	 * @param @param hql
	 * @param @return    
	 * @return List<YOrganizationauthorized>   
	 * @throws
	 */
	public List<YOrganizationauthorized> findByHql(String hql);
}
