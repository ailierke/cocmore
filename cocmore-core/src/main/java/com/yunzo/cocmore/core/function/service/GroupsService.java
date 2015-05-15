package com.yunzo.cocmore.core.function.service;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Tree1;
import com.yunzo.cocmore.core.function.vo.GroupTreeVo;
import com.yunzo.cocmore.core.function.vo.ImportGroupsVo;
/**
 * 
 * @author jiangxing
 * 社会团体信息service
 */
public interface GroupsService {
	
	public List<YBasicSocialgroups> getGroupList(String id);
	
	/**
	 * 添加
	 * @param group
	 * @return 
	 */
	public Boolean addGroups(YBasicSocialgroups group);
	/**
	 * 查询列表 
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param groupId 组织id  
	 * @para orgId 组织地址
	 * @param groupName 团体名称
	 */
	public PagingList<YBasicSocialgroups> getAllGroupsPagingList(Integer page,Integer pageSize,String groupId,String orgId,String groupName);

	/**
	 * 修改
	 * @param group
	 * @return
	 */
	public  boolean update(YBasicSocialgroups group);
	/**
	 * 查看单个
	 * @param group
	 * @return
	 */
	public  boolean findGroup(YBasicSocialgroups group);
	/**
	 * 通过hql查询
	 * @param hql
	 * @return
	 */
	public List<YBasicSocialgroups> getByHql(String hql);
	/**
	 * 通过会员id来获取所属商会
	 * @param userId
	 * @return
	 */
	Map<String, Object> getGroupByUserId(String telphoe);
	/**
	 * 获取商会通过省---->市集合----->商会集合
	 * @return
	 */
	List<Map<String, Object>> getAllGroup();

	
	/**
	 * 根据id查询
	 * @param fid
	 * @return
	 */
	public YBasicSocialgroups getById(YBasicSocialgroups groups,String fid);

	public void createTree(List<Tree1> treelist,String orgId,String groupName,String type,String userId,HashSet<String> groupSet);
	/**
	 * 获取名称和logo
	 * @param fid
	 * @return
	 */
	public Map<String,Object> getBygroupId(String fid);
	
	/**
	 * 自主注册
	 * @param group
	 */
	public void addRegisterGroup(YBasicSocialgroups group);
	/**
	 * 修改商会生效失效
	 * @param group
	 * @param status
	 * @return
	 */
	public Map<String, Object> updateStatus(YBasicSocialgroups group, String status);
	/**
	 * 根据名称查询返回id
	 * @return
	 */
	public String findByName(String Name);
/**
 * 查询商会
 * @param infoMap
 * @return
 * @throws IOException 
 * @throws JsonProcessingException 
 */
	public List<Map<String, Object>> getRegionBusinessList(String infoMap) throws JsonProcessingException, IOException;
	/**
	 * 查询是否有子节点，没有返回true
	*有子节点， 全是失效 返回true 否则选择false
	 * @param groupId
	 * @return
	 */

public boolean checkChildrenNode(String groupId);

	/**
	 * 根据id查询社会团体对象
	 * @param groupId
	 * @return
	 */
	public YBasicSocialgroups getById(String groupId);
/**
 * 主要用来查询团体树
 * @param page
 * @param pageSize
 * @param groupId
 * @param orgId
 * @param groupName
 * @return
 */
	List<GroupTreeVo> getAllGroupsPagingListByTree( String groupId, String orgId, String groupName,String typeId);
/**
 * 重写上面的方法
 * @param treelist
 * @param orgId
 * @param groupName
 * @param type
 */
	void createTree1(List<Tree1> treelist, String orgId, String groupName,
			String type, String userId, HashSet<String> groupSet,String typeId);


List<GroupTreeVo> getByHql1(String hql);




/**
 * 导入商会
 * @param importGroupsVO
 * @param rowIndex
 * @param request
 */
void importOrUpdateGroups(ImportGroupsVo importGroupsVO, int rowIndex,
		HttpServletRequest request, File lockFile);


void writeStatus(HttpServletRequest request, String message, File lockFile,
		boolean flag);
/**
 * 查询所有的评论
 * @return
 */

Map<String, Object> getLeastYComent(Integer start, Integer limit);
/**
 * 删除评论
 * @param fid
 * @throws Exception 
 */
public void delYComent(String fid) throws Exception;

Map<String, Object> getBygroupId1(String fid) throws Exception;

List<Map<String, Object>> fuzzyQueryBusinessList(String infoMap)
		throws JsonProcessingException, IOException;




}
