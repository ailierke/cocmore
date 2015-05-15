package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicType;

/**
 * @author：jackpeng
 * @date：2014年12月13日下午2:42:25
 * 类型service接口
 */
public interface TypeService {
	
	/**
	 * hql查询类型
	 * @param hql
	 * @return
	 */
	public List<YBasicType> getTypeHql(String hql);
	
}
