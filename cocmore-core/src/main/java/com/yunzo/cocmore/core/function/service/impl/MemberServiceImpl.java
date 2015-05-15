package com.yunzo.cocmore.core.function.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinformrecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicVerification;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationLog;
import com.yunzo.cocmore.core.function.model.mysql.YVisitorsRecordNotlogin;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
import com.yunzo.cocmore.core.function.service.DemandsupplyPushinfoServiceI;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.InformationviewServiceI;
import com.yunzo.cocmore.core.function.service.LifePushinfoSerciveI;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.MemberdistributionServiceI;
import com.yunzo.cocmore.core.function.service.ShopingPushinfoServiceI;
import com.yunzo.cocmore.core.function.util.LabelXMLToObject;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.RandomArray;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.thread.LoginIsUpdatePushThread;
import com.yunzo.cocmore.core.thread.LoginUserGroupPushThread;
import com.yunzo.cocmore.core.thread.LoginUserPushThread;
import com.yunzo.cocmore.utils.base.CRC32Util;
import com.yunzo.cocmore.utils.base.GetAdressByTel;
import com.yunzo.cocmore.utils.base.IMUtils;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.base.DateUtil;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;
import com.yunzo.cocmore.utils.number.EncryptionForTellPhone;

/** 
 *Description: <会员服务实现类>. <br>
 * @date:2014年11月26日 下午1:34:25
 * @author beck
 * @version V1.0                             
 */
@Service("memberService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class MemberServiceImpl implements MemberServiceI {
	private static final Logger logger = Logger
			.getLogger(MemberServiceImpl.class);

	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Resource(name = "memdisService")
	private MemberdistributionServiceI memdisService;
	
	@Resource
	private DemandSupplyComentPushService demandSupplyComentPushService;	//供需评论推送service
	@Resource
	private DynmicandInfoPushService dynmicandInfoPushService;	//商会动态推送service
	@Resource
	private DemandsupplyPushinfoServiceI dspService;	//发现供需推送service
	@Resource
	private LifePushinfoSerciveI lifePushService;	//生活推送service
	@Resource
	private InformationviewServiceI InformationviewService; // 通知视图的接口
	
	@Resource
	private ShopingPushinfoServiceI shopingPushinfoService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	


	private static final int RANDOM_NUMBER = 6;	//会员临时密码生成位数

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部会员，并分页")
	public PagingList<YBasicMember> findAll(String searchName,Integer start,Integer limit,String groupId) {
		logger.info("List<YBasicMember> findAll()");
		PagingList<YBasicMember> pagingList = new PagingList<YBasicMember>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YBasicMember>)dao.findAll(YBasicMember.class));
			return pagingList;
		}

		String hql = "from YBasicMember y where 1=1 and y.fbillState!=9 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fname like '%" + searchName + "%'";
		}

		hql+="and y.YBasicSocialgroups.fid = '"+groupId + "'";

		pagingList.setList((List<YBasicMember>) dao.find(hql, start, limit, null));

		//获取总条数
		List<YBasicMember> list = (List<YBasicMember>)dao.find(hql);
		pagingList.setCount(list.size());

//		logger.info("总条数："+pagingList.getCount());
//		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询会员")
	public YBasicMember getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicMember getById(String fid) || id==" + fid);
		return (YBasicMember) dao.findById(YBasicMember.class, fid);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean isMember(String telphone) {
		boolean flag = true;
		List<YBasicMember> list = (List<YBasicMember>)dao.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+telphone+"' and ymember.YBasicSocialgroups.fid is null");
		if(list!=null&&list.size()==1){
			if(list.get(0).getYBasicSocialgroups()==null){
				flag = false;
			}
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "新增会员")
	public void save(YBasicMember demo) {
		//获取当前最新版本号
		int maxVersion = memdisService.getMaxVersion(demo.getYBasicSocialgroups().getFid()) + 1;
		Set<YBasicentriesMemberdistribution> memDis = null;
		String[] str = demo.getJsonStr().split("}");
		String phone = EncryptionForTellPhone.encryptToABC(demo.getFmobilePhone());		//加密电话
		//设置ID和管理员属性，手机号作为管理员的账号
		demo.setFid(UUID.randomUUID().toString());
		demo.setFpassword(MD5Util.md5(BEGINSTRING + "888888"));
		if(demo.getIsAdmin() == 1){
			YSystemUsers user = new YSystemUsers(UUID.randomUUID().toString(), demo.getFmobilePhone(), MD5Util.md5(BEGINSTRING + "888888"), 5, "0");
			dao.save(user);
			demo.setYSystemUsers(user);
		}else{
			demo.setYSystemUsers(null);
		}

		if(!demo.getJsonStr().equals("")){
			try {
				while(demo.getJsonStr().indexOf("undefined") != -1){
					String fid = UUID.randomUUID().toString();
					String newJsonStr = demo.getJsonStr().replaceFirst("undefined", fid);
					demo.setJsonStr(newJsonStr);
				}
				List<YBasicentriesMemberdistribution> list = JSON.parseArray(demo.getJsonStr(), YBasicentriesMemberdistribution.class);
				memDis = new HashSet<YBasicentriesMemberdistribution>();
				for (int i = 0; i < list.size(); i++) {
					YBasicentriesMemberdistribution obj = list.get(i);
					String[] array = str[i].split(",");
					for(String temp : array){
						String[] field = temp.split(":");
						if(field[0].indexOf("yBasicPosition.fid") != -1){
							YBasicPosition pos = (YBasicPosition) dao.get(YBasicPosition.class, field[1].replaceAll("\"",""));
							dao.updateBySQL("update y_basic_position y set y.FVersion = " + (maxVersion + i) + " where y.FID = '" + pos.getFid() + "'");
							obj.setYBasicPosition(pos);
						}
					}				
					obj.setYBasicMember(demo);
					obj.setFversion(maxVersion + i);
					memDis.add(obj);
				}
				demo.setYBasicentriesMemberdistributions(memDis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * update by ailierke
		 */
		//如果这个电话号码所存在的会员部存在，就生成IM账号，如果存在就跳过
		List<YBasicMember> memberList =null;
		try{
			memberList = (List<YBasicMember>) dao.findAllByHQL("from YBasicMember m where m.fmobilePhone = '"+phone+"'");
		
			//保存生成的IM账号
			YBasicImaccount im = new YBasicImaccount();
			if(memberList!=null&&memberList.size()>0){
				List<YBasicImaccount> counts = (List<YBasicImaccount>) dao.findAllByHQL("from YBasicImaccount imaccount where imaccount.fimtel ='"+phone+"'");
				if(counts!=null&&counts.size()>0){
					im = counts.get(0);
				}else{
					Map<String,String> userMap = new HashMap<String,String>();
					userMap.put("username", CRC32Util.getCRC32(demo.getFmobilePhone()));
					userMap.put("password", MD5Util.md5("888888"));//默认密码888888
					IMUtils.createUser(userMap);//新增
					im.setFimkey(CRC32Util.getCRC32(demo.getFmobilePhone()));
					im.setFimpassword(MD5Util.md5("888888"));
					im.setFimtel(phone);
					dao.save(im);
				}
				
			}else{
				Map<String,String> userMap = new HashMap<String,String>();
				userMap.put("username", CRC32Util.getCRC32(demo.getFmobilePhone()));
				userMap.put("password", MD5Util.md5("888888"));//默认密码888888
				IMUtils.createUser(userMap);//新增
				im.setFimkey(CRC32Util.getCRC32(demo.getFmobilePhone()));
				im.setFimpassword(MD5Util.md5("888888"));
				im.setFimtel(phone);
				dao.save(im);
			}
			//电话号码加密
			demo.setFmobilePhone(phone);
			dao.save(demo);
			
			//获取默认的十个标签
			List<YBasicLabel> list = LabelXMLToObject.labelObject(demo.getFmobilePhone());
			dao.saveOrUpdateAll(list);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	@SystemServiceLog(description = "删除会员")
	public void delete(YBasicMember demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	@SystemServiceLog(description = "修改会员")
	public void update(YBasicMember demo) {
		//获取当前最新版本号
		int maxVersion = memdisService.getMaxVersion(demo.getYBasicSocialgroups().getFid()) + 1;
		Set<YBasicentriesMemberdistribution> memDis = null;
		List<YBasicentriesMemberdistribution> oldMemDis = findMDByMemID(demo.getFid());
		String phone = EncryptionForTellPhone.encryptToABC(demo.getFmobilePhone());		//加密电话
		String[] str = demo.getJsonStr().split("}");
		while(demo.getJsonStr().indexOf("undefined") != -1){
			String fid = UUID.randomUUID().toString();
			String newJsonStr = demo.getJsonStr().replaceFirst("undefined", fid);
			demo.setJsonStr(newJsonStr);
		}

		if(!demo.getJsonStr().equals("[")){//update by ailierke
			if(!demo.getJsonStr().equals("")){
				try {
					List<YBasicentriesMemberdistribution> list = JSON.parseArray(demo.getJsonStr(), YBasicentriesMemberdistribution.class);
					memDis = new HashSet<YBasicentriesMemberdistribution>();
					for (int i = 0; i < list.size(); i++) {
						YBasicentriesMemberdistribution obj = list.get(i);
						String[] array = str[i].split(",");
						for(String temp : array){
							String[] field = temp.split(":");
							if(field[0].indexOf("yBasicPosition.fid") != -1){
								YBasicPosition pos = (YBasicPosition) dao.get(YBasicPosition.class, field[1].replaceAll("\"",""));
								dao.updateBySQL("update y_basic_position y set y.FVersion = " + (maxVersion + i) + " where y.FID = '" + pos.getFid() + "'");
								obj.setYBasicPosition(pos);
								
							}
						}
						obj.setFversion(maxVersion + i);
						obj.setYBasicMember(demo);
						memDis.add(obj);
					}
					//循环删除前台选中删除的条款记录
					for(YBasicentriesMemberdistribution cla : oldMemDis){
						boolean b = true;
						for(YBasicentriesMemberdistribution clas : list){
							if(cla.getFid().equals(clas.getFid())){
								b = false;
								break;
							}
						}
						if(b){
							dao.updateBySQL("delete from y_basicentries_memberdistribution where FID='" + cla.getFid() +"'");
						}
					}
					demo.setYBasicentriesMemberdistributions(memDis);
					demo.setFmobilePhone(phone);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//更新版本号
		List<YBasicentriesMemberdistribution> list = findMDByMemID(demo.getFid());
		YBasicentriesMemberdistribution obj = null;
		YBasicPosition pn = null;
		if(list != null && list.size() >0){
			for (int i = 0; i < list.size(); i++) {
				obj = list.get(i);
				obj.setFversion(maxVersion + i);
				
				//同时更新职位表版本
				pn = obj.getYBasicPosition();
				if(pn != null){
					pn.setVersion(maxVersion + i);
					dao.update(pn);
					dao.flush();
				}
				dao.update(obj);
				dao.flush();
			}
		}
		dao.update(demo);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询全部会员")
	public List<YBasicMember> findAllBySql(String sql) {
		logger.info("List<YBasicIndustry> findAllBySql()");
		return (List<YBasicMember>)dao.getListBySql(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询会员")
	public List<YBasicMember> findByHql(String hql) {
		return (List<YBasicMember>)dao.findAllByHQL(hql);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询返回List<String>")
	public List<String> getByHql(String hql){
		return (List<String>) dao.findAllByHQL(hql);
	}

	/**
	 * 修改状态
	 * @param demo
	 */
	@SystemServiceLog(description = "修改会员状态")
	public void updateStatus(YBasicMember demo){
		//获取当前最新版本号
		int maxVersion = memdisService.getMaxVersion(demo.getYBasicSocialgroups().getFid()) + 1;
		//更新版本号
		List<YBasicentriesMemberdistribution> list = findMDByMemID(demo.getFid());
		YBasicentriesMemberdistribution obj = null;
		YBasicPosition pn = null;
		if(list != null && list.size() >0){
			for (int i = 0; i < list.size(); i++) {
				obj = list.get(i);
				obj.setFversion(maxVersion + i);
				
				//同时更新职位表版本
				pn = obj.getYBasicPosition();
				if(pn != null){
					pn.setVersion(maxVersion + i);
					dao.update(pn);
					dao.flush();
				}
				dao.update(obj);
				dao.flush();
			}
		}
		
		dao.update(demo);
	}

	/**
	 * 根据会员ID查询会员职位分配的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据会员id查询职员职位分配信息")
	public List<YBasicentriesMemberdistribution> findMDByMemID(String fid){
		List<YBasicentriesMemberdistribution> list = null;
		list = (List<YBasicentriesMemberdistribution>) dao.findAllByHQL("from YBasicentriesMemberdistribution y where y.YBasicMember.fid = '" + fid + "'");
		return list;
	}

	/**
	 * 登陆用户电话号码查询
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据登陆用户电话号码查询")
	public  YBasicMember getByMobilePhone(String fmobilePhone){
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",fmobilePhone);
		if(members.size() > 0){
			return members.get(0);
		}else{
			return null;
		}
	}

	/**
	 * APP登录
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> login(Map<String, Object> map){
		//设置返回结果
		Map<String, Object> value = new HashMap<String, Object>();

		//设置返回信息
		Map<String, Object> objList = new HashMap<String, Object>();
		try {
			
		
		String fmobilePhone = (String) map.get("userName");
		String password = (String) map.get("password");
//		String businessId = (String) map.get("businessId");
		YBasicMember member = null;
		YAppdevice device = null;
		String tempPwd = "";

		boolean flag;
		boolean flagGroup = false;
		
		//验证登录用户名和密码
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fbillState = 5 and y.fmobilePhone = ? and y.fpassword = ?",fmobilePhone,password);
		if(members!=null&&members.size() > 0){
			// 循环判断团体是否已经失效
			for (int i = 0; i < members.size(); i++) {
				member = members.get(i);
				//是否是未加入商会的会员
				if(member.getYBasicSocialgroups() != null){
					if(member.getYBasicSocialgroups().getFbillState() == 6 || member.getYBasicSocialgroups().getFbillState() == 9){
						continue;
					}else{
						flagGroup = true;
						break;
					}
				}else{
					flagGroup = true;
					break;
				}
			}
			
			
			
			if(!flagGroup){
				value.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				value.put(ResponseCode.MSGM.msg(), "该团体已经失效或者作废");
				return value;
			}
			
			//获取随机数，更新临时随机密码
			tempPwd = MD5Util.md5(RandomArray.getRandom(RANDOM_NUMBER));
			member.setFtempPwd(tempPwd);
			dao.update(member);
		}else{
			value.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			value.put(ResponseCode.MSGM.msg(), "用户名或密码不正确");
			return value;
		}
		
		//更新用户登录设备信息
		List<YAppdevice> ds = (List<YAppdevice>) dao.find("from YAppdevice y where y.fuserName = ?", fmobilePhone);
		if(ds!=null&&ds.size() > 0){
			device = ds.get(0);
			flag = false;
		}else{
			device = new YAppdevice();
			device.setFuserName(fmobilePhone);
			flag = true;
		}
		
		if(fmobilePhone == null || map.get("appChannelNo") == null || map.get("clientID")==null || map.get("clientID")==null){
			YSystemconfigurationLog log = new YSystemconfigurationLog();
			log.setFtype(9);
			log.setFtime(new Date());
			log.setFcontent(map.toString());
			dao.save(log);
		}
		
		device.setFmid((String) map.get("mid"));
		device.setFmd5info(tempPwd);
		
		device.setFdeviceOs((String) map.get("deviceOs"));
		device.setFdeviceOsversion((String) map.get("deviceOsversion"));
		device.setFdeviceType((String) map.get("deviceType"));
		device.setFappVersion((String) map.get("appVersion"));
		//获取渠道号
		String no = (String) map.get("appChannelNo");
		if("0".equals(no)){
			device.setFdeviceId((String) map.get("clientID"));
		}else{
			device.setFdeviceId((String) map.get("cocDevicetoken"));
		}
		device.setFclient((String) map.get("clientID"));
		device.setFappChannelNo(no);
		
		if(members!=null && members.size() > 0){
			device.setYBasicSocialgroups(members.get(0).getYBasicSocialgroups());
		}

		//保存或者更新设备信息
		if(flag){
			dao.save(device);
		}else{
			dao.update(device);
		}
		List<YBasicImaccount> imaccountList =null;
		
			imaccountList = (List<YBasicImaccount>) dao.findAllByHQL("from YBasicImaccount y where y.fimtel ='"+fmobilePhone+"'");
		if(imaccountList!=null&&imaccountList.size()>0){
			objList.put("imUsername", imaccountList.get(0).getFimkey());
			 objList.put("imPassword", imaccountList.get(0).getFimpassword());
		}else{
			objList.put("imUsername", "");
			 objList.put("imPassword", "");
		}
	 
		objList.put("mid", member.getFmobilePhone());
		objList.put("md5Info", tempPwd);
		if(member.getFheadImage()!=null){
			String headLineIMG = member.getFheadImage().replace(",", "");
			objList.put("imageUrl", headLineIMG);
		}
	
		objList.put("nickName", member.getFname());
		objList.put("consigneeAddress", member.getFreceivingAddress());
		objList.put("nativePlace", member.getFnativePlace());
		objList.put("sex", member.getFsex());
		objList.put("birthday", DateUtil.dateToString(member.getFbirthday()));
		if(member.getYBasicSocialgroups() != null){
			objList.put("businessId",member.getYBasicSocialgroups().getFid());
			if( member.getYBasicSocialgroups().getLogo()!=null){
				String logo =member.getYBasicSocialgroups().getLogo().replace(",", "");
				objList.put("imageUrl", logo);
			}
			objList.put("businessLogo", member.getYBasicSocialgroups().getLogo());
			objList.put("businessName", member.getYBasicSocialgroups().getFname());
		}
		//判断是否为原始密码 888888
		if(MD5Util.md5(BEGINSTRING + "888888").equals(password)){
			objList.put("isdefault", 0);
		}else{
			objList.put("isdefault", 1);
		}
		
		//存入记录到登录记录表中
		YVisitorsRecordNotlogin record = new YVisitorsRecordNotlogin();
		record.setFuserID(member.getFid());
		record.setFname(member.getFname());
		record.setFtelphone(member.getFmobilePhone());
		record.setFaccessTime(new Date());
		record.setFlastAccessTime(new Date());
		
		dao.save(record);
		
		//查询用户所有商会
		StringBuffer temp = new StringBuffer();
		for(YBasicMember mo : members){
			temp.append(mo.getYBasicSocialgroups().getFid() + ",");
		}
		String groupIds = temp.substring(0,temp.lastIndexOf(","));
		if(Integer.parseInt(device.getFappChannelNo()) == 0){
			//推动用户未读消息
			LoginUserPushThread userPush = new LoginUserPushThread("未读消息",device.getFdeviceId(),Integer.parseInt(device.getFappChannelNo()),member.getFmobilePhone(),demandSupplyComentPushService,
					dynmicandInfoPushService,dspService,lifePushService,InformationviewService,shopingPushinfoService);
			userPush.start();
			
			//用户所在商会未读消息
			LoginUserGroupPushThread userGroupPush = new LoginUserGroupPushThread(member.getFmobilePhone(),"未读消息", device.getFdeviceId(), groupIds, Integer.parseInt(device.getFappChannelNo()), dynmicandInfoPushService);
			userGroupPush.start();
		}else{
			//推动用户未读消息
			System.out.println(device.getFclient() + "," + Integer.parseInt(device.getFappChannelNo()));
			LoginUserPushThread userPush = new LoginUserPushThread("未读消息",device.getFclient(),Integer.parseInt(device.getFappChannelNo()),member.getFmobilePhone(),demandSupplyComentPushService,
					dynmicandInfoPushService,dspService,lifePushService,InformationviewService,shopingPushinfoService);
			userPush.start();
			
			//用户所在商会未读消息
			
			LoginUserGroupPushThread userGroupPush = new LoginUserGroupPushThread(member.getFmobilePhone(),"未读消息", device.getFclient(), groupIds, Integer.parseInt(device.getFappChannelNo()), dynmicandInfoPushService);
			userGroupPush.start();
		}
		
		if(Integer.parseInt(device.getFappChannelNo()) == 1){
			LoginIsUpdatePushThread isUpdate = new LoginIsUpdatePushThread("是否更新", device.getFclient(), Integer.parseInt(device.getFappChannelNo()), 1);
			isUpdate.start();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
		value.put(ResponseCode.MSGR.msg(), objList);  
		value.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
		value.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "修改密码")
	public boolean updatePassword(String fmobilePhone,String oldPassword,String password){
		String msg = "";
		YBasicMember member = null;
		//List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ? and y.fpassword = ?",fmobilePhone,oldPassword);

		List<YBasicMember> members = (List<YBasicMember>)dao.find("from YBasicMember y where y.fmobilePhone = ? and y.fpassword = ?", fmobilePhone,oldPassword);
		if(members!=null&&members.size() > 0){
			for (int i = 0; i < members.size(); i++) {
				member = members.get(i);
				member.setFpassword(password);
				dao.update(member);
				dao.flush();
			}
			return true;
		}
		return false;
	}

	/**
	 * 游客注册
	 * @return
	 */
	@SystemServiceLog(description = "游客注册")
	public Map<String, Object> saveGust(){
		Map<String, Object> obj = new HashMap<String, Object>();
		YBasicMember member = new YBasicMember();
		member.setFid(UUID.randomUUID().toString());
		member.setFpassword(MD5Util.md5(BEGINSTRING + "888888"));
		obj.put("mid", member.getFid());
		obj.put("md5Info", member.getFpassword());
		dao.save(member);
		return obj;
	}

	/**
	 * 批量查询用户隐私设置通讯录列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "批量查询用户隐私设置通讯录列表")
	public List<Map<String,Object>> getContactsPrivacyList(String businessId){
		List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.YBasicSocialgroups.fid = ?",businessId);
		for(YBasicMember obj : members){
			map = new HashMap<String, Object>();
			map.put("m", obj.getFid());
			map.put("h", obj.getFisHidePhone());
			objList.add(map);
		}

		return objList;
	}

	/**
	 * 查询会员个人信息接口
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "查询会员个人信息")
	public Map<String,Object> getContactsOtherPersonInfo(String userId,String businessId){
		Map<String, Object> map = new HashMap<String, Object>();
		YBasicMember member = null;
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",userId);
		if(members != null && members.size() > 0){
			member = members.get(0);
			map.put("realName", member.getFname());
			String img = member.getFheadImage();
			if(img==null || img.equals("")){
				map.put("serImage", null);
			}else{
				map.put("serImage", img.replaceAll(",",""));
			}
			//环信账号
			List<YBasicImaccount> ims = (List<YBasicImaccount>) dao.find("from YBasicImaccount y where y.fimtel = ?", member.getFmobilePhone());
			if(ims!=null && ims.size()>0){
				YBasicImaccount im = ims.get(0);
				map.put("imUsername", im.getFimkey());
			}
			map.put("mid", member.getFmobilePhone());
			map.put("sex", member.getFsex());
			if(member.getFbirthday()!=null){
				map.put("birthday",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getFbirthday()));
			}
			map.put("provincialId", member.getYBasicProvince() == null ? null :member.getYBasicProvince().getFid());
			map.put("cityId", member.getYBasicCity() == null ? null :member.getYBasicCity().getFid());
			map.put("countryId", member.getYBasicCounty() == null ? null :member.getYBasicCounty().getFid());
			map.put("organization", member.getFnativePlace());
			map.put("tel", member.getFmobilePhone());
			map.put("n8", member.getFisHidePhone());
			return map;
		}
		return null;
		
	}
	/**
	 * 添加
	 * @param member
	 */
	@SystemServiceLog(description = "新增会员")
	public void memberSave(YBasicMember member){
		dao.save(member);
	}

	
	
	/**
	 * 批量更新会员是否隐藏状态
	 * @param fids
	 * @param status
	 */
	@SystemServiceLog(description = "批量修改会员是否隐藏状态")
	public void updateMembersHideStatus(String fids,int status){
		
		if(fids != null){
			StringBuffer sb = new StringBuffer("update y_basic_member set FIsHidePhone = " + status + " where ");
			String [] array = fids.split(",");
			for (int i = 0; i < array.length; i++) {
				sb.append("FID = '" + array[i]  + "' ");
				if(i != array.length - 1){
					sb.append("or ");
				}
			}
			dao.updateBySQL(sb.toString());
		}
	}
	
	/**
	 * app端会员注册
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "app端会员注册")
	public Map<String, Object> appRegMember(Map<String, Object> jsonObj) throws NumberFormatException, Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<String> adressList = null;
		String hql = null;
		//获取传过来的参数
		String password = (String)jsonObj.get("password");
		String phoneCheckCode = (String)jsonObj.get("phoneCheckCode");
		String phone = (String)jsonObj.get("phone");
		String nickname = (String)jsonObj.get("nickname");
		String provincialId = (String)jsonObj.get("provincialId");
		String cityId = (String)jsonObj.get("cityId");
		String countryId = (String)jsonObj.get("countryId");
		String no = (String) map.get("appChannelNo");
		//String applyContent = (String)jsonObj.get("applyContent");
		
		List<YBasicMember> objs = (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?", phone);
		if(objs!=null&&objs.size()>0){
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), "该手机号码已注册！");
			return map;
		}
		/**
		 * update by ailierke
		 */
		adressList = GetAdressByTel.getAdress(EncryptionForTellPhone.decryptByABC(phone));
		if(null!=adressList&&adressList.size()==2){//如果省份和城市存在就执行模糊匹配的操作
			//校验省份类型是否存在
			hql="from YBasicProvince yBasicProvince where yBasicProvince.fname like '%"+adressList.get(1)+"%'";
			List<YBasicProvince> yBasicProvinceList = (List<YBasicProvince>)dao.findAllByHQL(hql);
			if(yBasicProvinceList!=null&&yBasicProvinceList.size()==1){
				provincialId=yBasicProvinceList.get(0).getFid();
			}
			//校验城市类型是否存在
			hql="from YBasicCity yBasicCity where yBasicCity.fname like '%"+adressList.get(0)+"%'";
			List<YBasicCity> yBasicCityList = (List<YBasicCity>)dao.findAllByHQL(hql);
			if(yBasicCityList!=null&&yBasicCityList.size()==1){
				cityId=yBasicCityList.get(0).getFid();
			}
		}
	
		List<YBasicVerification> list = (List<YBasicVerification>) dao.find("from YBasicVerification y where y.fuserPhone = ? and y.fverification = ? order by y.floseDate desc", phone,phoneCheckCode);
		if(list != null && list.size() > 0){
			YBasicVerification obj = list.get(0);
			if(System.currentTimeMillis() > obj.getFloseDate().getTime()){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.CODETIMEOUT.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.CODETIMEOUT.msg());
			}else{
				//保存会员
				YBasicMember member = new YBasicMember();
				member.setFid(UUID.randomUUID().toString());
				member.setFpassword(password);
				member.setFmobilePhone(phone);
				member.setFname(nickname);
				member.setFbillState(5);
				if(countryId != null){
					YBasicCounty country = (YBasicCounty) dao.get(YBasicCounty.class, countryId);
					if(country!=null){
						member.setYBasicCounty(country);
						member.setYBasicCity(country.getYBasicCity());
						member.setYBasicProvince(country.getYBasicCity().getYBasicProvince());
					}
				}else{
					if(cityId != null){
						YBasicCity city = (YBasicCity) dao.get(YBasicCity.class, cityId);
						if(city!=null){
							member.setYBasicCity(city);
							member.setYBasicProvince(city.getYBasicProvince());
						}
					}else{
						if(provincialId != null){
							YBasicProvince province = (YBasicProvince) dao.get(YBasicProvince.class , provincialId);
							if(province!=null){
								member.setYBasicProvince(province);
							}
						}
					}
				}
				String deviceId = "";
				YAppdevice ae = new YAppdevice();
				//获取渠道号,存入设备表
				if("0".equals(no)){
					deviceId = (String) jsonObj.get("clientID");
				}else{
					deviceId = (String) jsonObj.get("cocDevicetoken");
				}
				
				ae.setFappChannelNo(no);
				ae.setFclient((String) jsonObj.get("clientID"));
				ae.setFdeviceId(deviceId);
				ae.setFuserName(phone);
				dao.save(ae);
				
				//用户注册并申请加入商会
//				if(applyContent != null){
//					//存入入会申请记录表
//					YInitiationApply apply = new YInitiationApply();
//					apply.setFid(UUID.randomUUID().toString());
//					apply.setFphone(phone);
//					apply.setFapplyDate(new Date());
//					String groupId = (String)jsonObj.get("businessId");
//					apply.setFgroupsId(groupId);
//					dao.save(apply);
//					
//					//设置会员商会属性
//					String businessId = (String)jsonObj.get("businessId");
//					YBasicSocialgroups group = (YBasicSocialgroups) dao.get(YBasicSocialgroups.class, businessId);
//					member.setYBasicSocialgroups(group);
//					//推送消息到该商会的管理员
//					push(nickname,groupId);
//				}

				dao.save(member);
				
				//获取默认的十个标签
				List<YBasicLabel> lists = LabelXMLToObject.labelObject(phone);
				dao.saveOrUpdateAll(lists);

				/**
				 * update by ailierke
				 */
				//如果这个电话号码所存在的会员部存在，就生成IM账号，如果存在就跳过
				List<YBasicMember> memberList = (List<YBasicMember>) dao.findAllByHQL("from YBasicMember m where m.fmobilePhone = '"+member.getFmobilePhone()+"'");
				//保存生成的IM账号
				YBasicImaccount im = new YBasicImaccount();
				if(memberList!=null&&memberList.size()>1){
					List<YBasicImaccount> imList= (List<YBasicImaccount>) dao.findAllByHQL("from YBasicImaccount imaccount where imaccount.fimtel ='"+phone+"'");
					if(imList!=null && imList.size() > 0){
						im = imList.get(0);
					}
				}else{
					Map<String,String> userMap = new HashMap<String,String>();
					userMap.put("username", CRC32Util.getCRC32(phone));
					userMap.put("password", MD5Util.md5("888888"));//默认密码888888
					IMUtils.createUser(userMap);//新增
					im.setFimkey(CRC32Util.getCRC32(phone));
					im.setFimpassword(MD5Util.md5("888888"));
					im.setFimtel(phone);
					dao.save(im);
				}
				//返回数据
				map.put("imUsername", im.getFimkey());
				map.put("imPassword", im.getFimpassword());
				map.put("mid", phone);
				map.put("md5Info", member.getFpassword());
				
				Map<String, Object> values = new HashMap<String, Object>();
				values.put(ResponseCode.MSGR.msg(), map);
				values.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				values.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				return values;
			}
		}else{
			map.put(ResponseCode.MSGC.msg(), ResponseCode.VALIDATECODEWRONG.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.VALIDATECODEWRONG.msg());
		}
		return map;
	}
	
	/**
	 * 注册会员申请加入商会
	 * @param jsonObj
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "注册会员申请加入商会")
	public Map<String, Object> applyBusiness(Map<String, Object> jsonObj) throws NumberFormatException, Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		String nativePlace = "";
		
		String phone = (String)jsonObj.get("userName");
		String groupId = (String)jsonObj.get("businessId");
		String nickname = (String)jsonObj.get("nickname");
		String companyName = (String)jsonObj.get("companyName");
		String companyPosition = (String)jsonObj.get("companyPosition");
		String provincialId = (String)jsonObj.get("provincialId");
		String cityId = (String)jsonObj.get("cityId");
		String countryId = (String)jsonObj.get("countryId");
		

		//存入入会申请记录表
		YInitiationApply apply = new YInitiationApply();
		apply.setFid(UUID.randomUUID().toString());
		apply.setFphone(phone);
		apply.setFapplyDate(new Date());
		apply.setFgroupsId(groupId);
		apply.setFname(nickname);
		apply.setFcompanyName(companyName);
		apply.setFcompanyPosition(companyPosition);
		apply.setFstate(Status.PENDING.value());
		
		//获取memberID
		List<YBasicMember> list =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",phone);
		if(list!=null&&list.size()>0){
			YBasicMember member = list.get(0);
			apply.setFmemberId(member.getFid());
		}
		
		if(provincialId != null){
			nativePlace += provincialId;
		}
		if(cityId != null){
			nativePlace += cityId;
		}
		if(countryId != null){
			nativePlace += countryId;
		}
		apply.setFnativePlace(nativePlace);
		dao.save(apply);
		
		//推送消息到该商会的管理员
		push(nickname,groupId);
		
		map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
		map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		return map;
	}
	
	/**
	 * 推送会员申请加入商会的消息
	 * @param name
	 * @param groupId
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "推送会员申请加入商会的信息")
	public void push(String name,String groupId) throws NumberFormatException, Exception{
		StringBuffer condition = null;
		YBasicSocialgroups group = (YBasicSocialgroups) dao.get(YBasicSocialgroups.class,groupId);
		String content = name + "申请加入" + group.getFname();
		
		List<YBasicMember> members = (List<YBasicMember>) dao.find("from YBasicMember y where y.YSystemUsers.fid != null and y.YBasicSocialgroups.fid = ?",groupId);
		if(members!=null&&members.size()>0){
			condition =new StringBuffer("(");
			for(YBasicMember mem:members){
				condition.append("'"+mem.getFid()+"', ");
			}
			//去掉最后的","
			condition.replace(condition.lastIndexOf(","),condition.length()-1, "");
			condition.append(")");
			List<String> tels = (List<String>) dao.findAllByHQL("select  ymember.fmobilePhone from  YBasicMember ymember where ymember.fid in "+condition);
			//同样拼接查询用户设备表的in 条件，根据id来查询
			if(tels!=null&&tels.size()>0){
				condition =new StringBuffer("(");
				for(String record:tels){
					condition.append("'"+record+"', ");
				}
				//去掉最后的","
				condition.replace(condition.lastIndexOf(","),condition.length()-1, "");
				condition.append(")");
				List<YAppdevice> memberAppDeviceList = (List<YAppdevice>) dao.findAllByHQL("from  YAppdevice device where device.fuserName in "+condition);
				//如果存在此用户的clientId信息就推送
				if(memberAppDeviceList!=null&&memberAppDeviceList.size()>0){
					String logo ="";
					String logoUrl="";
					String transmissionContent="";
					int transmissionType =1;
					for(YAppdevice yAppdevice:memberAppDeviceList){
					//使用激活应用模板
					PushToListMessage.sendDownLoadMessageToSingel(yAppdevice.getFdeviceId(), new Integer(yAppdevice.getFappChannelNo()), TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", content, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
					}
				}
			}else{
				logger.info("没有登录过的设备信息....");
			}
		}else{
			logger.info("没有管理员....");
		}
	}
/**
 * update by ailierke
 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkAppUserLogin(String tel,String md5info) throws Exception {
		boolean flag = true;
		String hql = "from YAppdevice ydevice where ydevice.fuserName='"+tel+"'";
		List<YAppdevice> deviceList =(List<YAppdevice>)dao.findAllByHQL(hql);
		if(deviceList!=null&&deviceList.size()>0){
			if(!(md5info.equals(deviceList.get(0).getFmd5info()))) 
				flag=false;
		}else{
			throw new RuntimeException("系统异常");
		}
		return flag;
	}

	@Override
	public List findByGroupId(String groupId) {
		List list=new ArrayList();
		try {
			String sql="select y.FID , y.FName from y_basic_member as y where 1=1  ";
			if(groupId!=null&&!"".equals(groupId)){
				sql=sql+" and y.FSocialGroupsID='"+groupId+"'";
			}
			list=dao.getListBySql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Map<String, Object> updateByAPP(String infoMap,String images) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);
			String mid = (node.get("mid") == null ? null : node.get("mid").textValue());
			
			if (null == mid || "".equals(mid)) {
				map.put(ResponseCode.MSGC.msg(), 103);
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				return map;
			}

			List<YBasicMember> memberlist=(List<YBasicMember>)dao.findAllByHQL("from YBasicMember as y  where y.fmobilePhone='"+mid+"'");
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = null;
				if (node.get("birthday") != null
						&& !"".equals(node.get("birthday").textValue())) {

					d = sdf.parse(node.get("birthday").textValue());
				}

				YBasicProvince province = null;
				if (node.get("provincialId") != null
						&& !"".equals(node.get("provincialId").textValue())) {
					province = new YBasicProvince();
					province.setFid(node.get("provincialId").textValue());
				}
				YBasicCity city = null;
				if (node.get("cityId") != null
						&& !"".equals(node.get("cityId").textValue())) {
					city = new YBasicCity();
					city.setFid(node.get("cityId").textValue());
				}
				YBasicCounty county = null;
				if (node.get("countryId") != null
						&& !"".equals(node.get("countryId").textValue())) {
					county = new YBasicCounty();
					county.setFid(node.get("countryId").textValue());
				}
				String userNickName = null;
				if (node.get("userNickName") != null
						&& !"".equals(node.get("userNickName").textValue())) {
					userNickName = node.get("userNickName").textValue();// userNickName
																		// String
																		// 否
																		// 用户昵称
				}
				for(YBasicMember member:memberlist){
				
					String sex = null;
					if (node.get("sex") != null
							&& !"".equals(node.get("sex").textValue())) {
						sex = node.get("sex").textValue(); // sex String 否 性别(0男，1女)
					}

					if (null != member) {

						if (null != images&&!"".equals(images)) {
							member.setFheadImage(images);// userImageUrl String 否
															// 用户头像地址
						}
                
						if (userNickName != null&&!"".equals(userNickName)) {
							member.setFname(userNickName);// userNickName String
															// 否 用户昵称
						}
						if (node.get("sex") != null&&!"".equals(sex)) {
							member.setFsex(Integer.valueOf(sex));// sex String 否
																	// 性别(0男，1女)
						}
						if (d != null&&!"".equals(d)) {
							member.setFbirthday(d);// birthday String 否 生日
						}
						if (province != null&&!"".equals(province)) {
							member.setYBasicProvince(province);// provincialId
																// String 否 省份ID
						}
						if (city != null&&!"".equals(city)) {
							member.setYBasicCity(city);// cityId String 否 城市I
						}
						if (county != null&&!"".equals(city)) {
							member.setYBasicCounty(county);// countryId String 否 县Id
						}
						dao.update(member);
						dao.flush();
					} else {
						map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
						map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
					}
					
			}
				if(null != images&&!"".equals(images)){
					String[] img=images.split(","); 
					map.put(ResponseCode.MSGR.msg(), img[0]);
				}
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagingList<YBasicMember> findAll(String searchName, Integer start,
			Integer limit, String groupId, String sort, String dir) {
		logger.info("List<YBasicMember> findAll()");
		PagingList<YBasicMember> pagingList = new PagingList<YBasicMember>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YBasicMember>)dao.findAll(YBasicMember.class));
			return pagingList;
		}

		String hql = "from YBasicMember y where fbillState!=9 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fname like '%" + searchName + "%' ";
		}
		hql+="and y.YBasicSocialgroups.fid = '"+groupId + "' order by "+sort+" "+dir;

		pagingList.setList((List<YBasicMember>) dao.find(hql, start, limit, null));

		//获取总条数
		List<YBasicMember> list = (List<YBasicMember>)dao.find(hql);
		pagingList.setCount(list.size());

		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
}

