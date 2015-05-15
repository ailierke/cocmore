package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <编码规则服务接口>. <br>
 * @date:2014年11月27日 下午3:53:34
 * @author beck
 * @version V1.0                             
 */

public interface EncodingrulesServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YSystemconfigurationEncodingrules> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YSystemconfigurationEncodingrules getById(String fid);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YSystemconfigurationEncodingrules demo);
	
}
