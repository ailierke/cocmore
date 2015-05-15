package com.yunzo.cocmore.core.function.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLifePushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;

/**
 * Description: <生活推送记录接口实现类>. <br>
 * @date:2015年3月18日 下午12:09:54
 * @author beck
 * @version V1.0
 */
@Service("lifePushService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class LifePushinfoSerciveImpl implements LifePushinfoSerciveI{
	
	private static final Logger logger = Logger.getLogger(LifePushinfoSerciveImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	

	@Override
	public void save(YBasicLifePushinfo demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	public void update(YBasicLifePushinfo demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}
	
	@Override
	public void saveConfigurationMessage(YSystemconfigurationMessage message)
			throws Exception {
		// TODO Auto-generated method stub
		dao.save(message);
	}

	@Override
	public List<YBasicLifePushinfo> selectLifePush() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public YBasicLifePushinfo findLifePushinfo(String fid, String mid) {
		String hql = "from YBasicLifePushinfo y where y.ftel = '"+mid+"' and y.flifeId = '"+fid+"'";
		List<YBasicLifePushinfo> list = (List<YBasicLifePushinfo>) dao.findAllByHQL(hql);
		YBasicLifePushinfo lifePushinfo = null;
		if(list != null && list.size() > 0){
			lifePushinfo = list.get(0);
		}
		return lifePushinfo;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<String> getTels() {
		//查询设备表所有电话号码
		String sql = "select fuserName from y_appdevice where FUserName is not null and FDeviceId is NOT NULL group by FDeviceId";
		List<String> phones = (List<String>) dao.getListBySql(sql);
		return new HashSet<String>(phones);
	}
	
	/**
	 * 生活推送未读消息
	 * @return
	 */
	public int unreadLifeMsg(String tel){
		String hql = "from YBasicLifePushinfo y where y.fstatu = 22 and y.ftel = '" + tel + "'";
		List<YBasicLifePushinfo> list  = (List<YBasicLifePushinfo>) dao.find(hql);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	
	/**
	 * 根据电话查询该项目推送是否已读
	 * @return
	 */
	public int isReadByTel(String tel,String lifeId){
		String hql = "from YBasicLifePushinfo y where y.ftel = ? and y.flifeId = ? and y.fstatu = 22";
		List<YBasicLifePushinfo> list  = (List<YBasicLifePushinfo>) dao.find(hql,tel,lifeId);
		if(list!=null&&list.size()>0){
			return 1;
		}
		return 0;
	}


}
