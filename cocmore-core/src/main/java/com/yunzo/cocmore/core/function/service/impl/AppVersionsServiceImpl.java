package com.yunzo.cocmore.core.function.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.model.mysql.YSystemAppVersions;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.service.AppVersionsServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * Description: <APP版本信息实现类>. <br>
 * @date:2014年12月13日 下午1:59:56
 * @author beck
 * @version V1.0
 */
@Service("appVnService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class AppVersionsServiceImpl implements AppVersionsServiceI{
	private static final Logger logger = Logger.getLogger(AppVersionsServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部版本信息，并分页")
	public PagingList<YSystemAppVersions> findAll(String searchName,Integer start,Integer limit) {
		// TODO Auto-generated method stub
		logger.info("List<YSystemAppVersions> findAll()");
		PagingList<YSystemAppVersions> pagingList = new PagingList<YSystemAppVersions>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YSystemAppVersions>)dao.findAll(YSystemAppVersions.class));
			return pagingList;
		}
		
		String hql = "from YSystemAppVersions y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.versionNumber = '" + searchName + "'";
		}
		pagingList.setList((List<YSystemAppVersions>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YSystemAppVersions> list = (List<YSystemAppVersions>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "新增版本信息")
	public void save(YSystemAppVersions demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	@SystemServiceLog(description = "修改版本信息")
	public void update(YSystemAppVersions demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}
	
	@Override
	@SystemServiceLog(description = "获取版本信息")
	public Map<String, Object> getAppVersions(String appVersion,String appChannelNo){
		Map<String, Object> objList = null;
		YSystemAppVersions demo = null;
		List<YSystemAppVersions> objs = (List<YSystemAppVersions>) dao.findAllByHQL("from YSystemAppVersions y where y.chanelNo = "+appChannelNo+" order by y.flag desc");
		if(objs!=null&&objs.size()>0){
			 demo = objs.get(0);
			 if(!demo.getVersionNumber().equals(appVersion)){
				 objList = new HashMap<String, Object>();
				 objList.put("downloadUrl", demo.getDownloadUrl());
				 objList.put("updateDetail", demo.getUpdateDetail());
				 objList.put("versionNumber", demo.getVersionNumber());
			 }
		}
		
		return objList;
	}
	
}
