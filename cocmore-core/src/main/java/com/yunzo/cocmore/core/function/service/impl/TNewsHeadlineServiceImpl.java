package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupscontact;
import com.yunzo.cocmore.core.function.service.TNewsHeadlineService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * @ClassName: TNewsHeadlineServiceImpl 
 * @Description: TODO 新闻头条接口实现类 
 * @date 2014年11月26日 上午11:07:49 
 * @author Ian
 *
 */
@Service("newsHeadlineService")
//启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
//不加事务会报异常：No Session found for current
//thread，所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class TNewsHeadlineServiceImpl implements TNewsHeadlineService {

	private static final Logger logger = Logger.getLogger(TNewsHeadlineServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "按条数hql查询新闻")
	public List<TNewsHeadline> findByHql(String hql, Integer rowNum) {
		// TODO Auto-generated method stub
		return (List<TNewsHeadline>) dao.find(hql, 0, rowNum, null);
	}

	@Override
	@SystemServiceLog(description = "新增新闻")
	public void save(TNewsHeadline newsHeadline) {
		dao.save(newsHeadline);
	}

	@Override
	@SystemServiceLog(description = "修改新闻")
	public void update(TNewsHeadline newsHeadline) {
		dao.update(newsHeadline);
	}

	@Override
	@SystemServiceLog(description = "删除新闻")
	public void delete(TNewsHeadline newsHeadline) {
		dao.delete(newsHeadline);
	}

	@Override
	@SystemServiceLog(description = "查询全部新闻")
	public List<TNewsHeadline> findAll() {
		return (List<TNewsHeadline>)dao.findAll(TNewsHeadline.class);
	}

	@Override
	@SystemServiceLog(description = "查询全部新闻，并分页")
	public PagingList<TNewsHeadline> getAllDynamicPagingList(String fheadline,Integer start,Integer limit,String zsxs,String xwfl,String publicTime) {
		PagingList<TNewsHeadline> pagingList = new PagingList<TNewsHeadline>();
		List<Object> values = new ArrayList<Object>();
		
		StringBuffer hqlList = new StringBuffer(" from TNewsHeadline as y  where 1=1 "); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from TNewsHeadline y where 1=1 "); 
		/**
		 * 通过groupId
		 */
		if(null!=fheadline&&!"".equals(fheadline)){
			hqlList.append(" and y.ftitle like '%" + fheadline + "%'");
			hqlCount.append(" and y.ftitle like '%" + fheadline + "%'");
		}
		if(null!=zsxs&&!"".equals(zsxs)){
			hqlList.append(" and y.ftype = '" + zsxs + "'");
			hqlCount.append(" and y.ftype = '" + zsxs + "'");
		}
		if(null!=xwfl&&!"".equals(xwfl)){
			hqlList.append(" and y.fclassification = '" + xwfl + "'");
			hqlCount.append(" and y.fclassification = '" + xwfl + "'");
		}
		if(null!=publicTime&&!"".equals(publicTime)){
			String begin = publicTime+" 00:00:00";  
		    String end = publicTime+" 23:59:59";  

			hqlList.append(" and y.freleaseTime >= '" + begin + "' and y.freleaseTime <='"+end+"'");
			hqlCount.append(" and y.freleaseTime >= '" + begin + "' and y.freleaseTime <='"+end+"'");
		}
		/**
		 * 
		 * 获得此页数据
		 */
		pagingList.setList((List<TNewsHeadline>) dao.find(hqlList.toString(), start, limit, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), start, limit, values.toArray()));
		
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询新闻")
	public TNewsHeadline getById(String fid) {
		return (TNewsHeadline)dao.findById(TNewsHeadline.class, fid);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取新闻信息")
	public List<Map<String, Object>> getHeadlineMap(){
		List<Map<String, Object>> array = new ArrayList<Map<String,Object>>();
		List<TNewsHeadline> list = (List<TNewsHeadline>) dao.findAll(TNewsHeadline.class);
		Map<String, Object> map = null;
		for(int i = 0;i < list.size();i++){
			map = new HashMap<String, Object>();
			map.put("tId", list.get(i).getFtid());
			map.put("title", list.get(i).getFtitle());
			map.put("imageUrl", list.get(i).getFimageUrl());
			map.put("releaseTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(list.get(i).getFreleaseTime()));
			map.put("detailsUrl", list.get(i).getFdetailsUrl());
			array.add(map);
		}
		return array;
	}

}
