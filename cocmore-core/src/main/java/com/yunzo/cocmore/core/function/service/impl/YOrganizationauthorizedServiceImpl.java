package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;
import com.yunzo.cocmore.core.function.service.YOrganizationauthorizedService;


/**
 * @ClassName: YOrganizationauthorizedServiceImpl 
 * @Description: TODO 用户关系接口实现类 
 * @date 2014年11月25日 下午4:30:55 
 * @author Ian
 *
 */
@Service("organizationauthorizedService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class YOrganizationauthorizedServiceImpl implements YOrganizationauthorizedService {

	private static final Logger logger = Logger.getLogger(YOrganizationauthorizedServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "新增用户关系")
	public void save(YOrganizationauthorized organizationauthorized) {
		dao.save(organizationauthorized);
	}

	@Override
	@SystemServiceLog(description = "修改用户关系")
	public void update(YOrganizationauthorized organizationauthorized) {
		dao.update(organizationauthorized);
	}

	@Override
	@SystemServiceLog(description = "删除用户关系")
	public void delete(YOrganizationauthorized organizationauthorized) {
		dao.delete(organizationauthorized);
	}

	@Override
	@SystemServiceLog(description = "根据用户id查询用户关系")
	public List<YOrganizationauthorized> findByUserID(String userid) {
		return (List<YOrganizationauthorized>)dao.findAllByHQL("from YOrganizationauthorized as y where y.YSystemUsers.fid='"+userid+"'");
	}

	@Override
	@SystemServiceLog(description = "根据id查询用户关系")
	public YOrganizationauthorized getById(String fid) {
		return (YOrganizationauthorized)dao.findById(YOrganizationauthorized.class, fid);
	}

	@Override
	@SystemServiceLog(description = "hql查询用户关系")
	public List<YOrganizationauthorized> findByHql(String hql) {
		return (List<YOrganizationauthorized>)dao.findAllByHQL(hql);
	}
	
}
