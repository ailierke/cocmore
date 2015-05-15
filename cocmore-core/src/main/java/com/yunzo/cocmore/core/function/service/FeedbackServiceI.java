package com.yunzo.cocmore.core.function.service;

import com.yunzo.cocmore.core.function.model.mysql.YBasicFeedback;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <意见反馈服务接口>. <br>
 * @date:2014年12月5日 下午5:43:51
 * @author beck
 * @version V1.0                             
 */

public interface FeedbackServiceI {
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YBasicFeedback> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YBasicFeedback demo);
}
