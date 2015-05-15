package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationFunctionService;

/**
 * @ClassName: YSystemconfigurationFunctionServiceImpl 
 * @Description: TODO 模块接口实现类 
 * @date 2014年11月24日 下午3:40:43 
 * @author Ian
 *
 */
@Service("systemconfigurationFunctionService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class YSystemconfigurationFunctionServiceImpl implements YSystemconfigurationFunctionService {

	private static final Logger logger = Logger.getLogger(YSystemconfigurationFunctionServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "新增模块")
	public void save(YSystemconfigurationFunction SystemconfigurationFunction) {
		dao.save(SystemconfigurationFunction);
	}

	@Override
	@SystemServiceLog(description = "删除模块")
	public void delete(YSystemconfigurationFunction SystemconfigurationFunction) {
		dao.delete(SystemconfigurationFunction);
	}

	@Override
	@SystemServiceLog(description = "根据id查询模块")
	public YSystemconfigurationFunction getById(String functionid) {
		return (YSystemconfigurationFunction)dao.findById(YSystemconfigurationFunction.class,functionid);
	}

	@Override
	@SystemServiceLog(description = "修改模块")
	public void update(YSystemconfigurationFunction SystemconfigurationFunction) {
		dao.update(SystemconfigurationFunction);
	}

	@Override
	@SystemServiceLog(description = "hql查询模块")
	public List<YSystemconfigurationFunction> findByHql(String hql) {
		
		return (List<YSystemconfigurationFunction>)dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "查询全部模块")
	public List<YSystemconfigurationFunction> findAll() {
		
		return (List<YSystemconfigurationFunction>)dao.findAll(YSystemconfigurationFunction.class);
	}
	
	
}
