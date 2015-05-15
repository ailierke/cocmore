package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDynamicInfoPush;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;

public interface DynmicandInfoPushService {


	void upatePushInfo(YBasicDynamicInfoPush pushInfo) throws Exception;


	void savePushInfo(YBasicDynamicInfoPush pushInfo) throws Exception;


	void saveConfigurationMessage(YSystemconfigurationMessage message)throws Exception;
	
	/**
	 * 条件查询动态、通知推送记录信息
	 * @param fid
	 * @param mid
	 * @param type
	 * @return
	 */
	public YBasicDynamicInfoPush findDynamicInfoPush(String fid,String mid);
	
	
	

	/**
	 * 未读消息
	 * @return
	 */
	public int unreadDynmicandMsg(String tel);
	
	/**
	 * 根据商会ID返回动态通知的未读消息数量
	 * @return
	 */
	public int unreadDynmicandMsgByGroupId(String tel,String groupId);
	boolean findRead(String fid, String tel) throws Exception;




	/**
	 * 修改动态、通知推送记录信息
	 * @param dynamicInfoPush
	 */
	void update(YBasicDynamicInfoPush dynamicInfoPush);



	List<YAppdevice> push(String fmobilePhone) throws Exception;

}
