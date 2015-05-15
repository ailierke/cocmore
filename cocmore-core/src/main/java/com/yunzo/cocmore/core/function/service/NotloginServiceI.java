package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YVisitorsRecordNotlogin;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <未登陆用户行为服务接口>. <br>
 * @date:2014年12月5日 下午5:17:20
 * @author beck
 * @version V1.0                             
 */

public interface NotloginServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YVisitorsRecordNotlogin> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 根据会员ID查询
	 * @param fid
	 * @return
	 */
	public List<YVisitorsRecordNotlogin> getById(String memberFid);
	
	/**
	 * 根据会员IDS查询会员及该会员的活跃度
	 * @param memberFids
	 * @return
	 */
	public List getMemberAndActiveByIds(String memberFids,int flag);
}
