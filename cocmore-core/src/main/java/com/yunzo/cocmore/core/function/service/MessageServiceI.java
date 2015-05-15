package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <系统消息服务接口>. <br>
 * @date:2014年11月27日 下午4:21:10
 * @author beck
 * @version V1.0                             
 */

public interface MessageServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YSystemconfigurationMessage> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YSystemconfigurationMessage getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YSystemconfigurationMessage demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(YSystemconfigurationMessage demo);
	
	public void saveOrUpdate(YSystemconfigurationMessage demo);
}
