package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDynamicInfoPush;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLifePushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.InformationviewServiceI;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.thread.LoginUserGroupPushThread;
import com.yunzo.cocmore.core.thread.LoginUserPushThread;
/**
 * 
 * @author ailierke
 *通知信息推送表
 */
@Service(value="dynmicandInfoPushService")
@Transactional
public class DynmicandInfoPushServiceImpl implements DynmicandInfoPushService {
	
private static final Logger logger = Logger.getLogger(DynmicandInfoPushServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	@Resource
	private DemandSupplyComentPushService demandSupplyComentPushService;	//供需评论推送service
	@Resource
	private DemandsupplyPushinfoServiceI dspService;	//发现供需推送service
	@Resource
	private LifePushinfoSerciveI lifePushService;	//生活推送service
	@Resource
	private InformationviewServiceI InformationviewService; // 通知视图的接口
	@Override
	public void savePushInfo(YBasicDynamicInfoPush pushInfo){
		logger.info("save pushinfo");
		dao.save(pushInfo);
	}
	@Override
	public void upatePushInfo(YBasicDynamicInfoPush pushInfo){
		logger.info("update pushinfo");
		dao.update(pushInfo);
	}
	
	@Override
	public void saveConfigurationMessage(YSystemconfigurationMessage message){
		dao.save(message);
	}
	
	/**
	 * 未读消息
	 * @return
	 */
	public int unreadDynmicandMsg(String tel){
		String hql = "from YBasicDynamicInfoPush y where y.statu = 22 and y.tel = '" + tel + "'";
		List<YBasicDynamicInfoPush> list  = (List<YBasicDynamicInfoPush>) dao.find(hql);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	
	/**
	 * 根据商会ID返回动态通知的未读消息数量
	 * @return
	 */
	public int unreadDynmicandMsgByGroupId(String tel,String groupId){
		String hql = "from YBasicDynamicInfoPush y where y.statu = 22 and y.groupId = '" +groupId + "' and y.tel = '" + tel + "'";
		List<YBasicDynamicInfoPush> list  = (List<YBasicDynamicInfoPush>) dao.find(hql);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public YBasicDynamicInfoPush findDynamicInfoPush(String fid, String mid) {
		String hql = "from YBasicDynamicInfoPush y where y.tel = '"+mid+"' and y.dynamicinformid = '"+fid+"'";
		List<YBasicDynamicInfoPush> list = (List<YBasicDynamicInfoPush>) dao.findAllByHQL(hql);
		YBasicDynamicInfoPush dynamicInfoPush = null;
		if(list != null && list.size() > 0){
			dynamicInfoPush = list.get(0);
		}
		return dynamicInfoPush;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void update(YBasicDynamicInfoPush dynamicInfoPush) {
		dao.update(dynamicInfoPush);
	}
	
	@Override
	public List<YAppdevice>  push( String fmobilePhone) throws Exception {
		/**
		 * 
		 * 用户所在商会未读消息
		 */
		List<YAppdevice> devices = (List<YAppdevice>) dao.find("from YAppdevice y where y.fuserName = ?", fmobilePhone);
		return devices;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	@Override
	public boolean findRead(String fid, String tel) throws Exception {
		boolean flag = true;
		String hql = "from YBasicDynamicInfoPush pushInfo where pushInfo.dynamicinformid='"+fid+"' and pushInfo.tel='"+tel+"'";
		List<YBasicDynamicInfoPush> pushInfoList = (List<YBasicDynamicInfoPush>) dao.findAllByHQL(hql);
		if(pushInfoList!=null&&pushInfoList.size()>0){
			Integer ststus = pushInfoList.get(0).getStatu();
			if(ststus==Status.UNREAD.value()){
				flag = false;
			}else{
				flag = true;
			}
		}
		return flag;
	}
	
}
