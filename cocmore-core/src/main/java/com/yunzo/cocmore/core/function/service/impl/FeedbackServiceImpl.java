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
import com.yunzo.cocmore.core.function.model.mysql.YBasicFeedback;
import com.yunzo.cocmore.core.function.model.mysql.YVisitorsRecordNotlogin;
import com.yunzo.cocmore.core.function.service.FeedbackServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <意见反馈服务实现类>. <br>
 * @date:2014年12月5日 下午5:44:55
 * @author beck
 * @version V1.0                             
 */
@Service("feedbackService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class FeedbackServiceImpl implements FeedbackServiceI {
	
	private static final Logger logger = Logger
			.getLogger(FeedbackServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	/**
	 * 查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询意见反馈，并分页")
	public PagingList<YBasicFeedback> findAll(String searchName, Integer start,
			Integer limit) {
		logger.info("List<YBasicFeedback> findAll()");
		PagingList<YBasicFeedback> pagingList = new PagingList<YBasicFeedback>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YBasicFeedback>)dao.findAll(YBasicFeedback.class));
			return pagingList;
		}
		
		String hql = "from YBasicFeedback y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fmessage like '%" + searchName + "%'";
		}
		pagingList.setList((List<YBasicFeedback>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YBasicFeedback> list = (List<YBasicFeedback>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
	
	/**
	 * 保存
	 * @param demo
	 */
	@Override
	@SystemServiceLog(description = "新增意见反馈")
	public void save(YBasicFeedback demo){
		dao.save(demo);
	}

}
