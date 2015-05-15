package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YWallsadvertising;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日下午5:58:35
 * 上墙广告service接口
 */
public interface WallsadvertisingService {
	/**
	 * 根据条件查询上墙广告
	 * @param hql
	 * @return
	 */
	public List<YWallsadvertising> getByHql(String hql);
	
	/**
	 * 查询全部活动广告并分页
	 * @param page
	 * @param pageSize
	 * @param activityId
	 * @return
	 */
	public PagingList<YWallsadvertising> getAllSadvertisingPagingList(Integer page,Integer pageSize,String activityId);
	
	/**
	 * 新增上墙广告
	 * @param supply
	 */
	public void save(YWallsadvertising wallAd);
	
	/**
	 * 删除上墙广告
	 * @param supply
	 */
	public void delete(YWallsadvertising wallAd);
	
	/**
	 * 根据ID查询上墙广告
	 * @param id
	 * @return
	 */
	public YWallsadvertising getById(String id);
}
