package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplycmentPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
/**
 * 
 * @author ailierke
 *通知信息推送表
 */
@Service(value="demandSupplyComentPushService")
@Transactional
public class DemandSupplycomentPushServiceImpl implements DemandSupplyComentPushService {
	
private static final Logger logger = Logger.getLogger(DemandSupplycomentPushServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	public void upatePushInfo(YBasicDemandsupplycmentPushinfo commentPushInfo)
			throws Exception {
		logger.info("upate  PushInfo");
		dao.update(commentPushInfo);
	}
	
	@Override
	public YBasicDemandsupplycmentPushinfo findPushInfo(String  pushInfoId)
			throws Exception {
		YBasicDemandsupplycmentPushinfo info = (YBasicDemandsupplycmentPushinfo) dao.findById(YBasicDemandsupplycmentPushinfo.class, pushInfoId);
			return info;
	}

	@Override
	public void savePushInfo(YBasicDemandsupplycmentPushinfo commentPushInfo)
			throws Exception {
		logger.info("save  PushInfo");
		dao.save(commentPushInfo);
	}

	@Override
	public void saveConfigurationMessage(YSystemconfigurationMessage message)
			throws Exception {
		logger.info("save  message");
		dao.save(message);
	}
	
	/**
	 * 未读消息
	 * @return
	 */
	public int unreadDemandSupplyComentMsg(String tel){
		String hql = "from YBasicDemandsupplycmentPushinfo y where y.fstatu = 22 and y.fmobilePhone = '" + tel + "'";
		List<YBasicDemandsupplycmentPushinfo> list  = (List<YBasicDemandsupplycmentPushinfo>) dao.find(hql);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<YBasicDemandsupplycmentPushinfo> findByHql(String mid) {
		String hql = "from YBasicDemandsupplycmentPushinfo y where y.fmobilePhone = '"+mid+"'";
		List<YBasicDemandsupplycmentPushinfo> list = (List<YBasicDemandsupplycmentPushinfo>) dao.findAllByHQL(hql);
		return list;
	}

	@Override
	public void update(YBasicDemandsupplycmentPushinfo demandsupplycmentPushinfo) {
		// TODO Auto-generated method stub
		dao.update(demandsupplycmentPushinfo);
	}
	

}
