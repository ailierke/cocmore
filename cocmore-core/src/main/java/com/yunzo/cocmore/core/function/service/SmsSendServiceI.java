package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.FSmsSendrecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <短信发送服务接口>. <br>
 * @date:2014年11月26日 下午3:50:34
 * @author beck
 * @version V1.0                             
 */

public interface SmsSendServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public List<FSmsSendrecord> findAll();
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public FSmsSendrecord getById(String fid);
	
	/**
	 * 根据手机号、姓名、状态进行查询
	 * @return
	 */
	public List<FSmsSendrecord> findAllByPhoneNameStatus(String hql);
	
	/**
	 * 
	 * @Title: getAllControllerPagingList 
	 * @Description: TODO 分页查询
	 * @param @param start
	 * @param @param limit
	 * @param @param groupId
	 * @param @param headline
	 * @param @return    
	 * @return PagingList<FSmsSendrecord>   
	 * @throws
	 */
	public PagingList<FSmsSendrecord> getAllControllerPagingList(Integer start,Integer limit,String groupId,String headline);
	
	/**
	 * 发送短信
	 * @param mobiles
	 * @param msgText
	 * @return
	 */
	public boolean send(String fids,String msgText,String dateString);
	
	
}
