package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.service.CityService;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午10:04:26
 * 城市service实现类
 */
@Service("cityService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class CityServiceImpl implements CityService {
	private static final Logger logger = Logger
			.getLogger(CityServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部城市")
	public List<YBasicCity> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YBasicCity> findAll()");
		return (List<YBasicCity>) dao.findAll(YBasicCity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询城市")
	public List<YBasicCity> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YBasicCity> getByHql(String hql)");
		return (List<YBasicCity>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "根据id查询城市")
	public YBasicCity getById(String id) {
		// TODO Auto-generated method stub
		return (YBasicCity) dao.findById(YBasicCity.class, id);
	}
	
	
}
