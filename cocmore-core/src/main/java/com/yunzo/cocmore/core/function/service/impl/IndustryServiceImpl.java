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
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.service.IndustryServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <产业服务实现类>. <br>
 * @date:2014年11月25日 下午5:07:40
 * @author beck
 * @version V1.0                             
 */
@Service("indService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class IndustryServiceImpl implements IndustryServiceI{
	
	private static final Logger logger = Logger
			.getLogger(IndustryServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部产业，并分页")
	public PagingList<YBasicIndustry> findAll(String searchName,Integer start,Integer limit) {
		logger.info("List<YBasicIndustry> findAll()");
		PagingList<YBasicIndustry> pagingList = new PagingList<YBasicIndustry>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YBasicIndustry>)dao.findAll(YBasicIndustry.class));
			return pagingList;
		}
		
		String hql = "from YBasicIndustry y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fname like '%" + searchName + "%'";
		}
		pagingList.setList((List<YBasicIndustry>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YBasicIndustry> list = (List<YBasicIndustry>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询产业")
	public YBasicIndustry getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicIndustry getById(String fid) || id==" + fid);
		return (YBasicIndustry) dao.findById(YBasicIndustry.class, fid);
	}

	@Override
	@SystemServiceLog(description = "新增产业")
	public void save(YBasicIndustry demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	@SystemServiceLog(description = "删除产业")
	public void delete(YBasicIndustry demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	@SystemServiceLog(description = "修改产业")
	public void update(YBasicIndustry demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}
	
}
