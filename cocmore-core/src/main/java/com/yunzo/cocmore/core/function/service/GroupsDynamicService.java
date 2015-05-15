package com.yunzo.cocmore.core.function.service;


import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 
 * @author jiangxing
 * 社会团体动态service
 */
public interface GroupsDynamicService {
	/**
	 * 添加
	 * @param dynamic
	 * @return 
	 */
	public Boolean addDynamic(YBasicSocialgroupsdynamic dynamic);
	
	/**
	 * 根据对象查询
	 * @param fid
	 * @return
	 */
	public YBasicSocialgroupsdynamic getById(String fid);
	/**
	 * 修改
	 * @param dynamic
	 */
	public void update(YBasicSocialgroupsdynamic dynamic);
	
	/**
	 * 查询列表
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param dynamic
	 */
	public PagingList<YBasicSocialgroupsdynamic> getAllDynamicPagingList(Integer page,Integer pageSize,String groupId,String fheadline);
	/**
	 * 修改或者更新
	 * @param dynamic
	 * @return 
	 */
	public Boolean updateDynamic(YBasicSocialgroupsdynamic dynamic);
	/**
	 * 删除
	 * @param dynamic
	 */
	public void deleteDynamic(YBasicSocialgroupsdynamic dynamic);
	/**
	 * 审核   反审核
	 * @param dynamic
	 * @throws UnsupportedEncodingException 
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public Boolean auditDynamic(YBasicSocialgroupsdynamic dynamic) throws UnsupportedEncodingException, NumberFormatException, Exception;
	/**
	 * 生效  失效
	 * @param dynamic
	 * @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public Boolean effectDynamic(YBasicSocialgroupsdynamic dynamic) throws NumberFormatException, Exception;
	


	/**
	 * 动态点赞和取消点赞
	 * @param fid
	 * @param userId
	 * @param type
	 * @return
	 */
	boolean pointLike(String fid, String userId,String businessId, int type);
	/**
	 * 获取动态评论的回复列表（分页）
	 * @param dynamicId
	 * @param reviewId
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> getDynamicReplyList(String dynamicId,
			String reviewId, int pageSize);
	/**
	 * 删除动态的评论 按照id
	 * @param reviewId
	 * @return
	 */
	public boolean delDynamicReview(String reviewId);
	/**
	 * 添加评论
	 * @param userId
	 * @param dynamicId
	 * @param reviewContent
	 */
	public void addDynamicReview(String tel,String businessId, String dynamicId,
			String reviewContent);
	/**
	 * 查询单个
	 * @param fid
	 * @return 
	 */
	public YBasicSocialgroupsdynamic getDynamic(String fid);
	/**
	 * 通过id获取详细动态信息
	 * @param fid
	 * @param tel 判断是否是游客
	 * @return
	 */
	Map<String, Object> getDynamicById(String fid,String tel,String businessId);
/**
 * 排序运维按点赞数、评论数排序  type ：0 点赞数  1评论数
 * @param start
 * @param limit
 * @param type
 * @return
 */
	public PagingList<YBasicSocialgroupsdynamic> getAllDynamicPagingList(
			Integer start, Integer limit, Integer type);
	/**
	 * 通过dynamicID或者当前用户id查询pageSize或者当前
	 * @param fid
	 * @param pageSize
	 * @param userId
	 * @return
	 */
List<Map<String, Object>> getDynamicList(String fid, int pageSize,
		String businessId, String tel, HttpServletRequest request);

}
