package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsactivity;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsActivityService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
/**
 * 社会团体活动实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsActivityService")
public class GroupsActivityServiceImpl implements GroupsActivityService {
	private static final Logger logger = Logger.getLogger(GroupsActivityServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	@Override
	@SystemServiceLog(description = "新增团体活动")
	public Boolean addActivity(YBasicSocialgroupsactivity activity) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.save(activity);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}
	
	@Override
	@SystemServiceLog(description = "根据id查询团体活动")
	public YBasicSocialgroupsactivity getById(String id) {
		// TODO Auto-generated method stub
		return (YBasicSocialgroupsactivity) cOC_HibernateDAO.findById(YBasicSocialgroupsactivity.class, id);
	}

	@Override
	@SystemServiceLog(description = "删除团体联系方式")
	public void deleteActivity(YBasicSocialgroupsactivity activity) {
		cOC_HibernateDAO.delete(activity);

	}

	@Override
	@SystemServiceLog(description = "团体联系方式审核、反审核")
	public Boolean auditActivity(YBasicSocialgroupsactivity activity) {
		boolean flag = true;
		if(activity.getFbillState()==Status.UNAUDIT.value()||activity.getFbillState()==Status.AUDIT.value()||activity.getFbillState()==Status.SUBMIT.value()){//如果是审核、反审核、提交状态就进行下列操作
			flag = false;
			YBasicSocialgroupsactivity activity1 = (YBasicSocialgroupsactivity) cOC_HibernateDAO.get(YBasicSocialgroupsactivity.class, activity.getFid());
			if(null != activity1){
				activity1.setFbillState(activity.getFbillState());//更改需求信息的审核状态 审核或者反审核
				cOC_HibernateDAO.update(activity1);//更新到持久层
				flag = true;
			}else{
				logger.info("数据不存在!");
			}
		}
		return flag;
	}

	@Override
	@SystemServiceLog(description = "团体联系方式生效、失效")
	public Boolean effectActivity(YBasicSocialgroupsactivity activity) {
		boolean flag = true;
		if(activity.getFbillState()==Status.EFFECT.value()||activity.getFbillState()==Status.UNEFFECT.value()){//如果是生效或者失效的主材进行下面的操作
			  flag = false;
			YBasicSocialgroupsactivity activity1 = (YBasicSocialgroupsactivity) cOC_HibernateDAO.get(YBasicSocialgroupsactivity.class, activity.getFid());
			if(null != activity1){
				activity1.setFbillState(activity.getFbillState());//更改需求信息的生效状态  生效  失效
				cOC_HibernateDAO.update(activity1);//更新到持久层
				flag = true;
			}else{
				logger.info("数据不存在!");
			}
		}
		return flag;
	}
	@Override
	@SystemServiceLog(description = "修改团体联系方式")
	public Boolean updateActivity(YBasicSocialgroupsactivity activity) {
		boolean flag = true;
		YBasicSocialgroupsactivity activity1 = (YBasicSocialgroupsactivity) cOC_HibernateDAO.get(YBasicSocialgroupsactivity.class, activity.getFid());
		if(null != activity1){
			activity1.setFbillState(activity.getFbillState());
			activity1.setFcomment(activity.getFcomment());
			activity1.setFheadline(activity.getFheadline());
			activity1.setFmessage(activity.getFmessage());
			activity1.setFnumber(activity.getFnumber());//编号需要自己去获取
			activity1.setFfinishTime(activity.getFfinishTime()); //结束时间
			activity1.setFstartTime(activity.getFstartTime());//开始时间
			activity1.setFcost(activity.getFcost());
			activity1.setFimages(activity.getFimages());//多张图片地址用“，”逗号分隔，前台负责拼接和拆分
			activity1.setFpeopleNum(activity.getFpeopleNum());
			activity1.setFsite(activity.getFsite());
			activity1.setFsource(activity.getFsource());
			activity1.setFsponsor(activity.getFsponsor());
			
			/**
			 * 下面还要更新表中外键关系
			 */
			activity1.setYBasicMember(activity.getYBasicMember());//发布人
			activity1.setYBasicType(activity.getYBasicType());//类型表
			activity1.setYBasicSocialgroups(activity.getYBasicSocialgroups());//前台传入YBasicSocialgroups的主键
			cOC_HibernateDAO.update(activity1);//更新到持久层
			
			flag = true;
		}else{
			logger.info("数据不存在!");
		}
		return flag;
	}


	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部、或根据条件查询团体联系方式")
	public PagingList<YBasicSocialgroupsactivity> getAllActivityPagingList(Integer page, Integer pageSize,String groupId,String fheadline) {
		PagingList<YBasicSocialgroupsactivity> pagingList = new PagingList<YBasicSocialgroupsactivity>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsactivity activity"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsactivity activity"); 
		/**
		 *团体id
		 */
		if(null!=groupId&&!"".equals(groupId)){
			hqlList.append(" where activity.YBasicSocialgroups.fid=?");
			hqlCount.append(" where activity.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		/**
		 * 如果传来模糊查询name
		 */
		if(null!=fheadline&&!"".equals(fheadline)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("activity.fheadline like ?");
			hqlCount.append("activity.fheadline like ?");
			values.add("%"+fheadline+"%");
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicSocialgroupsactivity>) cOC_HibernateDAO.find(hqlList.toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "修改团体联系方式")
	public void update(YBasicSocialgroupsactivity activity) {
		cOC_HibernateDAO.update(activity);
	}


}
