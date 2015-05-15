package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;

/**
 * Description: <会员职位分配接口>. <br>
 * @date:2014年12月16日 上午11:20:02
 * @author beck
 * @version V1.0
 */
public interface MemberdistributionServiceI {
	/**
	 * 获取当前最大版本号
	 */
	public int getMaxVersion(String businessId);
	
	/**
	 * 根据职位查找会员
	 * @param fid
	 * @return
	 */
	public List<YBasicentriesMemberdistribution> findPosByEdID(String fid);
	/**
	 * 新增
	 * @param obj
	 */
	public void save(YBasicentriesMemberdistribution obj);
}
