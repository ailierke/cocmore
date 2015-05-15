package com.yunzo.cocmore.core.function.service;


import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsactivity;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 
 * @author jiangxing
 * 社会团体活动service
 */
public interface GroupsActivityService {
	
	/**
	 * 根据id查询社会团体活动
	 * @param id
	 * @return
	 */
	public YBasicSocialgroupsactivity getById(String id);
	/**
	 * 修改
	 * @param activity
	 */
	public void update(YBasicSocialgroupsactivity activity);
	
	/**
	 * 添加
	 * @param activity
	 * @return 
	 */
	public Boolean addActivity(YBasicSocialgroupsactivity activity);
	/**
	 * 查询列表
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param groupId 团体的id
	 */
	public PagingList<YBasicSocialgroupsactivity> getAllActivityPagingList(Integer page,Integer pageSize,String groupId,String searchName);
	/**
	 * 修改或者更新
	 * @param activity
	 * @return 
	 */
	public Boolean updateActivity(YBasicSocialgroupsactivity activity);
	/**
	 * 删除
	 * @param activity
	 */
	public void deleteActivity(YBasicSocialgroupsactivity activity);
	/**
	 * 审核   反审核
	 * @param activity
	 */
	public Boolean auditActivity(YBasicSocialgroupsactivity activity);
	/**
	 * 生效  失效
	 * @param activity
	 * @return 
	 */
	public Boolean effectActivity(YBasicSocialgroupsactivity activity);
}
