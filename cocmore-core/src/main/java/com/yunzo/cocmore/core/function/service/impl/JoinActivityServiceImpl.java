package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.FAwardSetting;
import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.service.JoinActivityServiceI;

/**
 * @author：jackpeng
 * @date：2014年12月27日下午2:23:13
 * 上墙活动参与人service实现类
 */
@Service("joinActivityService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class JoinActivityServiceImpl implements JoinActivityServiceI {
	
	private static final Logger logger = Logger.getLogger(JoinActivityServiceImpl.class);
	
	@Resource
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "根据指定人id查询参与人")
	public List<YBasicJoinActivity> getById(String settingId) {
		logger.info("YBasicJoinActivity findAll(String settingId)");
		YBasicJoinActivity joinActivity = null;
		List<YBasicJoinActivity> list = new ArrayList<YBasicJoinActivity>();
		//根据id查询抽奖设置
 		FAwardSetting warSetting = getSettingId(settingId);
		//获取抽奖设置的指定人
		String designatedPerson = warSetting.getFdesignatedPerson();
		//拆分指定人
		String[] designatedPersonStr = designatedPerson.split(",");
		for(String designatedPersonId: designatedPersonStr){
			joinActivity = (YBasicJoinActivity) dao.findById(YBasicJoinActivity.class, designatedPersonId);
			list.add(joinActivity);
		}
		return list;
	}
	
	/**
	 * 根据抽奖设置id查询抽奖设置
	 * @param settingId
	 * @return
	 */
	@SystemServiceLog(description = "根据抽奖设置id查询抽奖设置")
	public FAwardSetting getSettingId(String settingId){
		FAwardSetting warSetting = (FAwardSetting) dao.findById(FAwardSetting.class, settingId);
		return warSetting;
	}
	
}
