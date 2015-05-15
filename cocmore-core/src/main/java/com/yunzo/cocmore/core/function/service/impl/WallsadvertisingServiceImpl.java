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
import com.yunzo.cocmore.core.function.model.mysql.YWallsadvertising;
import com.yunzo.cocmore.core.function.service.WallsadvertisingService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月27日上午9:57:08
 * 上墙广告service实现类
 */
@Service("WallsadvertisingService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
public class WallsadvertisingServiceImpl implements WallsadvertisingService {

	private static final Logger logger = Logger.getLogger(WallsadvertisingServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部上墙活动广告，并分页")
	public PagingList<YWallsadvertising> getAllSadvertisingPagingList(
			Integer page, Integer pageSize, String activityId) {
		// TODO Auto-generated method stub
		logger.info("查询全部活动广告并分页...");
		PagingList<YWallsadvertising> pagingList = new PagingList<YWallsadvertising>();
		List<Object> list = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YWallsadvertising y where 1=1");
		StringBuffer hqlCount = new StringBuffer("select count(0) from YWallsadvertising y where 1=1");
		if(activityId != null){
			hqlList.append("and y.YWallactivity.fid=?");
			hqlCount.append("and y.YWallactivity.fid=?");
			list.add(activityId);
		}
		//获得此页数据
		pagingList.setList((List<YWallsadvertising>)dao.find(hqlList.toString(), page, pageSize, list.toArray()));
		//获得总条数
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, list.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@SuppressWarnings(value = "unchecked")
	@Override
	@SystemServiceLog(description = "hql查询上墙活动广告")
	public List<YWallsadvertising> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YWallsadvertising> getByHql(String hql)");
		return (List<YWallsadvertising>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "新增上墙活动广告")
	public void save(YWallsadvertising wallAd) {
		// TODO Auto-generated method stub
		dao.save(wallAd);
	}

	@Override
	@SystemServiceLog(description = "删除上墙活动广告")
	public void delete(YWallsadvertising wallAd) {
		// TODO Auto-generated method stub
		dao.delete(wallAd);
	}

	@Override
	@SystemServiceLog(description = "根据id查询上墙活动广告")
	public YWallsadvertising getById(String id) {
		// TODO Auto-generated method stub
		return (YWallsadvertising) dao.findById(YWallsadvertising.class, id);
	}
	
}
