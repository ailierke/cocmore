package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;

/**
 * @author：jackpeng
 * @date：2015年3月18日下午3:20:56
 * 供需推送信息记录service实现
 */
@Service("dspService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class DemandsupplyPushinfoServiceImpl implements DemandsupplyPushinfoServiceI {
	
	private static final Logger logger = Logger.getLogger(DistrictServiceImpl.class);

	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	public void save(YBasicDemandsupplyPushinfo bdp) {
		logger.info("save YBasicDemandsupplyPushinfo");
		dao.save(bdp);
	}

	@Override
	public void saveConfigurationMessage(YSystemconfigurationMessage message)
			throws Exception {
		// TODO Auto-generated method stub
		dao.save(message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public YBasicDemandsupplyPushinfo findDemandsupplyPushinfo(String fid,String mid) {
		String hql = "from YBasicDemandsupplyPushinfo y where y.fdemandsupplyId = '"+fid+"' and y.ftel = '"+mid+"'";
		List<YBasicDemandsupplyPushinfo> list = (List<YBasicDemandsupplyPushinfo>) dao.findAllByHQL(hql);
		YBasicDemandsupplyPushinfo dsp = null;
		if(list != null && list.size() > 0){
			dsp = list.get(0);
		}
		return dsp;
	}

	@Override
	public void update(YBasicDemandsupplyPushinfo dsp) {
		// TODO Auto-generated method stub
		dao.update(dsp);
	}
	
	/**
	 * 未读消息
	 * @return
	 */
	public int unreadDemandSupplyMsg(){
		String hql = "from YBasicDemandsupplyPushinfo y where y.fstatu = 22";
		List<YBasicDemandsupplyPushinfo> list  = (List<YBasicDemandsupplyPushinfo>) dao.find(hql);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	
}
