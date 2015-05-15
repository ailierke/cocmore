package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.AppSystemCommandType;

/**
 * Description: <手机执行命令类型service接口>. <br>
 * @date:2015年3月12日 下午3:52:47
 * @author beck
 * @version V1.0
 */
public interface AppSysComTypeServiceI {
	/**
	 * 新增
	 * @param demo
	 */
	public void save(AppSystemCommandType demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(AppSystemCommandType demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(AppSystemCommandType demo);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public AppSystemCommandType getById(String id);
	
	/**
	 * 查询执行代码
	 * @return
	 */
	public Map<String, Object> selectAll(Map<String, Object> map);
}
