package com.yunzo.cocmore.core.function.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.service.MemberdistributionServiceI;

/**
 * Description: <>. <br>
 * @date:2014年12月16日 上午11:21:46
 * @author beck
 * @version V1.0
 */
@Service("memdisService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class MemberdistributionServiceImpl implements MemberdistributionServiceI{
	
	private static final Logger logger = Logger
			.getLogger(MemberdistributionServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	
	@Override
	@SystemServiceLog(description = "获取当前最大版本号")
	public int getMaxVersion(String businessId) {
		// TODO Auto-generated method stub
		List list = (List)dao.find("select max(y.fversion) from YBasicentriesMemberdistribution y inner join y.YBasicPosition p  "
				+ "where p.fsocialGroupsId = ?", businessId); 
		if(list.get(0) == null ){
			return 0;
		}else{
			return (int) list.get(0);
		}
	}

	/**
	 * 根据职位查询会员
	 */
	@Override
	@SystemServiceLog(description = "根据职位查询会员")
	public List<YBasicentriesMemberdistribution> findPosByEdID(String fid) {
		List<YBasicentriesMemberdistribution> list=null;
		list=(List<YBasicentriesMemberdistribution>)dao.findAllByHQL("from YBasicentriesMemberdistribution y where y.YBasicPosition.fid = '" + fid + "'");
		return list;
	}

	@Override
	public void save(YBasicentriesMemberdistribution obj) {
		dao.save(obj);
		dao.flush();
	}
}
