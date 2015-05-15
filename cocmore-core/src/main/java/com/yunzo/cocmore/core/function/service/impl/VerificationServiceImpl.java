package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.FSmsSendrecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicVerification;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.VerificationServiceI;
import com.yunzo.cocmore.core.function.util.RandomArray;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.utils.base.IMUtils;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.base.SMSUtils;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;

/**
 * Description: <验证码服务实现类>. <br>
 * @date:2014年12月17日 上午3:53:59
 * @author beck
 * @version V1.0
 */
@Service("vfService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class VerificationServiceImpl implements VerificationServiceI{
	private static final Logger logger = Logger
			.getLogger(VerificationServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	private static final int RANDOM_NUMBER = 4;	//验证码生成位数
	
	/**
	 * 发送短信，保存验证码
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "发送短信，保存验证码")
	public void getSMSAuthcode(String userName) {
		// TODO Auto-generated method stub
		//获取4位验证码
		String rondom = RandomArray.getRandom(RANDOM_NUMBER);
		String msgText = "您的验证码是:" + rondom + "，有效时间为30分钟。";
		try {
			SMSUtils.sendSMS(EncryptionForTellPhone.decryptByABC(userName), msgText);
			YBasicVerification obj = new YBasicVerification();

			obj.setFid(UUID.randomUUID().toString());
			obj.setFuserPhone(userName);
			obj.setFverification(rondom);
			
			//计算出失效时间
			long time = System.currentTimeMillis();	
			long loseDate = time + 1000 * 60 * 30;
			obj.setFloseDate(new Date(loseDate));
			dao.save(obj);
			
			//保存短信发送记录
			FSmsSendrecord sms = new FSmsSendrecord();
			
			//获取对应会员信息
			List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",userName);
			if(members != null && members.size() > 0){
				YBasicMember member = members.get(0);
				sms.setYBasicMember(member);
				sms.setYBasicSocialgroups(member.getYBasicSocialgroups());
			}
			sms.setFmobilePhone(userName);
			sms.setFcontent(msgText);
			sms.setFbillState(0);
			sms.setFsendTime(new Date(time));
			dao.save(sms);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 短信验证码找回登陆密码 
	 * @param demo
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "短信验证码找回登陆密码")
	public Map<String, Object> findPwdBySMS(String userName,String password,String phoneCheckCode){

		Map<String, Object> value = new HashMap<String, Object>();
		try {
			List<YBasicVerification> list = (List<YBasicVerification>) dao.find("from YBasicVerification y where y.fuserPhone = ? and y.fverification = ? order by y.floseDate desc", userName,phoneCheckCode);
			if(list != null && list.size() > 0){
				YBasicVerification obj = list.get(0);
				if(System.currentTimeMillis() > obj.getFloseDate().getTime()){
					value.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					value.put(ResponseCode.MSGM.msg(), "验证码已经失效，请重新获取");
				}else{
					List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",userName);
					if(members != null && members.size() > 0){
						for (int i = 0; i < members.size(); i++) {
							YBasicMember member = members.get(i);
							member.setFpassword(password);
							dao.update(member);
							dao.flush();
						}
						value.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
						value.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					}	
				}
			}else{
				value.put(ResponseCode.MSGC.msg(), ResponseCode.CODETIMEOUT.value());
				value.put(ResponseCode.MSGM.msg(), ResponseCode.CODETIMEOUT.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 获取注册或短信找回密码短信验证码 
	 * @param demo
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "获取注册或短信找回密码短信验证码")
	public Map<String, Object> getPhoneRegSMSCode(String mid,String userName){
		Map<String, Object> map = new HashMap<String, Object>();
		//判断是否已经存在该账号
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",userName);
		if(members != null && members.size() > 0){
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), "该账号已经存在！");
			return map;
		}
		//获取4位验证码
		String rondom = RandomArray.getRandom(RANDOM_NUMBER);
		String msgText = "您的验证码是:" + rondom + "，有效时间为30分钟。";
		try {
			//解密电话号码并发送短信
			SMSUtils.sendSMS(EncryptionForTellPhone.decryptByABC(userName), msgText);
			YBasicVerification obj = new YBasicVerification();

			obj.setFid(UUID.randomUUID().toString());
			obj.setFuserPhone(userName);
			obj.setFverification(rondom);
			
			//计算出失效时间
			long time = System.currentTimeMillis();	
			long loseDate = time + 1000 * 60 * 30;
			obj.setFloseDate(new Date(loseDate));
			dao.save(obj);
			
			//保存短信发送记录
			FSmsSendrecord sms = new FSmsSendrecord();

			sms.setYBasicMember(null);
			sms.setYBasicSocialgroups(null);
			sms.setFmobilePhone(userName);
			sms.setFcontent(msgText);
			sms.setFbillState(0);
			sms.setFsendTime(new Date(time));
			dao.save(sms);
			
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return map;
	}
}
