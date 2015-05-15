package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;

/**
 * @ClassName: YSystemconfigurationFunctionService 
 * @Description: TODO 模块表接口 
 * @date 2014年11月24日 下午2:55:41 
 * @author Ian
 *
 */
public interface YSystemconfigurationFunctionService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 添加模块
	 * @param @param SystemconfigurationFunction    
	 * @return void   
	 * @throws
	 */
	public void save(YSystemconfigurationFunction SystemconfigurationFunction);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除模块
	 * @param @param SystemconfigurationFunction    
	 * @return void   
	 * @throws
	 */
	public void delete(YSystemconfigurationFunction SystemconfigurationFunction);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改模块
	 * @param @param SystemconfigurationFunction    
	 * @return void   
	 * @throws
	 */
	public void update(YSystemconfigurationFunction SystemconfigurationFunction);
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据模块id查询模块
	 * @param @param functionid
	 * @param @return    
	 * @return YSystemconfigurationFunction   
	 * @throws
	 */
	public YSystemconfigurationFunction getById(String functionid);
	
	/**
	 * 
	 * @Title: findByHql 
	 * @Description: TODO 根据Hql查询模块
	 * @param @param hql
	 * @param @return    
	 * @return List<YSystemconfigurationFunction>   
	 * @throws
	 */
	public List<YSystemconfigurationFunction> findByHql(String hql);
	/**
	 * 
	 * @Title: findAll 
	 * @Description: TODO 查询所有的模块
	 * @param @return    
	 * @return List<YSystemconfigurationFunction>   
	 * @throws
	 */
	public List<YSystemconfigurationFunction> findAll();
}
