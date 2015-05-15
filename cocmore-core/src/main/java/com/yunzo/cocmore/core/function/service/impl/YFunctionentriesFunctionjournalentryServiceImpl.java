package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.service.YFunctionentriesFunctionjournalentryService;

/**
 * @ClassName: YFunctionentriesFunctionjournalentryServiceImpl 
 * @Description: TODO 功能表接口实现类 
 * @date 2014年11月24日 下午3:47:23 
 * @author Ian
 *
 */
@Service("functionentriesFunctionjournalentryService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class YFunctionentriesFunctionjournalentryServiceImpl implements YFunctionentriesFunctionjournalentryService {

	private static final Logger logger = Logger.getLogger(YFunctionentriesFunctionjournalentryServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "新增功能")
	public void save(YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry) {
		dao.save(functionentriesFunctionjournalentry);
	}

	@Override
	@SystemServiceLog(description = "删除功能")
	public void delete(YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry) {
		dao.delete(functionentriesFunctionjournalentry);
	}

	@Override
	@SystemServiceLog(description = "根据模块id查询功能")
	public List<YFunctionentriesFunctionjournalentry> findByFunctionID(String functionId) {
		
		return (List<YFunctionentriesFunctionjournalentry>)dao.findAllByHQL("from YFunctionentriesFunctionjournalentry as y where y.YSystemconfigurationFunction.fid='"+functionId+"'");
	}

	@Override
	@SystemServiceLog(description = "修改功能")
	public void update(YFunctionentriesFunctionjournalentry functionentriesFunctionjournalentry) {
		dao.update(functionentriesFunctionjournalentry);
	}

	@Override
	@SystemServiceLog(description = "hql查询功能")
	public List<YFunctionentriesFunctionjournalentry> findByHql(String hql) {
		return (List<YFunctionentriesFunctionjournalentry>)dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "查询全部功能")
	public List<YFunctionentriesFunctionjournalentry> findAll() {
		return (List<YFunctionentriesFunctionjournalentry>)dao.findAll(YFunctionentriesFunctionjournalentry.class);
	}

	@Override
	@SystemServiceLog(description = "根据id查询功能")
	public YFunctionentriesFunctionjournalentry getById(String fid) {
		
		return (YFunctionentriesFunctionjournalentry)dao.findById(YFunctionentriesFunctionjournalentry.class, fid);
	}

}
