package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <行业服务接口>. <br>
 * @date:2014年11月25日 下午5:18:23
 * @author beck
 * @version V1.0                             
 */

public interface TradeServiceI {
	
	/**
	 * 查询全部并分页
	 * @return
	 */
	public PagingList<YBasicTrade> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 获取行业列表
	 * @return
	 */
	public List<Map<String, Object>> getTradeList();
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YBasicTrade getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YBasicTrade demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(YBasicTrade demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YBasicTrade demo);
	
	/**
	 * 根据hql语句 查询
	 * @param hql
	 * @return
	 */
	public List<YBasicTrade> findByHql(String hql);
	
	
}
