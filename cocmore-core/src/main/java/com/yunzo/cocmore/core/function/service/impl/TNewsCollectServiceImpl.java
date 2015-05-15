package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.service.TNewsCollectService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @ClassName: TNewsCollectServiceImpl 
 * @Description: TODO 新闻收藏接口实现类 
 * @date 2014年11月26日 上午11:05:53 
 * @author Ian
 *
 */
@Service("newsCollectService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class TNewsCollectServiceImpl implements TNewsCollectService {

	private static final Logger logger = Logger.getLogger(TNewsCollectServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	@SystemServiceLog(description = "新增新闻收藏")
	public void save(TNewsCollect newsCollect) {
		dao.save(newsCollect);
	}

	@Override
	@SystemServiceLog(description = "修改新闻收藏")
	public void update(TNewsCollect newsCollect) {
		dao.update(newsCollect);
	}

	@Override
	@SystemServiceLog(description = "删除新闻收藏")
	public void delete(TNewsCollect newsCollect) {
		dao.delete(newsCollect);
	}

	@Override
	@SystemServiceLog(description = "查询全部新闻收藏")
	public List<TNewsCollect> findAll() {
		return (List<TNewsCollect>)dao.findAll(TNewsCollect.class);
	}

	@Override
	@SystemServiceLog(description = "查询新闻收藏并分页")
	public PagingList<TNewsCollect> getAllDynamicPagingList(Integer page,
			Integer pageSize) {
		
		PagingList<TNewsCollect> pagingList = new PagingList<TNewsCollect>();
		Integer fistResult = null;
		if(page != null){
			fistResult = (page - 1) * pageSize;
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<TNewsCollect>)dao.findByCriteria(DetachedCriteria.forClass(TNewsCollect.class),fistResult,pageSize));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCriteria(DetachedCriteria.forClass(TNewsCollect.class)));
		
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询新闻收藏")
	public TNewsCollect getById(String fid) {
		return (TNewsCollect)dao.findById(TNewsCollect.class, fid);
	}

}
