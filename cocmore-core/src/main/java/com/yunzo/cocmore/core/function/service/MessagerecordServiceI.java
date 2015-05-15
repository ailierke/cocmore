package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessagerecord;

/**
 * @author：jackpeng
 * @date：2015年1月5日上午3:26:28
 * 
 */
public interface MessagerecordServiceI {
	
	public void saveOrUpdateAll(List<YSystemconfigurationMessagerecord> messagerecords);
	
}
