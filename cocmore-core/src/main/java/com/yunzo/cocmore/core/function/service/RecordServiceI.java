package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.FAwardsRecord;

/**
 * @author：jackpeng
 * @date：2014年12月22日下午4:04:02
 * 抽奖记录service接口
 */
public interface RecordServiceI {
	
	/**
	 * 根据上墙主题id查询是否存在抽奖记录
	 * @param themeId
	 * @return
	 */
	public Boolean getByhql(String themeId);
	
	/**
	 * 根据上墙主题id查询抽奖记录信息
	 * @param hql
	 * @return
	 */
	public List<FAwardsRecord> getByThemeId(String themeId);
	
}
