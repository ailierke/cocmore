package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <会员服务接口>. <br>
 * @date:2014年11月26日 下午1:29:19
 * @author beck
 * @version V1.0                             
 */

public interface MemberServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YBasicMember> findAll(String searchName,Integer start,Integer limit,String groupId);
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YBasicMember getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YBasicMember demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(YBasicMember demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YBasicMember demo);
	/**
	 * 接口上的修改
	 * @param demo
	 */
	public Map<String, Object> updateByAPP(String infoMap,String images);
	
	/**
	 * 修改状态和密码
	 * @param demo
	 */
	public void updateStatus(YBasicMember demo);
	
	/**
	 * 根据sql查询全部
	 * @return
	 */
	public List<YBasicMember> findAllBySql(String sql);
	
	/**
	 * 根据hql查询
	 * @return
	 */
	public List<YBasicMember> findByHql(String hql);
	
	/**
	 * hql查询返回list<String>
	 * @param hql
	 * @return
	 */
	public List<String> getByHql(String hql);
	
	/**
	 * 根据会员ID查询会员职位分配的数据
	 * @return
	 */
	public List<YBasicentriesMemberdistribution> findMDByMemID(String fid);
	
	/**
	 * 登录
	 */
	public  Map<String, Object> login(Map<String, Object> map);
	
	/**
	 * 修改密码
	 * @param fmobilePhone
	 * @param password
	 * @return
	 */
	public boolean updatePassword(String fmobilePhone,String oldPassword,String password);
	
	
	/**
	 * 登陆用户电话号码查询
	 */
	public  YBasicMember getByMobilePhone(String fmobilePhone);
	
	
	/**
	 * 游客注册
	 * @return
	 */
	public Map<String, Object> saveGust();
	
	/**
	 * 批量查询用户隐私设置通讯录列表
	 */
	public List<Map<String,Object>> getContactsPrivacyList(String businessId);
	
	/**
	 * 查询会员个人信息接口
	 */
	public Map<String,Object> getContactsOtherPersonInfo(String userId,String businessId);
	
	/**
	 * 添加
	 * @param member
	 */
	public void memberSave(YBasicMember member);
	
	
	
	/**
	 * 批量更新会员是否隐藏状态
	 * @param fids
	 * @param status
	 */
	public void updateMembersHideStatus(String fids,int status);
	
	/**
	 * app端会员注册
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public Map<String, Object> appRegMember(Map<String, Object> jsonObj) throws NumberFormatException, Exception;
	
	/**
	 * 注册会员申请加入商会
	 * @param jsonObj
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public Map<String, Object> applyBusiness(Map<String, Object> jsonObj) throws NumberFormatException, Exception;
	/**
	 * 判断app端的用户是否是登录状态
	 * @param tel
	 * @return
	 * @throws Exception
	 */

	boolean checkAppUserLogin(String tel, String md5info) throws Exception;
	/**
	 * 根据团体查询
	 * @param groupId
	 * @return
	 */
	public List findByGroupId(String groupId);

	boolean isMember(String telphone);
/**
 * 排序的方法
 * @param searchName
 * @param start
 * @param limit
 * @param groupId
 * @param sort
 * @param dir
 * @return
 */
	public PagingList<YBasicMember> findAll(String searchName, Integer start,
			Integer limit, String groupId, String sort, String dir);
}
