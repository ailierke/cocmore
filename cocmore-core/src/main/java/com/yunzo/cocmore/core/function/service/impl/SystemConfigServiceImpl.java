package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.SystemConfig;
import com.yunzo.cocmore.core.function.service.SystemConfigServiceI;

/**
 * Description: <系统配置service接口实现类>. <br>
 * @date:2015年3月13日 上午11:30:08
 * @author beck
 * @version V1.0
 */
@Service("sysconService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class SystemConfigServiceImpl implements SystemConfigServiceI {
	
	private static final Logger logger = Logger.getLogger(ApplyServiceImpl.class);
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	public void save(SystemConfig demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}
	@Override
	public void update(SystemConfig demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}
	@Override
	public void delete(SystemConfig demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SystemConfig getObjByKey(String key) {
		// TODO Auto-generated method stub
		String hql = "from SystemConfig where fkey = '" + key + "'";
		
		List<SystemConfig> list = (List<SystemConfig>) dao.find(hql);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
		
	}
}
