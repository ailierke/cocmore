package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;
import com.yunzo.cocmore.core.function.service.LabelServiceI;

/**
 * @author：jackpeng
 * @date：2014年12月16日上午9:58:45
 * 标签service实现类
 */
@Transactional
@Service("labelService")
public class LabelServiceImpl implements LabelServiceI {
	
	private static final Logger logger = Logger.getLogger(LabelServiceImpl.class);
	
	@Resource
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据用户名查询标签")
	public int getLabelNum(String userName) {
		logger.info("int getLabelHql(String userName)");
		String hql = "from YBasicLabel y where y.fuserPhone = '"+userName+"'";
		List<YBasicLabel> list = (List<YBasicLabel>) dao.findAllByHQL(hql);
		if(list.size()==0){
			return 1;
		}else{
			return 0;
		}
	}
	
	@Override
	@SystemServiceLog(description = "删除标签")
	public void delete(YBasicLabel label) {
		// TODO Auto-generated method stub
		dao.delete(label);
	}

	@Override
	@SystemServiceLog(description = "新增标签")
	public void save(YBasicLabel label) {
		// TODO Auto-generated method stub
		dao.save(label);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询标签，返回对象")
	public List<YBasicLabel> getLabelHql(String hql) {
		// TODO Auto-generated method stub
		return (List<YBasicLabel>) dao.findAllByHQL(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询标签，返回字符串")
	public List<String> getByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<String>) dao.findAllByHQL(hql);
	}

	@Override
	public void saveOrUpdateList(List<YBasicLabel> list) {
		// TODO Auto-generated method stub
		dao.saveOrUpdateAll(list);
	}
	
}
