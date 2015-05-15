package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月25日下午5:25:44
 * 供应担保内容service接口
 */
public interface AssurancecontentService {
	/**
	 * 查询全部供应担保内容
	 * @return
	 */
	public List<YBasicAssurancecontent> findAll();
	
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param groupId
	 * @param content
	 * @return
	 */
	public PagingList<YBasicAssurancecontent> getAllControllerPagingList(Integer page,Integer pageSize,String groupId,String content);
	
	/**
	 * 根据ID查询供应担保内容
	 * @param id
	 * @return
	 */
	public YBasicAssurancecontent getById(String id);
	
	/**
	 * 新增供应担保内容
	 * @param content
	 */
	public void save(YBasicAssurancecontent content);
	
	/**
	 * 删除供应担保内容
	 * @param content
	 */
	public void delete(YBasicAssurancecontent content);
	
	/**
	 * 修改供应担保内容
	 * @param content
	 */
	public void update(YBasicAssurancecontent content);
}
