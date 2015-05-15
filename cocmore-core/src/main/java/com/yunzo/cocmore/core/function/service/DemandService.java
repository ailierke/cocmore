package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.util.PagingList;


/**
 * @author：jackpeng
 * @date：2014年11月25日上午9:17:32
 * 需求service接口
 */
public interface DemandService {
	
	/**
	 * 查看指定用户的供需列表
	 * @param fId
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getMyDemandList(String fId,Integer pageSize, String userId);
	
	/**
	 * 获取需求列表
	 * @param fId
	 * @param contentType
	 * @param pageSize
	 * @param tradeId
	 * @param provincialId
	 * @param cityId
	 * @param supplyDemandType
	 * @return
	 */
	public List<Map<String, Object>> getDemandList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,String provincialId,
			String cityId,int supplyDemandType,String mid);
	
	/**
	 * 查询全部需求
	 * @return
	 */
	public List<YBasicSocialgroupsdemand> findAll();
	
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param groupId
	 * @param headline
	 * @return
	 */
	public PagingList<YBasicSocialgroupsdemand> getAllDemandPagingList(Integer page,Integer pageSize,String groupId,String headline);
	
	/**
	 * 获取推荐的供需列表(广告栏位置)
	 * @return
	 */
	public List<Map<String, Object>> findSupplyAndDemandList();
	
	/**
	 * 根据ID查询需求
	 * @param id
	 * @return
	 */
	public YBasicSocialgroupsdemand getById(String id);
	
	/**
	 * 新增需求
	 * @param demand
	 */
	public void save(YBasicSocialgroupsdemand demand);
	
	/**
	 * 删除需求
	 * @param demand
	 */
	public void delete(YBasicSocialgroupsdemand demand);
	
	/**
	 * 修改需求
	 * @param demand
	 */
	public void update(YBasicSocialgroupsdemand demand);
	
	/**
	 * 根据条件查询需求
	 * @param hql
	 * @return
	 */
	public List<YBasicSocialgroupsdemand> getByHql(String hql);
	
	/**
	 * 查询我的 的需求
	 * @param memberId
	 * @param Size
	 * @param fid
	 * @return
	 */
	public List<Map<String, Object>> findByMyList(List<YBasicMember> memberlist,Integer Size,String fid);
	
	/**
	 * 投诉需求
	 * @param fId
	 * @param memberId
	 */
	public void saveComplaint(String fId,String memberId,String businessId);
	/**
	 * 查看详情
	 * @param fid
	 * @return
	 */


	Map<String, Object> getDemandInfo(String fid, String userId,String businessId);
	/**
	 * 点赞
	 * @param fid
	 * @param userId
	 * @param isSupport
	 * @return
	 */

	public void saveDemandForSupport(String fid, String userId,int isSupport,String businessId);
	
	/**
	 * 根据评论数排序查询所有的需求
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PagingList<YBasicSocialgroupsdemand> findDemandByComment(Integer page,Integer pageSize);
	/**
	 * 根据点赞数排序查询所有的需求
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PagingList<YBasicSocialgroupsdemand> findDemandByPointLike(Integer page,Integer pageSize);
	
	/**
	 * 根据时间排序查询所有的需求
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PagingList<YBasicSocialgroupsdemand> findDemandBytime(Integer page,Integer pageSize);
	
}
