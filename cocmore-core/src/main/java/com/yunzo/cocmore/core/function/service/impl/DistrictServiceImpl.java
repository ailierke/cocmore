package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDistrict;
import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.service.DistrictServiceI;

/** 
 *Description: <区域服务实现类>. <br>
 * @date:2014年12月2日 下午3:45:58
 * @author beck
 * @version V1.0                             
 */
@Service("disService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class DistrictServiceImpl implements DistrictServiceI{
	private static final Logger logger = Logger
			.getLogger(DistrictServiceImpl.class);

	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部区域")
	public List<YBasicDistrict> findAll() {
		logger.info("List<YBasicDistrict> findAll()");
		return (List<YBasicDistrict>)dao.findAll(YBasicDistrict.class);
	}
}
