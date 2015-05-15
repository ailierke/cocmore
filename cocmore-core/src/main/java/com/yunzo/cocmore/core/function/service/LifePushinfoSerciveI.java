package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Set;

import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLifePushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;

/**
 * Description: <生活推送记录接口>. <br>
 * @date:2015年3月18日 下午12:06:58
 * @author beck
 * @version V1.0
 */
public interface LifePushinfoSerciveI {
	
	/**
	 * 新增
	 * @param demo
	 */
	public void save(YBasicLifePushinfo demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YBasicLifePushinfo demo);
	
	/**
	 * 查询
	 * @return
	 */
	public List<YBasicLifePushinfo> selectLifePush();
	
	/**
	 * 推送消息
	 */
	public Set<String> getTels();
	
	public void saveConfigurationMessage(YSystemconfigurationMessage message)throws Exception;
	
	/**
	 * 生活推送未读消息
	 * @return
	 */
	public int unreadLifeMsg(String tel);
	
	
	/**
	 * 条件查询生活推送记录信息
	 * @param dsId
	 * @return
	 */
	public YBasicLifePushinfo findLifePushinfo(String fid,String mid);
	
	/**
	 * 根据电话查询该项目推送是否已读
	 * @return
	 */
	public int isReadByTel(String tel,String lifeId);
	
}
