package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.FSmsSendrecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YManshowinformation;
import com.yunzo.cocmore.core.function.service.SmsSendServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.DateUtil;
import com.yunzo.cocmore.utils.base.SMSUtils;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;

/** 
 *Description: <短信发送服务实现类>. <br>
 * @date:2014年11月26日 下午3:52:32
 * @author beck
 * @version V1.0                             
 */
@Service("smsService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class SmsSendServiceImpl implements SmsSendServiceI{
	private static final Logger logger = Logger
			.getLogger(SmsSendServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部短信发送")
	public List<FSmsSendrecord> findAll() {
		logger.info("List<FSmsSendrecord> findAll()");
		return (List<FSmsSendrecord>)dao.findAll(FSmsSendrecord.class);
	}

	@Override
	@SystemServiceLog(description = "根据id查询短信发送")
	public FSmsSendrecord getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("FSmsSendrecord getById(String fid) || id==" + fid);
		return (FSmsSendrecord) dao.findById(FSmsSendrecord.class, fid);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据手机号、姓名、状态查询短信发送")
	public List<FSmsSendrecord> findAllByPhoneNameStatus(String hql) {
		// TODO Auto-generated method stub
		return (List<FSmsSendrecord>)dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "查询全部短信发送，并分页")
	public PagingList<FSmsSendrecord> getAllControllerPagingList(Integer start,
			Integer limit, String groupId, String headline) {
		logger.info("PagingList<FSmsSendrecord> getAllControllerPagingList");
		
		
		PagingList<FSmsSendrecord> pagingList = new PagingList<FSmsSendrecord>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from FSmsSendrecord y where 1=1 ");
		StringBuffer hqlCount = new StringBuffer("select count(0) from FSmsSendrecord y where 1=1 ");
		/**
		 * 判断是否通过groupId或content
		 */
//		if(groupId != null){
			hqlList.append("and y.YBasicSocialgroups.fid=?");
			hqlCount.append("and y.YBasicSocialgroups.fid=?");
			values.add(groupId);
//		}
		if(headline != null){
			hqlList.append(" and y.YBasicMember.fname like '%"+headline+"%'");
			hqlCount.append(" and y.YBasicMember.fname like '%"+headline+"%'");
		}
		logger.info("************************************************"+hqlList.toString());
		try {
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<FSmsSendrecord>)dao.find(hqlList.toString(), start, limit,values.toArray()));
			
			/**
			 * 获得总条数
			 */
			pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), start, limit, values.toArray()));
			logger.info("总条数："+pagingList.getCount());
			logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pagingList;
	}
	
	/**
	 * 发送短信
	 * @param mobiles
	 * @param msgText
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean send(String fids,String msgText,String dateString){
		int num = 50;			//每次群发的数量
		int record = 1;			//记录群发的次数
		int count = num * record;	//群发总数
		//mobiles
		StringBuffer sb = new StringBuffer();
		//保存短信发送记录
		FSmsSendrecord sms = null;
		
		try {
			if(fids != null && !fids.equals("")){
				String[] array = fids.split(",");
				for (int i = 0; i < array.length; i++) {
					//满足定义的群发数量就发送一次
					if(i == count){
						String ms = sb.substring(0, sb.length() - 1).toString();
						System.out.println(SMSUtils.sendSMS(ms, msgText));
//						String[] temp = ms.split(",");
//						System.out.println(temp.length);
				
						record++;
						count = num * record;
						sb = new StringBuffer();
					}	
					YBasicMember obj =  (YBasicMember) dao.get(YBasicMember.class, array[i]);
					sb.append(EncryptionForTellPhone.decryptByABC(obj.getFmobilePhone()) + ",");
					sms = new FSmsSendrecord();
					sms.setFcontent(msgText);
					sms.setFmobilePhone(obj.getFmobilePhone());
					sms.setFsendTime(DateUtil.StringToDate(dateString, "yyyy-MM-dd HH:mm:ss"));
					sms.setYBasicMember(obj);
					sms.setYBasicSocialgroups(obj.getYBasicSocialgroups());
					sms.setFbillState(19);
					dao.save(sms);
				}
				//群发剩下的数量
				if(sb.indexOf(",") != -1){
					String ms = sb.substring(0, sb.length() - 1).toString();
					System.out.println(SMSUtils.sendSMS(ms, msgText));
//					String[] temp = ms.split(",");
//					System.out.println(temp.length);
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}
