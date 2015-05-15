package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.service.CountyService;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午10:19:31
 * 区县service实现类
 */
@Service("countyService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class CountyServiceImpl implements CountyService {
	private static final Logger logger = Logger
			.getLogger(CountyServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部区县")
	public List<YBasicCounty> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YBasicCounty> findAll()");
		return (List<YBasicCounty>) dao.findAll(YBasicCounty.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询区县")
	public List<YBasicCounty> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YBasicCounty> getByHql(String hql)");
		return (List<YBasicCounty>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "根据id查询区县")
	public YBasicCounty getById(String id) {
		// TODO Auto-generated method stub
		return (YBasicCounty) dao.findById(YBasicCounty.class, id);
	}
}
