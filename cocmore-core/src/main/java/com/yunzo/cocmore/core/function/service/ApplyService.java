package com.yunzo.cocmore.core.function.service;

import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年12月9日上午10:28:19
 * 入会申请service接口
 */
public interface ApplyService {
	
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public PagingList<YInitiationApply> getAllApplyPagingList(Integer page,Integer pageSize,String groupsId,String name);
	
	/**
	 * 根据ID查询入会申请
	 * @param id
	 * @return
	 */
	public YInitiationApply getById(String id);
	
	/**
	 * 新增入会申请
	 * @param apply
	 */
	public void save(YInitiationApply apply);
	
	/**
	 * 删除入会申请
	 * @param apply
	 */
	public void delete(YInitiationApply apply);
	
	/**
	 * 修改
	 * @param apply
	 */
	public void update(YInitiationApply apply);
	
	/**
	 * 入会申请 通过 or 不通过
	 * @param apply
	 */
	public void update(String fids,int state,String fcompanyNames,String fcompanyPositions,String groupId);
}
