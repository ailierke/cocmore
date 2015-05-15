package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YCompanyproduct;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <会员公司服务接口>. <br>
 * @date:2014年11月26日 下午3:00:37
 * @author beck
 * @version V1.0                             
 */

public interface MembercompanyServiceI {
	
	/**
	 * 查询全部公司并分页
	 * @param page
	 * @param pageSize
	 * @param TradeId
	 * @param industryId
	 * @return
	 */
	public PagingList<YBasicMembercompany> getCompanyPagingList(Integer page,Integer pageSize,String groupId,String tradeId,String industryId);
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YBasicMembercompany getById(String fid);
	
	/**
	 * 根据行业、产业查询
	 * @return
	 */ 
	public List<YBasicMembercompany> findAllByIndAndTrade(String hql);
	
	/**
	 * 添加公司
	 */
	public void save(YBasicMembercompany company);
	/**
	 * 
	 * App端添加公司信息
	 */
	public void saveByApp(YBasicMembercompany company);
	
	/**
	 * 修改公司
	 * @param company
	 */
	public void update(YBasicMembercompany company);
	
	/**
	 * App端修改公司信息
	 */
	public void updateByApp(YBasicMembercompany company);
	
	/**
	 * 根据会员ID查询
	 * @return
	 */
	public List<Map<String, Object>> findAllByUserId(String phone,String businessId);
	
	/**
	 * 根据会员ID查询
	 * @param fid
	 * @return
	 */
	public YBasicMembercompany getByMemberId(String fid);
	
	public List<YCompanyproduct> findProductByComID(String fid);
	
	public YCompanyproduct getProductById(String fid);
	
	
}
