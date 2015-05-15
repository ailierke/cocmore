package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.service.AssurancecontentService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月25日下午5:29:16
 * 供应担保内容service实现类
 */
@Service("contentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class AssurancecontentServiceImpl implements AssurancecontentService {
	
	private static final Logger logger = Logger.getLogger(AssurancecontentServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部供应担保")
	public List<YBasicAssurancecontent> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YBasicAssurancecontent> findAll()");
		return (List<YBasicAssurancecontent>) dao.findAll(YBasicAssurancecontent.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部供应担保，并分页")
	public PagingList<YBasicAssurancecontent> getAllControllerPagingList(
			Integer page, Integer pageSize, String groupId, String content) {
		// TODO Auto-generated method stub
		logger.info("查询信息列表，并分页....");
		PagingList<YBasicAssurancecontent> pagingList = new PagingList<YBasicAssurancecontent>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicAssurancecontent y where 1=1");
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicAssurancecontent y where 1=1");
		/**
		 * 判断是否通过groupId或content
		 */
		if(groupId != null){
			hqlList.append("and y.YBasicSocialgroups.fid=?");
			hqlCount.append("and y.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		if(content != null){
			hqlList.append(" and y.fcontent like '%"+content+"%'");
			hqlCount.append(" and y.fcontent like '%"+content+"%'");
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicAssurancecontent>)dao.find(hqlList.toString(), page, pageSize,values.toArray()));
		
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询供应担保")
	public YBasicAssurancecontent getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YBasicAssurancecontent getById(String id)");
		return (YBasicAssurancecontent) dao.findById(YBasicAssurancecontent.class, id);
	}

	@Override
	@SystemServiceLog(description = "新增供应担保")
	public void save(YBasicAssurancecontent content) {
		// TODO Auto-generated method stub
		dao.save(content);
	}

	@Override
	@SystemServiceLog(description = "删除供应担保")
	public void delete(YBasicAssurancecontent content) {
		// TODO Auto-generated method stub
		dao.delete(content);
	}

	@Override
	@SystemServiceLog(description = "修改供应担保")
	public void update(YBasicAssurancecontent content) {
		// TODO Auto-generated method stub
		dao.update(content);
	}
	
}
