package com.yunzo.cocmore.core.function.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinformrecord;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.vo.PushVo;
/**
 * 
 * @author jiangxing
 * 社会团体通知service
 */
public interface GroupsInformService {
	/**
	 * 添加
	 * @param inform
	 * @param memberIds 
	 * @return 
	 * @throws Exception 
	 */
	void addInform(YBasicSocialgroupsinform inform, String[] memberIds) throws Exception;
	
	/**
	 * 根据id查询对象
	 * @param fid
	 * @return
	 */
	public YBasicSocialgroupsinform getById(String fid);
	
	/**
	 * 修改
	 * @param inform
	 */
	public void update(YBasicSocialgroupsinform inform);
	
	/**
	 * 查询列表
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param groupId 团体的id
	 */
	public PagingList<YBasicSocialgroupsinform> getAllInformPagingList(Integer page,Integer pageSize,String groupId,String fheadline);


	/**
	 * 删除
	 * @param inform
	 */
	public void deleteInform(YBasicSocialgroupsinform inform);
	/**
	 * 审核   反审核
	 * @param inform
	 */
	public Boolean auditInform(YBasicSocialgroupsinform inform);
	/**
	 * 生效  失效
	 * @param inform
	 * @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void effectInform(YBasicSocialgroupsinform inform) throws NumberFormatException, Exception;
	
	/**
	 * 通过id获取详情信息
	 */
	Map<String, Object> getInformById(String fid, String tel,String businessId);
	
	/**
	 * 点赞
	 * @param fid
	 * @param userId
	 * @param type 
	 * @return
	 */
	boolean pointLike(String fid, String tel,String businessId , int type);
	/**
	 * 用户是否参加通知
	 * @param fid
	 * @param userId
	 * @param isJoin
	 * @return
	 */
	public boolean saveInformJoin(String fid, String tel, String businessId,String isJoin);
	/**
	 * 获取动态评论的回复列表（分页）
	 * @param noticeId
	 * @param reviewId
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getInformReplyList(String noticeId,
			String reviewId, int pageSize);
	/**
	 * 删除评论通过评论Id
	 * @param reviewId
	 * @return
	 */
	public boolean delInformReview(String reviewId);
	/**
	 * 添加评论
	 * @param userId
	 * @param noticeId
	 * @param reviewContent
	 */
	public void addInformReview(String tel,String businessId, String noticeId,
			String reviewContent);
	/**
	 * 查看所有与回复数
	 * @param inform
	 * @return
	 */
	List<YBasicSocialgroupsinformrecord> getGroupsinformRecords(
			YBasicSocialgroupsinform inform);
	/**
	 * 查看单个
	 * @param fid
	 * @return
	 */
	YBasicSocialgroupsinform findInform(String fid);
	void deleteInformRecord(YBasicSocialgroupsinform inform);
	/**
	 * 修改隐藏字段为隐藏
	 * @param fid
	 */
	public void updateBycordId(String fid,String type);
/**
 * 获取点赞数和不点赞数
 * @param informId
 * @return
 */
	ArrayList<Integer> findPointLikeAndNotLike(String informId);


/**
 * 运维按点赞数、评论数排序  type ：0 点赞数  1排序数
 * @param page
 * @param pageSize
 * @param type
 * @return
 */
	PagingList<YBasicSocialgroupsinform> getAllInformPagingList(Integer page,
			Integer pageSize,Integer type);

Map<String, PushVo> getOutRepeat(Set<String> tels);
/**
 * 修改或者更新
 * @param inform
 * @return 
 */
void updateInform(YBasicSocialgroupsinform inform, String[] memberIds,
		HttpServletRequest request);
/**
 * 获取商会通知
 * @param fid
 * @param pageSize
 * @param businessId 
 * @return
 */
List<Map<String, Object>> getInformList(String fid, int pageSize,
		String businessId, String tel, HttpServletRequest request);
	
}
