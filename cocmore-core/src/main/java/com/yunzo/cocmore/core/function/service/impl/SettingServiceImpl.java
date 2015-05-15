package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.FAwardSetting;
import com.yunzo.cocmore.core.function.service.SettingService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @author：jackpeng
 * @date：2014年11月26日上午10:08:16
 * 抽奖设置service实现类
 */
@Service("settingService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class SettingServiceImpl implements SettingService {
	
	private static final Logger logger = Logger.getLogger(SettingServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部抽奖设置")
	public List<FAwardSetting> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<FAwardSetting> findAll()");
		return (List<FAwardSetting>)dao.findAll(FAwardSetting.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部抽奖设置，并分页")
	public PagingList<FAwardSetting> getAllSettingPagingList(Integer page,
			Integer pageSize, String activityName,String groupId) {
		logger.info("查询全部抽奖设置并分页...");
		PagingList<FAwardSetting> pagingList = new PagingList<FAwardSetting>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from FAwardSetting f where 1=1");
		StringBuffer hqlCount = new StringBuffer("select count(0) from FAwardSetting f where 1=1");
		//判断是否通过groupId或headline
		if(groupId != null){
			hqlList.append("and f.YWallactivity.YBasicSocialgroups.fid=?");
			hqlCount.append("and f.YWallactivity.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		if(activityName != null){
			hqlList.append("and f.YWallactivity.ftheme like '%"+activityName+"%'");
			hqlCount.append("and f.YWallactivity.ftheme like '%"+activityName+"%'");
		}
		//获得此页数据
		pagingList.setList((List<FAwardSetting>)dao.find(hqlList.toString(), page, pageSize,values.toArray()));
		//获得总条数
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize,values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询抽奖设置")
	public FAwardSetting getById(String id) {
		// TODO Auto-generated method stub
		logger.info("FAwardSetting getById(String id) fid=="+id);
		return (FAwardSetting)dao.findById(FAwardSetting.class, id);
	}

	@Override
	@SystemServiceLog(description = "新增抽奖设置")
	public void save(FAwardSetting setting) {
		// TODO Auto-generated method stub
		dao.save(setting);
	}

	@Override
	@SystemServiceLog(description = "删除抽奖设置")
	public void delete(FAwardSetting setting) {
		// TODO Auto-generated method stub
		dao.delete(setting);
	}

	@Override
	@SystemServiceLog(description = "修改抽奖设置")
	public void update(FAwardSetting setting) {
		// TODO Auto-generated method stub
		dao.update(setting);
	}
	
}
