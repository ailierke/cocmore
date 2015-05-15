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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.MessageServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <系统消息服务实现类>. <br>
 * @date:2014年11月27日 下午4:25:23
 * @author beck
 * @version V1.0                             
 */
@Service("msgService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class MessageServiceImpl implements MessageServiceI{
	private static final Logger logger = Logger
			.getLogger(MessageServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部系统消息服务，并分页")
	public PagingList<YSystemconfigurationMessage> findAll(String searchName,Integer start,Integer limit) {
		// TODO Auto-generated method stub
		logger.info("List<YSystemconfigurationMessage> findAll()");
		PagingList<YSystemconfigurationMessage> pagingList = new PagingList<YSystemconfigurationMessage>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YSystemconfigurationMessage>)dao.findAll(YSystemconfigurationMessage.class));
			return pagingList;
		}
		
		String hql = "from YSystemconfigurationMessage y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fmessageType like '%" + searchName + "%'";
		}
		pagingList.setList((List<YSystemconfigurationMessage>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YSystemconfigurationMessage> list = (List<YSystemconfigurationMessage>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询系统消息服务")
	public YSystemconfigurationMessage getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YSystemconfigurationMessage getById(String fid) || id==" + fid);
		return (YSystemconfigurationMessage) dao.findById(YSystemconfigurationMessage.class, fid);
	}

	@Override
	@SystemServiceLog(description = "根据id新增系统消息服务")
	public void save(YSystemconfigurationMessage demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	@SystemServiceLog(description = "删除系统消息服务")
	public void delete(YSystemconfigurationMessage demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	@SystemServiceLog(description = "新增或修改系统消息服务")
	public void saveOrUpdate(YSystemconfigurationMessage demo) {
		// TODO Auto-generated method stub
		dao.saveOrUpdate(demo);
	}
	
}
