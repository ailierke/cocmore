package com.yunzo.cocmore.core.function.service;


import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupscontact;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 
 * @author jiangxing
 * 社会团体联系service
 */
public interface GroupsContactService {
	/**
	 * 添加
	 * @param group
	 * @return 
	 */
	public Boolean addGroupsContact(YBasicSocialgroupscontact GroupsContact);
	/**
	 * 查询列表 
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param groupId 组织id  
	 */
	public PagingList<YBasicSocialgroupscontact> getAllGroupsPagingList(Integer page,Integer pageSize,String groupId);

	/**
	 * 修改
	 * @param group
	 * @return
	 */
	public  boolean updateGroupsContact(YBasicSocialgroupscontact GroupsContact);
	/**
	 * 删除
	 * @param GroupsContact
	 * @return
	 */
	public boolean  deleteGroupsContact(YBasicSocialgroupscontact GroupsContact);
	/**
	 * 查看详情
	 * @param GroupsContact
	 * @return
	 */
	public YBasicSocialgroupscontact findGroupsContact(YBasicSocialgroupscontact GroupsContact);
	
	/**
	 * 根据hql语句查询
	 * @param hql
	 * @return
	 */
	public List<YBasicSocialgroupscontact> findByHql(String hql);
	
	/**
	 * 根据团体查询对象
	 * @param groupId
	 * @return
	 */
	public YBasicSocialgroupscontact findByGroupId(YBasicSocialgroupscontact groupscontact,String groupId);
	
	/**
	 * 根据团体id查询
	 * @param groupId
	 * @return
	 */
	public Map<String,Object> getByGroupId(String groupId);
	
	
}
