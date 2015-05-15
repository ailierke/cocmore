package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YWallreply;
import com.yunzo.cocmore.core.function.service.WallreplyService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月27日下午4:59:43
 * 上墙回复service实现类
 */
@Service("replyService")
@Transactional(propagation = Propagation.REQUIRED,readOnly = false,rollbackFor = {Exception.class})
public class WallreplyServiceImpl implements WallreplyService {
	
	private static final Logger logger = Logger.getLogger(WallreplyServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据上墙活动id查询上墙回复，并分页")
	public PagingList<YWallreply> getAllReplyPagingList(Integer page,
			Integer pageSize, String activityId, Integer state) {
		// TODO Auto-generated method stub
		logger.info("查询全部回复并分页");
		PagingList<YWallreply> pagingList = new PagingList<YWallreply>();
		List<Object> list = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YWallreply y where 1=1");
		StringBuffer hqlCount = new StringBuffer("select count(0) from YWallreply y where 1=1");
		if(activityId != null){
			hqlList.append("and y.YWallactivity.fid=? ");
			hqlCount.append("and y.YWallactivity.fid=? ");
			list.add(activityId);
		}
		if(state != null){
			hqlList.append("and  y.fstate="+state+"");
			hqlCount.append("and y.fstate="+state+"");
		}
		pagingList.setList((List<YWallreply>)dao.find(hqlList.toString(), page, pageSize, list.toArray()));
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, list.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询上墙回复")
	public YWallreply getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YWallreply getById(String id) fid=="+id);
		return (YWallreply) dao.findById(YWallreply.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询上墙回复")
	public List<YWallreply> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YWallreply> getByHql(String hql)");
		return (List<YWallreply>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "删除上墙回复")
	public void delete(YWallreply wallReply) {
		// TODO Auto-generated method stub
		dao.delete(wallReply);
	}

	@Override
	@SystemServiceLog(description = "修改上墙回复")
	public void update(YWallreply wallReply) {
		// TODO Auto-generated method stub
		dao.update(wallReply);
	}

	@Override
	@SystemServiceLog(description = "新增上墙回复")
	public void save(YWallreply wallReply) {
		// TODO Auto-generated method stub
		dao.save(wallReply);
	}
	
}
