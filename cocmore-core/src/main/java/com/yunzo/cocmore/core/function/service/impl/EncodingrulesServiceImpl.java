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
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.service.EncodingrulesServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;
/** 
 *Description: <编码规则服务实现类>. <br>
 * @date:2014年11月27日 下午3:55:45
 * @author beck
 * @version V1.0                             
 */
@Service("encService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class EncodingrulesServiceImpl implements EncodingrulesServiceI{
	private static final Logger logger = Logger
			.getLogger(EncodingrulesServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询编码规则，并分页")
	public PagingList<YSystemconfigurationEncodingrules> findAll(String searchName,Integer start,Integer limit) {
		// TODO Auto-generated method stub
		logger.info("List<YSystemconfigurationEncodingrules> findAll()");
		PagingList<YSystemconfigurationEncodingrules> pagingList = new PagingList<YSystemconfigurationEncodingrules>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YSystemconfigurationEncodingrules>)dao.findAll(YSystemconfigurationEncodingrules.class));
			return pagingList;
		}
		
		String hql = "from YSystemconfigurationEncodingrules y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fname like '%" + searchName + "%'";
		}
		pagingList.setList((List<YSystemconfigurationEncodingrules>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YSystemconfigurationEncodingrules> list = (List<YSystemconfigurationEncodingrules>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询编码规则")
	public YSystemconfigurationEncodingrules getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YSystemconfigurationEncodingrules getById(String fid) || id==" + fid);
		return (YSystemconfigurationEncodingrules) dao.findById(YSystemconfigurationEncodingrules.class, fid);
	}

	@Override
	@SystemServiceLog(description = "修改编码规则")
	public void update(YSystemconfigurationEncodingrules demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}
	
	
}
