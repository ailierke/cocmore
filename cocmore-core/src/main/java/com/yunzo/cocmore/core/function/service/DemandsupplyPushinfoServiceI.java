package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;

/**
 * @author：jackpeng
 * @date：2015年3月18日下午3:18:14
 * 供需推送信息记录service
 */
public interface DemandsupplyPushinfoServiceI {
	
	/**
	 * 新增供需推送信息记录
	 * @param bdp
	 */
	public void save(YBasicDemandsupplyPushinfo bdp);
	
	/**
	 * 系统消息记录
	 * @param message
	 * @throws Exception
	 */
	public void saveConfigurationMessage(YSystemconfigurationMessage message)throws Exception;
	
	/**
	 * 未读消息
	 * @return
	 */
	public int unreadDemandSupplyMsg();
	

	/**
	 * 条件查询供需推送记录信息
	 * @param dsId
	 * @return
	 */
	public YBasicDemandsupplyPushinfo findDemandsupplyPushinfo(String fid, String mid);
	
	/**
	 * 修改供需推送信息记录
	 * @param dsp
	 */
	public void update(YBasicDemandsupplyPushinfo dsp);

}
