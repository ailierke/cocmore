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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupscontact;
import com.yunzo.cocmore.core.function.service.GroupsContactService;
import com.yunzo.cocmore.core.function.util.PagingList;
/**
 * 社会团体联系方式业务实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsContactService")
public class GroupsContactServiceImpl implements GroupsContactService {
	private static final Logger logger = Logger.getLogger(GroupsContactServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	
	@Override
	@SystemServiceLog(description = "新增团体联系方式")
	public Boolean addGroupsContact(YBasicSocialgroupscontact groupscontact) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.save(groupscontact);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询团体联系方式，并分页")
	public PagingList<YBasicSocialgroupscontact> getAllGroupsPagingList(Integer page, Integer pageSize,String groupId) {
		logger.info("查联系我们信息列表....");
		PagingList<YBasicSocialgroupscontact> pagingList = new PagingList<YBasicSocialgroupscontact>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer(" from YBasicSocialgroupscontact groupContact"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupscontact groupContact"); 
		/**
		 * 通过groupId
		 */
		if(null!=groupId&&!"".equals(groupId)){
			hqlList.append(" where groupContact.YBasicSocialgroups.fid=?");
			hqlCount.append(" where groupContact.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicSocialgroupscontact>) cOC_HibernateDAO.find(hqlList.toString(), page, pageSize, values.toArray()));
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
	public boolean updateGroupsContact(YBasicSocialgroupscontact groupscontact) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.update(groupscontact);//更新到持久层
		}catch(Exception e){
			logger.info("更新异常!");
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	@SystemServiceLog(description = "删除团体联系方式")
	public boolean deleteGroupsContact(YBasicSocialgroupscontact groupContact) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.delete(groupContact);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}

	@Override
	@SystemServiceLog(description = "查看团体联系方式详情")
	public YBasicSocialgroupscontact findGroupsContact(YBasicSocialgroupscontact groupContact) {
		groupContact = (YBasicSocialgroupscontact) cOC_HibernateDAO.findById(YBasicSocialgroupscontact.class, groupContact.getFid());
		return groupContact;
	}

	@Override
	@SystemServiceLog(description = "hql查询团体联系方式")
	public List<YBasicSocialgroupscontact> findByHql(String hql) {
		
		return (List<YBasicSocialgroupscontact>)cOC_HibernateDAO.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "根据团体查询团体联系方式")
	public YBasicSocialgroupscontact findByGroupId(YBasicSocialgroupscontact groupscontact,String groupId) {
		try {
			List<YBasicSocialgroupscontact> groupscontacts=new ArrayList<YBasicSocialgroupscontact>();
			groupscontacts=(List<YBasicSocialgroupscontact>)cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroupscontact as y where y.YBasicSocialgroups.fid='"+groupId+"'");
			groupscontact =groupscontacts.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupscontact;
	}

	@Override
	@SystemServiceLog(description = "根据团体id查询团体联系方式")
	public Map<String, Object> getByGroupId(String groupId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<YBasicSocialgroupscontact> groupscontacts=new ArrayList<YBasicSocialgroupscontact>();
			groupscontacts=(List<YBasicSocialgroupscontact>)cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroupscontact as y where y.YBasicSocialgroups.fid='"+groupId+"'");
			if(groupscontacts!=null&&groupscontacts.size()>0){
				map.put("groupscontact", groupscontacts.get(0));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
}
