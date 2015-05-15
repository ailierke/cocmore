package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.service.ProvinceService;

/**
 * @author：jackpeng
 * @date：2014年12月2日上午9:28:42
 * 省份service实现类
 */
@Service("provinceService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class ProvinceServiceImpl implements ProvinceService {
	private static final Logger logger = Logger
			.getLogger(ProvinceServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部省份")
	public List<YBasicProvince> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YBasicProvince> findAll()");
		return (List<YBasicProvince>) dao.findAll(YBasicProvince.class);
	}

	@Override
	@SystemServiceLog(description = "根据id查询省份")
	public YBasicProvince getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YBasicProvince getById(String id)");
		return (YBasicProvince) dao.findById(YBasicProvince.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YBasicProvince> findByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("YBasicProvince findByHql(String hql)");
		return (List<YBasicProvince>) dao.findAllByHQL(hql);
	}
	
}
