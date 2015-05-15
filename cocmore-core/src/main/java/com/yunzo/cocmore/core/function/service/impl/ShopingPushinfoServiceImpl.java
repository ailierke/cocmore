package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplycmentPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDynamicInfoPush;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLifePushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicShopingPushinfo;
import com.yunzo.cocmore.core.function.service.ShopingPushinfoServiceI;

/**
 * @author：jackpeng
 * @date：2015年3月27日下午5:56:31
 * 预约推送信息记录实现类
 */
@Service("shopingPushinfoService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class ShopingPushinfoServiceImpl implements ShopingPushinfoServiceI {

	private static final Logger logger = Logger.getLogger(LabelServiceImpl.class);
	
	@Resource
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<YBasicShopingPushinfo> findShopinPhone(String mid) {
		String hql = "from YBasicShopingPushinfo y where y.ftel = '"+mid+"'";
		List<YBasicShopingPushinfo> list = (List<YBasicShopingPushinfo>) dao.findAllByHQL(hql);
		return list;
	}

	@Override
	public void update(YBasicShopingPushinfo shopingPushinfo) {
		// TODO Auto-generated method stub
		logger.info("update YBasicShopingPushinfo");
		dao.update(shopingPushinfo);
	}

	@Override
	public void save(YBasicShopingPushinfo shopingPushinfo) {
		// TODO Auto-generated method stub
		logger.info("save YBasicShopingPushinfo");
		dao.save(shopingPushinfo);
	}

	/**
	 * 未读消息
	 * @param tel
	 * @return
	 */
	public int unreadOrderMsg(String tel){
		String hql = "from YBasicShopingPushinfo y where y.fstatu = 22 and y.ftel = '" + tel + "'";
		List<YBasicShopingPushinfo> list  = (List<YBasicShopingPushinfo>) dao.find(hql);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		return 0;
	}
	
}
