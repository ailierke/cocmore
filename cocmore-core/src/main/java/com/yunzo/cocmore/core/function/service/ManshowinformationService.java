package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.model.mysql.YImage;
import com.yunzo.cocmore.core.function.model.mysql.YManshowinformation;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * 个人秀接口
 * @author yunzo
 *
 */
public interface ManshowinformationService {
	
	/**
	 * 新增个人秀信息
	 * @param manshow
	 */
	public void save(YManshowinformation manshow);
	/**
	 * 修改个人秀信息
	 * @param manshow
	 */
	public void update(YManshowinformation manshow);
	/**
	 * 删除个人秀信息
	 * @param manshow
	 */
	public void delete(YManshowinformation manshow);
	
	/**
	 * 新增个人秀图片
	 * @param image
	 */
	public void saveImage(YImage image);
	/**
	 * 修改个人秀图片
	 * @param image
	 */
	public void updateImage(YImage image);
	/**
	 * 根据个人秀id查询个人秀图片
	 * @param showId
	 * @return
	 */
	public List<YImage> getByshowId(String showId);
	/**
	 * 根据id查询个人秀
	 * @param fid
	 * @return
	 */
	public YManshowinformation getById(String fid);
	
	/**
	 * 获取我的个人秀列表
	 * @param memberId
	 * @param Size
	 * @param fid
	 * @return
	 */
	public List<Map<String,Object>> findMyManList(String memberId,Integer Size,String fid);
	
	/**
	 * 获取个人秀列表
	 * @param memberId
	 * @param Size
	 * @param fid
	 * @return
	 */
	public List<Map<String,Object>> findManList(String memberId,Integer Size,String fid);
	
	
	/**
	 * 根据组织或团体查询所有的个人秀列表
	 * @param page
	 * @param pageSize
	 * @param orgId
	 * @param groupId
	 * @param searchName
	 * @return
	 */
	public List<YManshowinformation> getAllDynamicPagingList(Integer page,Integer pageSize,String groupId,String memberId);
	
	/**
	 * 根据个人秀id和会员id 查询 点赞记录
	 * @param memberId
	 * @param showId
	 * @return
	 */
	public List<YPointlike> findByPoint(String memberId,String showId);
	
	/**
	 * 个人秀点赞
	 * @param pointlike
	 */
	public void savePointlike(YPointlike pointlike);
	/**
	 * 个人秀取消点赞
	 * @param pointlike
	 */
	public void updatePointlike(YPointlike pointlike);
	
	/**
	 * 获取个人秀评论列表
	 * @param reviewID
	 * @param fid
	 * @param pageSize
	 * @return
	 */
	public List<Map<String,Object>> findByComment(String reviewID,String fid,Integer pageSize);
	
	/**
	 * 点赞数
	 */
	public Integer getNumByshowId(String showId);
}
