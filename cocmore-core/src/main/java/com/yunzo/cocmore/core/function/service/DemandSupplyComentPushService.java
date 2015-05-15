package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplycmentPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;

public interface DemandSupplyComentPushService {


	void upatePushInfo(YBasicDemandsupplycmentPushinfo commentPushInfo) throws Exception;


	void savePushInfo(YBasicDemandsupplycmentPushinfo commentPushInfo) throws Exception;


	void saveConfigurationMessage(YSystemconfigurationMessage message)throws Exception;
	
	/**
	 * 条件查询我的供需评论推送记录信息
	 * @param dsId
	 * @return
	 */
	public List<YBasicDemandsupplycmentPushinfo> findByHql(String mid);
	
	/**
	 * 修改我的供需评论推送信息记录
	 * @param dsp
	 */
	public void update(YBasicDemandsupplycmentPushinfo demandsupplycmentPushinfo);

	/**
	 * 未读消息
	 * @return
	 */
	public int unreadDemandSupplyComentMsg(String tel);


	YBasicDemandsupplycmentPushinfo findPushInfo(String pushInfoId) throws Exception;
}
