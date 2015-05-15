package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;

/**
 * @author：jackpeng
 * @date：2014年12月16日上午9:54:17
 * 标签接口
 */
public interface LabelServiceI {
	
	/**
	 * 更具用户名查询标签
	 * @param userName
	 * @return
	 */
	public int getLabelNum(String mid);
	
	/**
	 * 新增标签
	 * @param label
	 */
	public void save(YBasicLabel label);
	
	/**
	 * 删除标签
	 * @param label
	 */
	public void delete(YBasicLabel label);
	
	/**
	 * hql查询标签返回对象
	 * @param hql
	 * @return
	 */
	public List<YBasicLabel> getLabelHql(String hql);
	
	/**
	 * hql查询标签返回字符串
	 * @param hql
	 * @return
	 */
	public List<String> getByHql(String hql);
	
	public void saveOrUpdateList(List<YBasicLabel> list);
}
