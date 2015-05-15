package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * 评论接口
 * @author yunzo
 *
 */
public interface CommentService {
	
	/**
	 * 查询评论分页（接口）
	 * @param commentID
	 * @param fId
	 * @param pageSize
	 * @param mid
	 * @return
	 */
	public List<Map<String, Object>> getByHql(String commentID,String fId,int pageSize);
	
	/**
	 * 根据模块id查询评论总条数
	 * @return
	 */
	public String getCountNum(String id);
	
	/**
	 * 新增评论
	 * @param comment
	 */
	public void save(YComment comment);
	/**
	 * 删除评论
	 * @param comment
	 */
	public void delete(YComment comment);
	/**
	 * 修改评论
	 * @param comment
	 */
	public void update(YComment comment);
	/**
	 * 查询所有的评论
	 * @return
	 */
	public List<YComment> findAll();
	/**
	 * 根据hql语句查询
	 * @param hql
	 * @return
	 */
	public List<YComment> findByHql(String hql);
	
	/**
	 * 根据新闻id查询并分页
	 * @param ftid
	 * @param start
	 * @param limit
	 * @return
	 */
	public PagingList<YComment>  getAllDynamicPagingList(String ftid, Integer start,Integer limit);
	
	/**
	 * 根据id查询评论
	 * @param id
	 * @return
	 */
	public YComment getById(String id);
	/**
	 * hql查询
	 * @param hql
	 * @return
	 */
	public List<YComment> findByHql(String hql,Integer rowNum);
	
	/**
	 * 评论供需
	 */
	public Map<String, Object> savesupplyDemand(String memberId,String fContent,Integer supplyDemandType,String fId,String businessId);
	
	/**
	 * 删除供需评论
	 * @param memberId
	 * @param supplyDemandType
	 * @param fId
	 * @return
	 */
	public boolean delsupplyDemand(String commentID);
	
}
