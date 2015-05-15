package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.service.AppdeviceServiceI;

/**
 * Description: <设备信息服务实现类>. <br>
 * @date:2014年12月29日 下午12:12:16
 * @author beck
 * @version V1.0
 */
@Service("deviceService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class AppdeviceServiceImpl implements AppdeviceServiceI{
	private static final Logger logger = Logger.getLogger(AppdeviceServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	@SystemServiceLog(description = "新增设备信息服务")
	public void save(YAppdevice demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询设备信息服务")
	public List<YAppdevice> getByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<YAppdevice>) dao.findAllByHQL(hql);
	}
	
}
