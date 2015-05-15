package com.yunzo.cocmore.core.function.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesEmployeedistribution;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.vo.GuaranteeVo;

/**
 * @author：jackpeng
 * @date：2014年11月24日下午12:13:51
 * 供应Service接口
 */
public interface SupplyService {
	
	/**
	 * 查看指定用户的供需列表
	 * @param fId
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getMySupplyList(String fId,Integer pageSize,String userId);
	
	/**
	 * 查询全部供应
	 * @return
	 */
	public List<YBasicSocialgroupssupply> findAll();
	
	/**
	 * 条件查询供应并分页
	 * @param fId
	 * @param contentType
	 * @param pageSize
	 * @param tradeId
	 * @param provincialId
	 * @param cityId
	 * @param countryId
	 * @return
	 */
	public List<Map<String, Object>> getSupplyList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,String provincialId,
			String cityId,int supplyDemandType,String mid);
	
	/**
	 * 同时获取供应和需求
	 * @param fId
	 * @param contentType
	 * @param pageSize
	 * @param tradeId
	 * @param provincialId
	 * @param cityId
	 * @param countryId
	 * @return
	 */
	public List<Map<String, Object>> getSupAndDemList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,String provincialId,
			String cityId,String mid);
	
	/**
	 * 推荐供需
	 * @param fId
	 * @param contentType
	 * @param pageSize
	 * @param tradeId
	 * @param provincialId
	 * @param cityId
	 * @param countryId
	 * @return
	 */
	public List<Map<String, Object>> getRecommendationList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,String provincialId,
			String cityId,String mid);
	
	/**
	 * 获取推荐的供需列表(广告栏位置)
	 * @return
	 */
	public List<Map<String, Object>> findSupplyAndDemandList();
	
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param groupId
	 * @param headline
	 * @return
	 */
	public PagingList<YBasicSocialgroupssupply> getAllSupplyPagingList(Integer page,Integer pageSize,String groupId,String headline);
	
	/**
	 * 根据供应id查询供应担保分配信息
	 * @return
	 */
	public List<YSupplyGroup> findSASupplyId(String id);
	
	/**
	 * 根据ID查询供应
	 * @param id
	 * @return
	 */
	public YBasicSocialgroupssupply getById(String id);
	
	/**
	 * 新增供应
	 * @param supply
	 */
	public void save(YBasicSocialgroupssupply supply);
	
	/**
	 * 新增供应和担保分录
	 * @param supply
	 */
	public void saveSupply(YBasicSocialgroupssupply supply);
	
	/**
	 * 修改供应和担保分录
	 * @param supply
	 */
	public void updateSupply(YBasicSocialgroupssupply supply);
	
	/**
	 * 删除供应
	 * @param supply
	 */
	public void delete(YBasicSocialgroupssupply supply);
	
	/**
	 * 修改供应
	 * @param supply
	 */
	public void update(YBasicSocialgroupssupply supply);
	
	/**
	 * 条件查询供应
	 * @param hql
	 * @return
	 */
	public List<YBasicSocialgroupssupply> getByHql(String hql);
	
	/**
	 * 查询我的 供应
	 * @param memberId
	 * @param Size
	 * @param fid
	 * @return
	 */
	public List<Map<String, Object>> findByMyList(List<YBasicMember> memberlist,Integer Size,String fid);
	
	/**
	 * 添加投诉供应
	 * @param fId
	 * @param memberId
	 */
	public void saveComplaint(String fId,String memberId,String businessId);
/**
 * 查看详情
 * @param fid
 * @param userId 
 * @return
 */
	public Map<String, Object> getSupplyInfo(String fid, String userId,String businessId);
/**
 * 点赞
 * @param fid
 * @param userId
 * @param isSupport
 * @return
 */
public void saveSupplyForSupport(String fid, String userId,int isSupport,String businessId);
/**
 * 通过用户获取自己的商会和担保信息
 * @param telphone
 * @return
 */

public List<Map<String, Object>> getBusinessListAndEnsureType(String telphone);
/**
 * 获取担保表信息
 * 
 */
public YBasicAssurancecontent getYBasicAssurancecontentById(String id);
/**
 * 获取担保信息VO通过groupId
 * @param groupId (要确定担保信息的商会)
 * @return
 */
List<GuaranteeVo> getYBasicAssurancecontentVo(String start,String limit,String groupId, String ispass);
/**
 * 对自己需要做担保的进行通过或者拒绝
 * @param supplygroupId
 * @throws UnsupportedEncodingException 
 * @throws Exception 
 * @throws NumberFormatException 
 */

void updateYBasicAssurancecontent(String supplygroupId, String ispass) throws UnsupportedEncodingException, NumberFormatException, Exception;

Integer getCount(String start, String limit, String groupId, String ispass);
/**
 * 获取点赞和踩的数量  
 * @param supplyDemandId
 * @return
 */
public Map<String ,Object> getNumById(String supplyDemandId);
/**
 * supply 和demand 评分
 * @return这里写在一起
 */
public void commentScore(String fId,Integer supplyDemandType,Integer gradeNum,String mid);

public List<Map<String, Object>> getsupplyAnddemandByType(String userId, Integer pageSize,
		String searchName, Integer supplyDemandType,String fid);
/**
 * 根据评论排序查询供应
 * @param start
 * @param limit
 * @return
 */
public PagingList<YBasicSocialgroupssupply> findSupplyByComment(Integer start,Integer limit);
/**
 * 根据点赞排序查询供应
 * @param start
 * @param limit
 * @return
 */
public PagingList<YBasicSocialgroupssupply> findSupplyByPointLike(Integer start,Integer limit);

/**
 * 根据时间排序查询供应
 * @param start
 * @param limit
 * @return
 */
public PagingList<YBasicSocialgroupssupply> findSupplyBytime(Integer start,Integer limit);
/**
 * 供需点赞
 * @param pointlike
 */
public void savepointlike(YPointlike pointlike);
}
