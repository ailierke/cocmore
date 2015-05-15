package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <职位服务接口>. <br>
 * @date:2014年11月25日 下午4:32:13
 * @author beck
 * @version V1.0                             
 */

public interface PositionServiceI {
	
	/**
	 * 查询全部并页面
	 * searchName不为空时，模糊查询分页
	 * @param searchName
	 * @param start
	 * @param limit
	 * @param groupId
	 * @param org
	 * @return
	 */
	public PagingList<YBasicPosition> findAll(String searchName,Integer start,Integer limit,String groupId,String orgId);
	
	/**
	 * 根据主键ID查询
	 * @param fid
	 * @return
	 */
	public YBasicPosition getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YBasicPosition demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(YBasicPosition demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YBasicPosition demo);
	
	/**
	 * 查询上级职位，排除本身职位
	 * @param fid
	 * @return
	 */
	public List<YBasicPosition> findSuper(String selfFid,String groupId,String orgId);
	
	/**
	 * 
	 * @Title: findByHql 
	 * @Description: TODO 根据hql语句查询
	 * @param @param hql  
	 * @param @return    
	 * @return List<YBasicPosition>   
	 * @throws
	 */
	public List<YBasicPosition> findByHql(String hql);
	
	
	/**
	 * 根据团体ID和版本号查询职员职位是否有更新
	 * @param businessId
	 * @param CacheVersion
	 * @return
	 */
	public int getPositionByVersion(String businessId,int CacheVersion);
	
	/**
	 * 根据团体ID和版本号查询职位是否有更新
	 * @param businessId
	 * @param CacheVersion
	 * @return
	 */
	public int getPositionByPositionVersion(String businessId,int CacheVersion);
	
	/**
	 * 获取通讯录会员
	 * @param businessId
	 * @param CacheVersion
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> getPositionByVersionAndPageSize(String businessId,int CacheVersion,int pageSize);
	
	/**
	 * 获取通讯录职位
	 * @param businessId
	 * @param CacheVersion
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getPositionInfo(String businessId,int CacheVersion);
}
