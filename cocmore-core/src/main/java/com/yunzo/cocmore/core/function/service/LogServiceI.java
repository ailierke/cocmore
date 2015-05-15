package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationLog;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <日志服务接口>. <br>
 * @date:2014年11月27日 上午11:46:57
 * @author beck
 * @version V1.0                             
 */

public interface LogServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YSystemconfigurationLog> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 查询单个
	 * @param fid
	 * @return
	 */
	public YSystemconfigurationLog getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YSystemconfigurationLog demo);
	
	/**
	 * 查询数据库日志的总数量
	 * @return
	 */
	public long count();
	
	/**
	 * 查询数据库日志的最大flag
	 * @return
	 */
	public long maxFlag();
}
