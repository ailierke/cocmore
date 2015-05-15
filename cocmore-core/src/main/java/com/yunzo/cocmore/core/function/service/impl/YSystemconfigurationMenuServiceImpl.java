package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMenu;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationMenuService;

/**
 * @ClassName: YSystemconfigurationMenuServiceImpl 
 * @Description: TODO菜单接口实现类 
 * @date 2014年11月24日 下午4:49:15 
 * @author Ian
 *
 */
@Service("menuService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class YSystemconfigurationMenuServiceImpl implements YSystemconfigurationMenuService {

private static final Logger logger = Logger.getLogger(CharacterServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	@Override
	@SystemServiceLog(description = "新增TODO菜单")
	public void save(YSystemconfigurationMenu menu) {
		dao.save(menu);
		
	}

	@Override
	@SystemServiceLog(description = "修改TODO菜单")
	public void update(YSystemconfigurationMenu menu) {
		dao.update(menu);
		
	}

	@Override
	@SystemServiceLog(description = "删除TODO菜单")
	public void delete(YSystemconfigurationMenu menu) {
		dao.delete(menu);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YSystemconfigurationMenu> findAll() {
		
		return (List<YSystemconfigurationMenu>)dao.findAll(YSystemconfigurationMenu.class);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<YSystemconfigurationMenu> findByHql(String hql){
		return (List<YSystemconfigurationMenu>)dao.findAllByHQL(hql);
	}

	@Override
	public YSystemconfigurationMenu getById(String fid) {
		return (YSystemconfigurationMenu)dao.findById(YSystemconfigurationMenu.class,fid);
	}
	
	
}
