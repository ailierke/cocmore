package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <组织服务接口>. <br>
 * @date:2014年11月24日 下午3:22:46
 * @author beck
 * @version V1.0                             
 */

public interface OrganizationService {
	/**
	 * 查询全部并分页
	 * @return
	 */
	public PagingList<YBasicOrganization> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 查询全部
	 * @return
	 */
	public List<YBasicOrganization> findAll();
	
	/**
	 * 根据参数ID查询
	 * 查询出参数ID除外的所有数据
	 * 为空的话查询出所有
	 * @param superId
	 * @return
	 */
	public List<YBasicOrganization> findAllBySuperId(String superId);
	
	public YBasicOrganization getById(String fid);
	
	public void save(YBasicOrganization demo);
	
	public void delete(YBasicOrganization demo);
	
	public void update(YBasicOrganization demo);
	
	public List<YBasicOrganization> getByHql(String hql);
}
