package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationParameter;

/** 
 *Description: <系统参数服务接口>. <br>
 * @date:2014年11月27日 下午4:11:34
 * @author beck
 * @version V1.0                             
 */

public interface ParameterServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public List<YSystemconfigurationParameter> findAll();
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YSystemconfigurationParameter getById(String fid);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YSystemconfigurationParameter demo);
}
