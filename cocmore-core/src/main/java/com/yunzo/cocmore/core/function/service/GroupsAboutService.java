package com.yunzo.cocmore.core.function.service;


import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 
 * @author jiangxing
 * 社会团体信息service
 */
public interface GroupsAboutService {
	/**
	 * 添加
	 * @param group
	 * @return 
	 */
	public Boolean addGroupsAbout(YBasicSocialgroupsabout groupsabout);
	/**
	 * 查询列表 
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param groupId 组织id  
	 */
	public PagingList<YBasicSocialgroupsabout> getAllGroupsPagingList(Integer page,Integer pageSize,String groupId);

	/**
	 * 修改
	 * @param group
	 * @return
	 */
	public  boolean updateGroupsAbout(YBasicSocialgroupsabout groupsabout);
	/**
	 * 删除
	 * @param groupsabout
	 * @return
	 */
	public boolean  deleteGroupsAbout(YBasicSocialgroupsabout groupsabout);
	/**
	 * 查看详情
	 * @param groupsabout
	 * @return
	 */
	public YBasicSocialgroupsabout findGroupsAbout(YBasicSocialgroupsabout groupsabout);
	
	/**
	 * 根据hql语句查询
	 * @param hql
	 * @return
	 */
	public List<YBasicSocialgroupsabout> findByHql(String hql);
	
	/**
	 * 根据团体查询
	 * @param groupId
	 * @return
	 */
	public List<YBasicSocialgroupsabout> findByGroupId(List<YBasicSocialgroupsabout> groupsabouts,String groupId);
	
	/**
	 * 根据团体id查询
	 * @param groupId
	 * @return
	 */
	public Map<String,Object> getBygroupId(String groupId);
	/**
	 * 根据id查询对象
	 */
	public YBasicSocialgroupsabout getByAboutId(String id);
	
}
