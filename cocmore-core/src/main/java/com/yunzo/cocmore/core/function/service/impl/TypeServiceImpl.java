package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.service.TypeService;

/**
 * @author：jackpeng
 * @date：2014年12月13日下午2:44:39
 * 类型service实现类
 */
@Service("typeService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class TypeServiceImpl implements TypeService {
	
	private static final Logger logger = Logger
			.getLogger(TypeServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询类型")
	public List<YBasicType> getTypeHql(String hql) {
		// TODO Auto-generated method stub
		return (List<YBasicType>) dao.findAllByHQL(hql);
	}
	
}
