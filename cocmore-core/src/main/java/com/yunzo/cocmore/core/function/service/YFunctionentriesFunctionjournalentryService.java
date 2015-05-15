package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;

/**
 * @ClassName: YFunctionentriesFunctionjournalentryService 
 * @Description: TODO(功能表接口) 
 * @date 2014年11月24日 下午3:14:37 
 * @author Ian
 *
 */
public interface YFunctionentriesFunctionjournalentryService {
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO 添加功能
	 * @param @param FunctionentriesFunctionjournalentry    
	 * @return void   
	 * @throws
	 */
	public void save(YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry);
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO 删除功能
	 * @param @param FunctionentriesFunctionjournalentry    
	 * @return void   
	 * @throws
	 */
	public void delete(YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry);
	/**
	 * 
	 * @Title: update 
	 * @Description: TODO 修改功能
	 * @param @param functionentriesFunctionjournalentry    
	 * @return void   
	 * @throws
	 */
	public void update(YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry);
	/**
	 * 
	 * @Title: findByFunctionID 
	 * @Description: TODO 根据模块id查询功能分录
	 * @param @param functionId
	 * @param @return    
	 * @return List<YFunctionentriesFunctionjournalentry>   
	 * @throws
	 */
	public List<YFunctionentriesFunctionjournalentry> findByFunctionID(String functionId);
	
	/**
	 * 
	 * @Title: findByHql 
	 * @Description: TODO 根据hql语句查询功能
	 * @param @param hql
	 * @param @return    
	 * @return List<YFunctionentriesFunctionjournalentry>   
	 * @throws
	 */
	public List<YFunctionentriesFunctionjournalentry> findByHql(String hql);
	/**
	 * 
	 * @Title: findAll 
	 * @Description: TODO 查询所有的功能
	 * @param @return    
	 * @return List<YFunctionentriesFunctionjournalentry>   
	 * @throws
	 */
	public List<YFunctionentriesFunctionjournalentry> findAll();
	/**
	 * 
	 * @Title: getById 
	 * @Description: TODO 根据id查询功能
	 * @param @param fid
	 * @param @return    
	 * @return YFunctionentriesFunctionjournalentry   
	 * @throws
	 */
	public YFunctionentriesFunctionjournalentry getById(String fid);
}
