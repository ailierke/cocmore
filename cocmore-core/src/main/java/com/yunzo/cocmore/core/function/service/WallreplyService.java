package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YWallreply;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月27日下午4:33:16
 * 上墙回复service接口
 */
public interface WallreplyService {
	
	/**
	 * 根据活动id查询上墙回复并分页
	 * @param page
	 * @param pageSize
	 * @param activityId
	 * @param state
	 * @return
	 */
	public PagingList<YWallreply> getAllReplyPagingList(Integer page,Integer pageSize,String activityId,Integer state);
	
	/**
	 * 根据ID查询上墙回复
	 * @param id
	 * @return
	 */
	public YWallreply getById(String id);
	
	/**
	 * 根据条件查询上墙回复
	 * @param hql
	 * @return
	 */
	public List<YWallreply> getByHql(String hql);
	
	/**
	 * 删除上墙回复
	 * @param supply
	 */
	public void delete(YWallreply wallReply);
	
	/**
	 * 修改上墙回复
	 * @param supply
	 */
	public void update(YWallreply wallReply);
	
	/**
	 * 新增上墙回复
	 * @param wallReply
	 */
	public void save(YWallreply wallReply);
}
