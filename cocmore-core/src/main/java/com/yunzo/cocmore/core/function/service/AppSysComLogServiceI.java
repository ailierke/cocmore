package com.yunzo.cocmore.core.function.service;

import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.AppSystemCommandLog;

/**
 * Description: <手机执行命令记录service接口>. <br>
 * @date:2015年3月12日 下午3:53:37
 * @author beck
 * @version V1.0
 */
public interface AppSysComLogServiceI {
	
	/**
	 * 新增
	 * @param demo
	 */
	public void save(AppSystemCommandLog demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(AppSystemCommandLog demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(AppSystemCommandLog demo);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public AppSystemCommandLog getById(String id);
	
	/**
	 * APP端传递参数过来保存
	 * @param map
	 */
	public void appSave(Map<String, Object> map);
}
