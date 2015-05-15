package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicMemberbymemberhide;

/**
 * Description: <会员对会员黑名单服务接口>. <br>
 * @date:2014年12月17日 上午9:50:52
 * @author beck
 * @version V1.0
 */
public interface MBMHideServiceI {
	
	/**
	 * 查询全部
	 * @return
	 */
	public List<Map<String, Object>> findAllByInId(String phone,String businessId);
	
	/**
	 * 新增
	 * @return
	 */
	public void save(YBasicMemberbymemberhide demo);
	
	/**
	 * 根据发起人和隐藏人查询
	 * @return
	 */
	public YBasicMemberbymemberhide findByInOut(String in,String out);
	
	/**
	 * 删除
	 * @return
	 */
	public void delete(YBasicMemberbymemberhide demo);
	
	/**
	 * 设置通讯录隐私
	 * @param infoMap
	 */
	public void setContactsPrivacy(String infoMap);
}
