package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.FAwardsRecord;
import com.yunzo.cocmore.core.function.service.RecordServiceI;

/**
 * @author：jackpeng
 * @date：2014年12月22日下午4:07:44
 * 抽奖记录service实现类
 */
@Service("recordService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class RecordServiceImpl implements RecordServiceI {
	
	private static final Logger logger = Logger.getLogger(RecordServiceImpl.class);
	
	@Resource
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据上墙主题id查询是否存在抽奖记录")
	public Boolean getByhql(String themeId) {
		logger.info("Boolean getByhql(String themeId)");
		String hql = null;
		List<FAwardsRecord> list = null;
		try {
			hql = "from FAwardsRecord y where y.fthemeId = '"+themeId+"'";
			list = (List<FAwardsRecord>) dao.findAllByHQL(hql);
			if(list != null && list.size() > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 根据上墙主题id查询抽奖记录信息
	 * @param hql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据上墙主题id查询抽奖记录")
	public List<FAwardsRecord> getByThemeId(String themeId){
		logger.info("getByhql(String themeId)");
		String hql = null;
		List<FAwardsRecord> list = null;
		try {
			hql = "from FAwardsRecord y where y.fthemeId = '"+themeId+"'";
			list = (List<FAwardsRecord>) dao.findAllByHQL(hql);
			if(list != null && list.size() > 0){
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
