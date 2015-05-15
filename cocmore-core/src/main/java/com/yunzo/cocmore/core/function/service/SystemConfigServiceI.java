package com.yunzo.cocmore.core.function.service;

import com.yunzo.cocmore.core.function.model.mysql.SystemConfig;

/**
 * Description: <系统配置service接口>. <br>
 * @date:2015年3月13日 上午11:25:40
 * @author beck
 * @version V1.0
 */
public interface SystemConfigServiceI {
	
	/**
	 * 新增
	 * @param demo
	 */
	public void save(SystemConfig demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(SystemConfig demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(SystemConfig demo);
	
	/**
	 * 根据KEY值查询对象
	 * @param key
	 * @return
	 */
	public SystemConfig getObjByKey(String key);
}
