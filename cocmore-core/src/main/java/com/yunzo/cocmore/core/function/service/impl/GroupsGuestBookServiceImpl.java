package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsguestbook;
import com.yunzo.cocmore.core.function.service.GroupsGuestBookService;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 社会团体留言业务实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsGuestbookService")
public class GroupsGuestBookServiceImpl implements GroupsGuestBookService {
	private static final Logger logger = Logger.getLogger(GroupsGuestBookServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	
	@Override
	@SystemServiceLog(description = "新增团体留言")
	public Boolean addGuestbook(YBasicSocialgroupsguestbook guestbook) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.save(guestbook);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询团体留言")
	public PagingList<YBasicSocialgroupsguestbook> getAllGuestbookPagingList(Integer page, Integer pageSize,String createUserName,String groupId) {
		logger.info("查询留言信息列表....");
		PagingList<YBasicSocialgroupsguestbook> pagingList = new PagingList<YBasicSocialgroupsguestbook>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsguestbook guestBook"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsguestbook guestBook"); 
		/**
		 * 如果创建人条件存在就执行条件查询
		 */
		if(null!=createUserName&&!"".equals(createUserName)){
			hqlList.append(" where guestBook.YBasicMember.fname like ?");
			hqlCount.append(" where guestBook.YBasicMember.fname like ?");
			values.add("%"+createUserName+"%");
		}
		/**
		 * 如果前台传来组织id就查询组织下面的所有团体
		 */
		if(null!=groupId&&!"".equals(groupId)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("guestBook.YBasicSocialgroups.fid=?");
			hqlCount.append("guestBook.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicSocialgroupsguestbook>) cOC_HibernateDAO.find(hqlList.toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
}
