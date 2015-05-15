package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午9:17:34
 * 省份service接口
 */
public interface ProvinceService {
	/**
	 * 查询全部省份
	 * @return
	 */
	public List<YBasicProvince> findAll();
	
	/**
	 * 根据ID查询省份
	 * @param id
	 * @return
	 */
	public YBasicProvince getById(String id);
	
	/**
	 * hql查询
	 * @param hql
	 * @return
	 */
	public List<YBasicProvince> findByHql(String hql);
}
