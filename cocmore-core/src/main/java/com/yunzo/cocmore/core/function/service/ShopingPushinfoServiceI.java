package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicShopingPushinfo;

/**
 * @author：jackpeng
 * @date：2015年3月27日下午5:55:26
 * 预约推送信息记录接口
 */
public interface ShopingPushinfoServiceI {
	
	/**
	 * 根据手机号查询预约记录信息
	 * @param mid
	 * @return
	 */
	public List<YBasicShopingPushinfo> findShopinPhone(String mid);
	
	/**
	 * 修改预约推送消息记录
	 * @param shopingPushinfo
	 */
	public void update(YBasicShopingPushinfo shopingPushinfo);
	
	/**
	 * 修改预约推送消息记录
	 * @param shopingPushinfo
	 */
	public void save(YBasicShopingPushinfo shopingPushinfo);
	
	/**
	 * 未读消息
	 * @param tel
	 * @return
	 */
	public int unreadOrderMsg(String tel);
	
}
