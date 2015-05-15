package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
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
import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.service.CollectService;

/**
 * @author：jackpeng
 * @date：2014年12月13日下午12:27:45
 * 新闻收藏service实现类
 */
@Service("collectService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class CollectServiceImpl implements CollectService {
private static final Logger logger = Logger.getLogger(CommentServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据新闻id查询是否收藏该新闻")
	public String getCollectYN(String id,String userPhone) {
		String hql = "from TNewsCollect y where y.TNewsHeadline.ftid = '"+id+"' and y.ftel = '"+userPhone+"'";
		List<TNewsCollect> list = (List<TNewsCollect>) dao.findAllByHQL(hql);
		int count = list.size();
		if(count == 0){
			return "0";
		}else{
			return "1";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询新闻收藏")
	public List<TNewsCollect> getByHql(String hql) {
		// TODO Auto-generated method stub
		return (List<TNewsCollect>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "新增新闻收藏")
	public void save(TNewsCollect newsCollect) {
		// TODO Auto-generated method stub
		dao.save(newsCollect);
	}

	@Override
	@SystemServiceLog(description = "删除新闻收藏")
	public void delete(TNewsCollect newsCollect) {
		// TODO Auto-generated method stub
		dao.delete(newsCollect);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部新闻收藏")
	public List<TNewsCollect> findAll() {
		// TODO Auto-generated method stub
		return (List<TNewsCollect>) dao.findAll(TNewsCollect.class);
	}

	@Override
	@SystemServiceLog(description = "根据id查询新闻收藏")
	public TNewsCollect getById(String id) {
		// TODO Auto-generated method stub
		return (TNewsCollect) dao.findById(TNewsCollect.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询新闻收藏，并按条数返回数据")
	public List<TNewsCollect> findByHql(String hql,Integer rowNum) {
		// TODO Auto-generated method stub
		return (List<TNewsCollect>) dao.find(hql, 0, rowNum, null);
	}
	
}
