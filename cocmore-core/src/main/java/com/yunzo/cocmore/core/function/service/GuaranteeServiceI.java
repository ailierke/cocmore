package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;

/**
 * @author：jackpeng
 * @date：2014年12月17日上午3:19:42
 * 供应、担保关系service接口
 */
public interface GuaranteeServiceI {
	
	/**
	 * 添加
	 * @param guarantee
	 */
	public void save(YSupplyGroup guarantee);
	/**
	 * 修改
	 * @param guarantee
	 */
	public void update(YSupplyGroup guarantee);
	
	/**
	 * 根据id查询对象
	 * @param fid
	 * @return
	 */
	public YSupplyGroup getById(String fid);
	/**
	 * 根据hql语句查询
	 * @param hql
	 * @return
	 */
	public List<YSupplyGroup> findByHql(String hql);
	
	/**
	 * 修改我的供需认证
	 * @param supplyId
	 * @param list
	 */
	public Map<String, Object> updateByGroup(String supplyId,List<String> list);
}
