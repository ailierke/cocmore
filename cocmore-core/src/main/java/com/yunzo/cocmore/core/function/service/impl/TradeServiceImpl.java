package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
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
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsabout;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.service.TradeServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <行业服务实现类>. <br>
 * @date:2014年11月25日 下午5:19:19
 * @author beck
 * @version V1.0                             
 */
@Service("tradeService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class TradeServiceImpl implements TradeServiceI{
	
	private static final Logger logger = Logger
			.getLogger(TradeServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	/**
	 * 查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部行业，并分页")
	public PagingList<YBasicTrade> findAll(String searchName,Integer start,Integer limit) {
		logger.info("List<YBasicTrade> findAll()");
		PagingList<YBasicTrade> pagingList = new PagingList<YBasicTrade>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YBasicTrade>)dao.findAll(YBasicTrade.class));
			return pagingList;
		}
		
		String hql = "from YBasicTrade y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fname like '%" + searchName + "%'";
		}
		pagingList.setList((List<YBasicTrade>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YBasicTrade> list = (List<YBasicTrade>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取行业列表")
	public List<Map<String, Object>> getTradeList() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapStr = null;
		List<YBasicTrade> list = null;
		YBasicTrade trade = null;
		list = (List<YBasicTrade>) dao.findAll(YBasicTrade.class);
		for(int i = 0;i < list.size();i++){
			trade = list.get(i);
			mapStr = new HashMap<String, Object>();
			mapStr.put("tradeName", trade.getFname());
			mapStr.put("tradeID", trade.getFid());
			listMap.add(mapStr);
		}
		return listMap;
	}

	@Override
	@SystemServiceLog(description = "根据id查询新闻")
	public YBasicTrade getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicTrade getById(String fid) || id==" + fid);
		return (YBasicTrade) dao.findById(YBasicTrade.class, fid);
	}

	@Override
	@SystemServiceLog(description = "新增新闻")
	public void save(YBasicTrade demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	@SystemServiceLog(description = "删除新闻")
	public void delete(YBasicTrade demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	@SystemServiceLog(description = "修改新闻")
	public void update(YBasicTrade demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}

	@Override
	public List<YBasicTrade> findByHql(String hql) {
		
		return (List<YBasicTrade>)dao.findAllByHQL(hql);
	}

}
