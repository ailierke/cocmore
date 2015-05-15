package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.FAwardSetting;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日上午10:04:29
 * 抽奖设置service接口
 */
public interface SettingService {
	/**
	 * 查询全部抽奖设置
	 * @return
	 */
	public List<FAwardSetting> findAll();
	
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param activityName
	 * @return
	 */
	public PagingList<FAwardSetting> getAllSettingPagingList(Integer page,Integer pageSize,String activityName,String groupId);
	
	/**
	 * 根据ID查询抽奖设置
	 * @param id
	 * @return
	 */
	public FAwardSetting getById(String id);
	
	/**
	 * 新增抽奖设置
	 * @param setting
	 */
	public void save(FAwardSetting setting);
	
	/**
	 * 删除抽奖设置
	 * @param setting
	 */
	public void delete(FAwardSetting setting);
	
	/**
	 * 修改抽奖设置
	 * @param setting
	 */
	public void update(FAwardSetting setting);
}
