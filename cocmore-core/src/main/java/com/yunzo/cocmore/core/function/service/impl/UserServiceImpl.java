package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YBasicVerification;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.RandomArray;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.base.SMSUtils;

/**
 * Description: <用户服务实现类>. <br>
 * <p>
 * <用户服务实现类>
 * </p>
 * Makedate:2014年11月18日 上午4:26:59
 * 
 * @author xiaobo
 * @version V1.0
 */
@Service("userInfo")
// 启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
// 不加事务会报异常：No Session found for current
// thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger
			.getLogger(UserServiceImpl.class);

	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	

	/**
	 * 查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部用户")
	public PagingList<YSystemUsers> findAll(String searchName,String type,Integer start,Integer limit) {
		logger.info("List<YSystemUsers> findAll()");
		PagingList<YSystemUsers> pagingList = new PagingList<YSystemUsers>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YSystemUsers>)dao.findAll(YSystemUsers.class));
			return pagingList;
		}
		
		StringBuffer sql = new StringBuffer("select yy.FID as fid,yy.FAccount as faccount,yy.FUserPassword as fuserPassword,yy.FBillState as fbillState,"
				+ "yy.FTypeID as ftypeId,yy.FLag as flag,yy.FPrivileges as fprivileges,g.FName as fSGName from (select u.FID,u.FAccount,u.FUserPassword,u.FBillState,u.FTypeID,u.FLag,u.FPrivileges,m.FSocialGroupsID from y_system_users u "
				+ "LEFT JOIN y_basic_member m on u.FID = m.FAdminID) yy "
				+ "LEFT JOIN y_basic_socialgroups g on yy.FSocialGroupsID = g.FID ");
		
		if(searchName != null){
			sql.append("where yy.faccount like '%" + searchName + "%' ");
		}
		if(type != null){
			sql.append("and yy.ftypeId = '" + type + "' ");
		}
		
		//获取总条数
		List list = (List)dao.getListBySql(sql.toString());
		pagingList.setCount(list.size());
		//获取分页的数据
		sql.append("order by flag limit " + start + "," + limit);
		List<YSystemUsers> ss = (List<YSystemUsers>) dao.getListBySqlVO(sql.toString(), YSystemUsers.class);
		
		pagingList.setList(ss);
		
		
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}


	//@Cacheable(value = "commonCache", key = "#fid + 'UserServiceImpl.getById'")
	@SystemServiceLog(description = "根据用户id查询用户")
	public YSystemUsers getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YSystemUsers getById(String id)|| id==" + fid);
		return (YSystemUsers) dao.findById(YSystemUsers.class, fid);
	}


	//@Cacheable(value = "commonCache", key = "#fid + '_' + #name + 'UserServiceImpl.getByName'")
	public YSystemUsers getByNameAndPassWord(String name,String password,String type) {
		// TODO Auto-generated method stub
		logger.info("getByNameAndPassWord(String name,String password,String type)|| name==" + name
				+ "||password==" + password+ "||type==" + type);
		List<YSystemUsers> list = (List<YSystemUsers>) dao.find("from YSystemUsers where faccount = ? and fuserPassword = ? and ftypeId = ?",name,password,type);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	//@CacheEvict(value = "commonCache", key = "#user.id + 'UserServiceImpl.getById'")
	@SystemServiceLog(description = "添加用户")
	public void save(YSystemUsers demo) {
		// TODO Auto-generated method stub
		demo.setFuserPassword(MD5Util.md5(BEGINSTRING + "888888"));
		demo.setFprivileges(0);
		dao.save(demo);
	}

	@Override
	//@CacheEvict(value = "commonCache", allEntries = true)
	@SystemServiceLog(description = "删除用户")
	public void delete(YSystemUsers demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}
	
	/**
	 * 检查用户账号是否已存在
	 * @param faccount
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "检查用户账号是否已存在")
	public boolean checkByFaccount(String faccount){
		List<YSystemUsers> list = (List<YSystemUsers>) dao.find("from YSystemUsers where faccount = ?",faccount);
		if(list.size() > 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 修改密码时，检查用户原密码是否输入正确
	 * @param faccount
	 * @return
	 */
	@Override
	@SystemServiceLog(description = "修改密码时，检查用户原密码是否是否输入正确")
	public boolean checkUserPassword(String fid,String password){
		@SuppressWarnings("unchecked")
		List<YSystemUsers> list = (List<YSystemUsers>) dao.find("from YSystemUsers y where y.fid = ? and y.fuserPassword = ?",fid,MD5Util.md5(BEGINSTRING + password));
		if(list.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改
	 */
	@Override
	 @SystemServiceLog(description = "修改用户")
	public void update(YSystemUsers demo){
		dao.update(demo);
	}
	
	
	/**
	 * 修改密码
	 * @param password
	 */
	@SystemServiceLog(description = "修改密码")
	public void updatePassword(String fid,String password){
		YSystemUsers user = (YSystemUsers) dao.findById(YSystemUsers.class, fid);
		user.setFuserPassword(MD5Util.md5(BEGINSTRING + password));
//		String hql = "update y_system_users y set y.FUserPassword = '"+ MD5Util.md5(password) +"' where y.FID='" + fid +"'";
//		dao.updateBySQL(hql);
		dao.update(user);
	}
	
	/**
	 * 通过账号查询
	 * @param faccount
	 * @return
	 */
	@SystemServiceLog(description = "通过账号查询")
	public YSystemUsers getByFaccount(String faccount){
		@SuppressWarnings("unchecked")
		List<YSystemUsers> list = (List<YSystemUsers>) dao.find("from YSystemUsers  where faccount = ?",faccount);
		return list.get(0); 
	}
	
	/**
	 * 发送验证码到手机
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "发送验证码到手机")
	public int sendTel(String faccount){
		//获取随机验证码
		try {
			String code = RandomArray.getRandom(4);
			String msgText = "验证码"+ code +",仅用于找回密码,请勿告知他人。【验证码失效时间为30分钟】";
			SMSUtils.sendSMS(faccount, msgText);
	
			YBasicVerification obj = new YBasicVerification();
			obj.setFid(UUID.randomUUID().toString());
			obj.setFuserPhone(faccount);
			obj.setFverification(code);
			
			//计算出失效时间
			long time = System.currentTimeMillis();	
			long loseDate = time + 1000 * 60 * 30;
			obj.setFloseDate(new Date(loseDate));
			
			dao.save(obj);
			//保存短信发送记录
			FSmsSendrecord sms = new FSmsSendrecord();
			
			//获取对应会员信息
			List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",faccount);
			if(members.size() > 0){
				YBasicMember member = members.get(0);
				sms.setYBasicMember(member);
				sms.setYBasicSocialgroups(member.getYBasicSocialgroups());
			}
			sms.setFmobilePhone(faccount);
			sms.setFcontent(msgText);
			sms.setFbillState(0);
			sms.setFsendTime(new Date(time));
			dao.save(sms);
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	/**
	 * 判断验证码
	 * @param faccount
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "判断验证码")
	public Map<String, Object> checkCode(String faccount,String code){
		Map<String, Object> value = new HashMap<String, Object>();
		try {
			List<YBasicVerification> list = (List<YBasicVerification>) dao.find("from YBasicVerification y where y.fuserPhone = ? and y.fverification = ? order by y.floseDate desc", faccount,code);
			if(list != null && list.size() > 0){
				YBasicVerification obj = list.get(0);
				if(System.currentTimeMillis() > obj.getFloseDate().getTime()){
					value.put("success", false);
					value.put("msg", "验证码已经失效，请重新获取");
				}else{
					YSystemUsers user = getByFaccount(faccount);
					value.put("success", true);
					value.put("fid", user.getFid());
				}
			}else{
				value.put("success", false);
				value.put("msg", "验证码输入有误，请重新输入！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
