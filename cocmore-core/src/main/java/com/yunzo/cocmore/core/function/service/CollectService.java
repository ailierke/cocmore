package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.YComment;

/**
 * @author：jackpeng
 * @date：2014年12月13日下午12:26:24
 * 新闻收藏service
 */
public interface CollectService {
	/**
	 * 根据模块id查询收藏
	 * @return
	 */
	public String getCollectYN(String id,String userPhone);
	
	/**
	 * 新增
	 * @param newsCollect
	 */
	public void save(TNewsCollect newsCollect);
	
	/**
	 * 删除
	 * @param newsCollect
	 */
	public void delete(TNewsCollect newsCollect);
	
	/**
	 * 查询全部
	 * @return
	 */
	public List<TNewsCollect> findAll();
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public TNewsCollect getById(String id);
	
	/**
	 * 按条数hql查询
	 * @param hql
	 * @return
	 */
	public List<TNewsCollect> findByHql(String hql,Integer rowNum);
	
	/**
	 * 按条数hql查询
	 * @param hql
	 * @return
	 */
	public List<TNewsCollect> getByHql(String hql);
	
}
