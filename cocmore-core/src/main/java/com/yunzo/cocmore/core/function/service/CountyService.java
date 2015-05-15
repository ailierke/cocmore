package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午10:17:48
 * 区县service接口
 */
public interface CountyService {
	/**
	 * 查询全部区县
	 * @return
	 */
	public List<YBasicCounty> findAll();
	
	/**
	 * 根据省份id查询区县
	 * @param hql
	 * @return
	 */
	public List<YBasicCounty> getByHql(String hql);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public YBasicCounty getById(String id);
}
