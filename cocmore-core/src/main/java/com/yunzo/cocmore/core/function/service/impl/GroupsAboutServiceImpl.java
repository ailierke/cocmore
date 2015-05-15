package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.service.GroupsAboutService;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 社会团体关于业务实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsAboutService")
public class GroupsAboutServiceImpl implements GroupsAboutService {
	private static final Logger logger = Logger.getLogger(GroupsAboutServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	
	@Override
	@SystemServiceLog(description = "添加社会团体")
	public Boolean addGroupsAbout(YBasicSocialgroupsabout groupabout) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.save(groupabout);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询社会团体关于我，并分页")
	public PagingList<YBasicSocialgroupsabout> getAllGroupsPagingList(Integer page, Integer pageSize,String groupId) {
		logger.info("查询关于我们信息列表....");
		PagingList<YBasicSocialgroupsabout> pagingList = new PagingList<YBasicSocialgroupsabout>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsabout groupsabout"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsabout groupsabout"); 
		/**
		 * 通过groupId
		 */
		if(null!=groupId&&!"".equals(groupId)){
			hqlList.append(" where groupsabout.YBasicSocialgroups.fid=? order by YBasicSocialgroups.flag asc");
			hqlCount.append(" where groupsabout.YBasicSocialgroups.fid=?");
			values.add(groupId);
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<YBasicSocialgroupsabout>) cOC_HibernateDAO.find(hqlList.toString(), page, pageSize, values.toArray()));
			/**
			 * 获得总条数
			 */
			pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
			logger.info("总条数："+pagingList.getCount());
			logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		}
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "修改社会团体关于我")
	public boolean updateGroupsAbout(YBasicSocialgroupsabout groupabout) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.update(groupabout);//更新到持久层
		}catch(Exception e){
			logger.info("更新异常!");
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	@SystemServiceLog(description = "删除社会团体关于我")
	public boolean deleteGroupsAbout(YBasicSocialgroupsabout groupsabout) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.delete(groupsabout);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}

	@Override
	@SystemServiceLog(description = "查看社会团体关于我详情")
	public YBasicSocialgroupsabout findGroupsAbout(YBasicSocialgroupsabout groupsabout) {
		groupsabout = (YBasicSocialgroupsabout) cOC_HibernateDAO.findById(YBasicSocialgroupsabout.class, groupsabout.getFid());
		return groupsabout;
	}

	@Override
	@SystemServiceLog(description = "hql查询社会团体关于我")
	public List<YBasicSocialgroupsabout> findByHql(String hql) {
		
		return (List<YBasicSocialgroupsabout>)cOC_HibernateDAO.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "根据团体查询社会团体关于我")
	public List<YBasicSocialgroupsabout> findByGroupId(List<YBasicSocialgroupsabout> groupsabouts, String groupId) {
		try {
			groupsabouts=(List<YBasicSocialgroupsabout>)cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroupsabout as y where y.YBasicSocialgroups.fid='"+groupId+"' order by y.sequenceNumber asc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupsabouts;
	}

	@Override
	@SystemServiceLog(description = "根据团体id查询社会团体关于我")
	public Map<String, Object> getBygroupId(String groupId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<YBasicSocialgroupsabout> groupsabouts=new ArrayList<YBasicSocialgroupsabout>();
			groupsabouts=(List<YBasicSocialgroupsabout>)cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroupsabout as y where y.YBasicSocialgroups.fid='"+groupId+"' order by y.sequenceNumber asc");
			map.put("groupsabouts", groupsabouts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public YBasicSocialgroupsabout getByAboutId(String id) {
		YBasicSocialgroupsabout about=new YBasicSocialgroupsabout();
		try {
			about=(YBasicSocialgroupsabout)cOC_HibernateDAO.findById(YBasicSocialgroupsabout.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return about;
	}
}
