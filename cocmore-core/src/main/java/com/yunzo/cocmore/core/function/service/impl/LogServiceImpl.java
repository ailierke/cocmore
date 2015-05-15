package com.yunzo.cocmore.core.function.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationLog;
import com.yunzo.cocmore.core.function.service.LogServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <日志服务实现类>. <br>
 * @date:2014年11月27日 上午11:55:40
 * @author beck
 * @version V1.0                             
 */
@Service("logService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class LogServiceImpl implements LogServiceI{

	private static final Logger logger = Logger
			.getLogger(LogServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public PagingList<YSystemconfigurationLog> findAll(String searchName,Integer start,Integer limit) {
		logger.info("List<LogServiceImpl> findAll()");
		PagingList<YSystemconfigurationLog> pagingList = new PagingList<YSystemconfigurationLog>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YSystemconfigurationLog>)dao.findAll(YSystemconfigurationLog.class));
			return pagingList;
		}
		
		String hql = "from YSystemconfigurationLog y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fuserId like '%" + searchName + "%'";
		}
		pagingList.setList((List<YSystemconfigurationLog>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YSystemconfigurationLog> list = (List<YSystemconfigurationLog>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	public YSystemconfigurationLog getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YSystemconfigurationLog getById(String fid) || id==" + fid);
		return (YSystemconfigurationLog) dao.findById(YSystemconfigurationLog.class, fid);
	}
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YSystemconfigurationLog demo){
		logger.info("YSystemconfigurationLog save");
		dao.save(demo);
		
	}
	
	/**
	 * 查询数据库日志的总数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long count(){
		List<YSystemconfigurationLog> list = (List<YSystemconfigurationLog>) dao.findAllByHQL("from YSystemconfigurationLog");
		if(list != null && list.size() > 0){
			return list.size();
		}
		return 0;
	}
	
	/**
	 * 查询数据库日志的最大flag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long maxFlag(){
		List<Long> list = (List<Long>) dao.getListBySql("select max(y.flag) from y_systemconfiguration_log y");
		return list.get(0);
	}
	

}
