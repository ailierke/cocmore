package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.service.CharacterService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @ClassName: CharacterServiceImpl 
 * @Description: TODO(角色接口实现类) 
 * @date 2014年11月24日 上午11:53:20 
 * @author Ian
 *
 */
@Service("characterService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class CharacterServiceImpl implements CharacterService{

	private static final Logger logger = Logger.getLogger(CharacterServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "新增角色")
	public void save(YCharacter character) {
		try {
			dao.save(character);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(character.getFname());
	}

	@Override
	@SystemServiceLog(description = "修改角色")
	public void update(YCharacter character) {
		dao.update(character);
	}

	@Override
	@SystemServiceLog(description = "删除角色")
	public void delete(YCharacter character) {
		dao.delete(character);
	}


	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部角色")
	public List<YCharacter> findAll() {
		List<YCharacter> list=null;
		try {
			list = (List<YCharacter>)dao.findAllByHQL("from YCharacter as y where y.fstate=5");
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println(list.get(0).getYBasicOrganization().getFname());
		//List<YCharacter> list = (List<YCharacter>)dao.findAllByHQL("from YCharacter");
		//List<YCharacter> list = (List<YCharacter>)dao.getListBySql("SELECT * FROM y_character");
//		System.out.println(list.get(0).getYBasicOrganization().getFid());
//		System.out.println(list);
//		System.out.println(list.get(0).getYRoleauthorizations());
		return list;
		
	}


	@Override
	@SystemServiceLog(description = "查询全部角色并分页，或根据条件查询角色")
	public PagingList<YCharacter> getAllDynamicPagingList(Integer page,
			Integer pageSize,String searchName) {
		PagingList<YCharacter> pagingList = new PagingList<YCharacter>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YCharacter as y");
		StringBuffer hqlCount = new StringBuffer("select count(0) from YCharacter y");
		if(null!=searchName&&!"".equals(searchName)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("y.fname like ?");
			hqlCount.append("y.fname like ?");
			values.add("%"+searchName+"%");
		}
		System.out.println(hqlList);
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YCharacter>)dao.find(hqlList.toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询角色")
	public YCharacter getById(String fid) {
		return (YCharacter)dao.findById(YCharacter.class, fid);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询角色")
	public List<YCharacter> getByHql(String hql) {
		return (List<YCharacter>)dao.findAllByHQL(hql);
	}
	
}
