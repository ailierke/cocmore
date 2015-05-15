package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午10:02:23
 * 城市service接口
 */
public interface CityService {
	/**
	 * 查询全部城市
	 * @return
	 */
	public List<YBasicCity> findAll();
	
	/**
	 * 根据省份id查询城市
	 * @param hql
	 * @return
	 */
	public List<YBasicCity> getByHql(String hql);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public YBasicCity getById(String id);
}
