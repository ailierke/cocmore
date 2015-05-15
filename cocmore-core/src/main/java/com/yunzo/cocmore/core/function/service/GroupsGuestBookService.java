package com.yunzo.cocmore.core.function.service;


import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsguestbook;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 
 * @author jiangxing
 * 社会团体留言service
 */
public interface GroupsGuestBookService {
	/**
	 * 添加
	 * @param guestbook
	 * @return 
	 */
	public Boolean addGuestbook(YBasicSocialgroupsguestbook guestbook);
	/**
	 * 查询列表 根据创建人id
	 * page查询的第几页
	 * pageSize每页有多少条数据
	 * @param createUserId 创建人id
	 * @param groupId 所属团体
	 */
	public PagingList<YBasicSocialgroupsguestbook> getAllGuestbookPagingList(Integer page,Integer pageSize,String createUserId,String groupId);

}
