package com.yunzo.cocmore.core.function.service;

import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YSystemAppVersions;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * Description: <APP版本信息接口>. <br>
 * @date:2014年12月13日 下午1:56:22
 * @author beck
 * @version V1.0
 */
public interface AppVersionsServiceI {
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public PagingList<YSystemAppVersions> findAll(String searchName,Integer start,Integer limit);
	
	public void save(YSystemAppVersions demo);
	
	public void update(YSystemAppVersions demo);
	
	public Map<String, Object> getAppVersions(String appVersion,String appChannelNo);
}
