package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;



/**
 * Description: <设备信息服务接口>. <br>
 * @date:2014年12月29日 下午12:11:09
 * @author beck
 * @version V1.0
 */
public interface AppdeviceServiceI {
	/**
	 * 新增设备信息
	 * @param demo
	 */
	public void save(YAppdevice demo);
	
	/**
	 * hql查询
	 * @param hql
	 * @return
	 */
	public List<YAppdevice> getByHql(String hql);
}
