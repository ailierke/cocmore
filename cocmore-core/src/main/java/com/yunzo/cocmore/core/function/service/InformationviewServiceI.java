package com.yunzo.cocmore.core.function.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.Informationview;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;

public interface InformationviewServiceI {
	
	public List<Informationview> getByHql(String hql);
	
	/**
	 * 获取通知信息
	 * @param memberId
	 * @return
	 */
	public List<Map<String, Object>> findByMemberList(List<YBasicMember> memberlist,String messageId,int pageSize);
	
	/**
	 * 根据id查询对象
	 * @param fid
	 * @return
	 */
	public Informationview getById(String fid);
	
	/**
	 * 根据用户查询未读消息通知
	 * @param memberId
	 * @return
	 */
	public List<Informationview> getListByUserId(List<YBasicMember> memberlist);
	
	/**
	 * 根据用户电话查询未读消息通知
	 * @param memberId
	 * @return
	 */
	public int getListByUserTel(String tel);
	
	/**
	 * 获取消息通知
	 * @param tel
	 * @return
	 */
	public List<Informationview> findInformationviewPhone(String tel);
	
	/**
	 * 修改消息通知
	 * @param informationview
	 */
	public void update(Informationview informationview);
}
