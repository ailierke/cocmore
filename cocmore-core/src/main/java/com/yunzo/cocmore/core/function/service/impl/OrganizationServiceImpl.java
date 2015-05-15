package com.yunzo.cocmore.core.function.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.service.OrganizationService;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <组织服务实现类>. <br>
 * @date:2014年11月24日 下午3:25:05
 * @author beck
 * @version V1.0                             
 */
@Service("orgService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class OrganizationServiceImpl implements OrganizationService{
	
	private static final Logger logger = Logger
			.getLogger(OrganizationServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	/**
	 * 分页查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部组织，并分页")
	public PagingList<YBasicOrganization> findAll(String searchName,Integer start,Integer limit) {
		logger.info("List<YBasicOrganization> findAll()");
		PagingList<YBasicOrganization> pagingList = new PagingList<YBasicOrganization>();
		
		String hql = "from YBasicOrganization y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fname like '%" + searchName + "%'";
		}
		pagingList.setList((List<YBasicOrganization>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YBasicOrganization> list = (List<YBasicOrganization>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
	
	/**
	 * 根据参数ID查询
	 * 查询出参数ID除外的所有数据
	 * 为空的话查询出所有
	 * @param superId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据参数ID查询查询出参数ID除外的所有数据为空的话查询出所有")
	public List<YBasicOrganization> findAllBySuperId(String superId){
		logger.info("List<YBasicOrganization> findAllBySuperId()");
		String hql = "from YBasicOrganization y where 1=1 ";
		if(superId != null){
			hql = hql + "and y.fid != '" + superId + "'";
		}
		return (List<YBasicOrganization>)dao.find(hql);
	}

	@Override
	@SystemServiceLog(description = "根据id查询组织")
	public YBasicOrganization getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicOrganization getById(String fid) || id==" + fid);
		return (YBasicOrganization) dao.findById(YBasicOrganization.class, fid);
	}

	@Override
	@SystemServiceLog(description = "新增组织")
	public void save(YBasicOrganization demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	@SystemServiceLog(description = "删除组织")
	public void delete(YBasicOrganization demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	@SystemServiceLog(description = "修改组织")
	public void update(YBasicOrganization demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部组织")
	public List<YBasicOrganization> findAll() {
		// TODO Auto-generated method stub
		List<YBasicOrganization> list = (List<YBasicOrganization>) dao.find("select new YBasicOrganization(y.fid,y.fname,y.fbillState,y.fsuperiorOrganizationId) from YBasicOrganization y");
		//List<YBasicOrganization> list = (List<YBasicOrganization>)dao.findAll(YBasicOrganization.class);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询组织")
	public List<YBasicOrganization> getByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<YBasicOrganization>)dao.findAllByHQL(hql);
	}
	
}
