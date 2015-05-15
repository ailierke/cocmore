package com.yunzo.cocmore.core.function.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.AppSystemCommandLog;
import com.yunzo.cocmore.core.function.service.AppSysComLogServiceI;

/**
 * Description: <手机执行命令记录service接口实现类>. <br>
 * @date:2015年3月12日 下午3:59:01
 * @author beck
 * @version V1.0
 */
@Service("appSCLogService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class AppSysComLogServiceImpl implements AppSysComLogServiceI{
	private static final Logger logger = Logger.getLogger(AppSysComLogServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	public void save(AppSystemCommandLog demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	public void update(AppSystemCommandLog demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}

	@Override
	public void delete(AppSystemCommandLog demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	public AppSystemCommandLog getById(String id) {
		// TODO Auto-generated method stub
		return (AppSystemCommandLog) dao.get(AppSystemCommandLog.class, id);
	}
	
	/**
	 * APP端传递参数过来保存
	 * @param map
	 */
	public void appSave(Map<String, Object> map){
		String fmid = (String) map.get("userName");		//电话
		String fGroupID = (String) map.get("businessId");	//团体
		String exeCode = (String) map.get("exeCode");	//执行命令类型
		
		AppSystemCommandLog log = new AppSystemCommandLog();
		log.setFmid(fmid);
		log.setFgroupID(fGroupID);
		log.setFascTypeID(exeCode);
		log.setFbillState(1);
		log.setFextTime(new Date());
		
		dao.save(log);
	}
	
}
