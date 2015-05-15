package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YReplytocomment;

/**
 * @author：jackpeng
 * @date：2014年12月17日上午4:37:12
 * 回复service接口
 */
public interface ReplytocommentServiceI {
	
	/**
	 * 查询并分页
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getReplyList(String mid,String fId,int pageSize,String commentID);

	public YReplytocomment findReplyById(String id);

	public YReplytocomment save(YReplytocomment reply);

	YReplytocomment update(YReplytocomment reply);

	void del(String id);
	
}
