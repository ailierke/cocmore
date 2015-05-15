package com.yunzo.cocmore.core.function.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessagerecord;
import com.yunzo.cocmore.core.function.service.MessagerecordServiceI;

/**
 * @author：jackpeng
 * @date：2015年1月5日上午3:26:52
 * 
 */
@Transactional
@Service("messagerecordService")
public class MessagerecordServiceImpl implements MessagerecordServiceI {
	private static final Logger logger = Logger.getLogger(LabelServiceImpl.class);
	
	@Resource
	private COC_HibernateDAO dao;

	@Override
	@SystemServiceLog(description = "新增或更新集合中的全部信息")
	public void saveOrUpdateAll(List<YSystemconfigurationMessagerecord> messagerecords) {
		// TODO Auto-generated method stub
		dao.saveOrUpdateAll(messagerecords);
	}
	
}
