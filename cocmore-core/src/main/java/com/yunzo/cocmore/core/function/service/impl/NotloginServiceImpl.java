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
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.model.mysql.YVisitorsRecordNotlogin;
import com.yunzo.cocmore.core.function.service.NotloginServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <未登陆用户行为服务实现类>. <br>
 * @date:2014年12月5日 下午5:21:01
 * @author beck
 * @version V1.0                             
 */
@Service("notLoginService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class NotloginServiceImpl implements NotloginServiceI{
	private static final Logger logger = Logger
			.getLogger(NotloginServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	/**
	 * 查询全部
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部未登录用户，并分页")
	public PagingList<YVisitorsRecordNotlogin> findAll(String searchName,
			Integer start, Integer limit) {
		logger.info("List<YVisitorsRecordNotlogin> findAll()");
		PagingList<YVisitorsRecordNotlogin> pagingList = new PagingList<YVisitorsRecordNotlogin>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YVisitorsRecordNotlogin>)dao.findAll(YVisitorsRecordNotlogin.class));
			return pagingList;
		}
		
		String hql = "from YVisitorsRecordNotlogin y where 1=1 ";
		//获取数据
		if(searchName != null){
			hql += "and y.fequipment like '%" + searchName + "%'";
		}
		pagingList.setList((List<YVisitorsRecordNotlogin>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YVisitorsRecordNotlogin> list = (List<YVisitorsRecordNotlogin>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	/**
	 * 根据会员ID查询
	 * @param fid
	 * @return
	 */
	public List<YVisitorsRecordNotlogin> getById(String memberFid){
		return (List<YVisitorsRecordNotlogin>) dao.find("from YVisitorsRecordNotlogin y where y.fuserID = '" + memberFid + "'");
	}
	
	/**
	 * 根据会员IDS查询会员及该会员的活跃度
	 * @param memberFids
	 * @return
	 */
	public List getMemberAndActiveByIds(String memberFids,int flag){
		Map<String, Object> map = new HashMap<String, Object>();
		List value = new ArrayList();
		try {
			if(memberFids!=null&& (!memberFids.equals(""))){
				String[] ids = memberFids.split(",");
				for(String id : ids){
					StringBuffer sb = new StringBuffer();
					sb.append("select fname,COUNT(*) from y_visitors_record_notlogin where fuserid = '"+id+"' AND ");
					switch (flag) {
					case 0:
						sb.append("FAccessTime > date_sub(curdate(), INTERVAL 7 DAY) GROUP BY FName");
						break;
					case 1:
						sb.append("FAccessTime > date_sub(curdate(), INTERVAL 1 MONTH) GROUP BY FName");
						break;
					case 2:
						sb.append("FAccessTime > date_sub(curdate(), INTERVAL 3 MONTH) GROUP BY FName");
						break;
					case 3:
						sb.append("FAccessTime > date_sub(curdate(), INTERVAL 6 MONTH) GROUP BY FName");
						break;
					default:
						break;
					}
					
					List list = dao.getListBySql(sb.toString());
					Object[] array = (Object[]) list.get(0);
					map.put("fname", array[0]);
					map.put("count", array[1]);
					value.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
