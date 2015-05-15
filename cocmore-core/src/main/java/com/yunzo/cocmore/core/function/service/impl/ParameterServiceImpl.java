package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationParameter;
import com.yunzo.cocmore.core.function.service.ParameterServiceI;

/** 
 *Description: <系统参数服务实现类>. <br>
 * @date:2014年11月27日 下午4:12:32
 * @author beck
 * @version V1.0                             
 */
@Service("parService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class ParameterServiceImpl implements ParameterServiceI{
	private static final Logger logger = Logger
			.getLogger(ParameterServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部系统参数")
	public List<YSystemconfigurationParameter> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YSystemconfigurationParameter> findAll()");
		return (List<YSystemconfigurationParameter>)dao.findAll(YSystemconfigurationParameter.class);
	}

	@Override
	@SystemServiceLog(description = "根据id查询系统参数")
	public YSystemconfigurationParameter getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YSystemconfigurationParameter getById(String fid) || id==" + fid);
		return (YSystemconfigurationParameter) dao.findById(YSystemconfigurationParameter.class, fid);
	}

	@Override
	@SystemServiceLog(description = "修改系统参数")
	public void update(YSystemconfigurationParameter demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}
}
