package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;

/**
 * @author：jackpeng
 * @date：2014年12月27日下午2:18:56
 * 上墙活动参与人service接口
 */
public interface JoinActivityServiceI {
	
	/**
	 * 根据指定人id查询参与人
	 * @param id
	 * @return
	 */
	public List<YBasicJoinActivity> getById(String id);
	
}
