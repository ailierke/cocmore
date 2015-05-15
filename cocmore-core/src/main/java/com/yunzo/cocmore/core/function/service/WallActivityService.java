package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日下午1:48:38
 * 上墙活动service接口
 */
public interface WallActivityService {
	
	/**
	 * 查询全部并分页
	 * @param page
	 * @param pageSize
	 * @param groupId
	 * @param headline
	 * @return
	 */
	public PagingList<YWallactivity> getAllActivityPagingList(Integer page,Integer pageSize,String groupId,String theme);
	
	/**
	 * 查询全部上墙活动
	 * @return
	 */
	public List<YWallactivity> findAll();
	
	/**
	 * 根据ID查询上墙活动
	 * @param id
	 * @return
	 */
	public YWallactivity getById(String id);
	
	/**
	 * 新增上墙活动
	 * @param wallActivity
	 */
	public void save(YWallactivity wallActivity);
	
	/**
	 * 删除上墙活动
	 * @param wallActivity
	 */
	public void delete(YWallactivity wallActivity);
	
	/**
	 * 修改上墙活动
	 * @param wallActivity
	 */
	public void update(YWallactivity wallActivity);
	
	/**
	 * 根据条件查询上墙活动
	 * @param hql
	 * @return
	 */
	public List<YWallactivity> getByHql(String hql);
	/**
	 * 保存单个参加活动上墙的人（可手动导入）
	 * @param yBasicJoinActivity
	 */

	public void saveJoinActivity(YBasicJoinActivity yBasicJoinActivity);
	/**
	 * 删除多个ids参加活动人
	 * @param ids
	*/
	public void deleteJoinActivity(String ids);
	/**
	 * 修改参加活动上墙记录
	 * @param yBasicJoinActivity
	 */

	public void updateJoinActivity(YBasicJoinActivity yBasicJoinActivity);
	/**
	 * 根据excel文件来导入读取
	 * @param file
	 * @param activityId
	 * @throws Exception 
	*/
	public void importJoinActivity(MultipartFile file, String activityId) throws Exception;
	/**
	 * 
	 * @param start 开始条数
	 * @param limit 每页大小
	 * @param searchCondition 参加人名称
	 * @param ztid 上墙主题活动id
	 * @param groupId 团体 id
	 * @return
	 */
	public PagingList<YBasicJoinActivity> getAllGroupsPagingList(Integer start,Integer limit,String searchCondition,String ztid);
	/**
	 * 填写随机座位号的范围，改变签到人员的座位号
	 * @param startNum起始值
	 * @param endNum结束值
	 * @param ztid活动上墙主题id
	*/
	public void changeSeatNumberForJionWallActivitypeople(Integer startNum,
			Integer endNum, String ztid);
	/**
	 * 为抽奖查询奖项和参与人（去掉已经中奖的人）
	 * @param ztid
	 * @return
	*/
	public Map<String, Object> findAllJionWallActivitypeople(String ztid);

	/**
	 * 同时满足一下条件 查询座位号
	 * @param userName
	 * @param tel
	 * @param groupname
	 * @param ztid
	 * @return
	*/
	public Integer getWallActivitypeopleSeatNum(String userName,
			String tel, String groupname, String ztid);
	/**
	 * 增加中奖人信息
	 * @param ztid
	 * @param settingId
	 * @param tel
	 * @return
	*/
	public void saveWinActivitypeople(String ztid, String[] settingId,
			String tel);
	}
