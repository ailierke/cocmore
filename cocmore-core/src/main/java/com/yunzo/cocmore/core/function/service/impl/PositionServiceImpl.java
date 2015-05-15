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
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.service.PositionServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.core.function.vo.MemberVo;

/** 
 *Description: <职位服务实现类>. <br>
 * @date:2014年11月25日 下午4:34:54
 * @author beck
 * @version V1.0                             
 */
@Service("posService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class PositionServiceImpl implements PositionServiceI{
	
	private static final Logger logger = Logger
			.getLogger(PositionServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部职位，并分页")
	public PagingList<YBasicPosition> findAll(String searchName,Integer start,Integer limit,String groupId,String orgId) {
		logger.info("List<YBasicPosition> findAll()");
		PagingList<YBasicPosition> pagingList = new PagingList<YBasicPosition>();
		String hql = "from YBasicPosition y where 1=1 ";
		//不分页就是查询全部
		if(start == null && limit == null){
			if(!groupId.equals("")){
				hql += " and y.fsocialGroupsId = '"+groupId+"' order by  y.fseq";
			}
			pagingList.setList((List<YBasicPosition>)dao.find(hql));
			return pagingList;
		}
		
		//组织查询
//		if(!orgId.equals("")){
//			hql += " and y.forganizationId = '"+orgId+"'";
//		}
		//团体查询
		if(!groupId.equals("")){
			hql += " and y.fsocialGroupsId = '"+groupId+"'order by  y.fseq ";
		}
		//条件查询
		if(searchName != null){
			hql += " and y.fname like '%" + searchName + "%'";
		}
		pagingList.setList((List<YBasicPosition>) dao.find(hql, start, limit, null));
		
		//获取总条数
		List<YBasicPosition> list = (List<YBasicPosition>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询职位")
	public YBasicPosition getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicOrganization getById(String fid) || id==" + fid);
		return (YBasicPosition) dao.findById(YBasicPosition.class, fid);
	}
	
	/**
	 * 查询上级职位，排除本身职位
	 * @param fid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询上级职位")
	public List<YBasicPosition> findSuper(String selfFid,String groupId,String orgId){
		logger.info("List<YBasicPosition> findSuper()");
		String hql = "from YBasicPosition y where 1=1 ";
		if(selfFid != null && !"".equals(selfFid)){
			hql = hql + "and y.fid != '" + selfFid + "'";
		}
		if(orgId != null && !"".equals(orgId)){
			hql = hql + "and y.forganizationId = '"+orgId+"'";
		}
		if(groupId != null && !"".equals(groupId)){
			hql = hql + "and y.fsocialGroupsId = '" + groupId + "'";
		}
		return (List<YBasicPosition>)dao.find(hql);
	}

	@Override
	@SystemServiceLog(description = "新增职位")
	public void save(YBasicPosition demo) {
		// TODO Auto-generated method stub
		demo.setFhide(1);
		try {
			List list = (List) dao.getListBySql("select max(y.fversion) from y_basic_position y where FSocialGroupsID = '" + demo.getFsocialGroupsId() + "'");
			if(list.get(0)!=null){
				int maxVersion = (int) list.get(0);
				demo.setVersion(maxVersion + 1);
			}else{
				demo.setVersion(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dao.save(demo);
	}

	@Override
	@SystemServiceLog(description = "删除职位")
	public void delete(YBasicPosition demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	@SystemServiceLog(description = "修改职位")
	public void update(YBasicPosition demo) {
		// TODO Auto-generated method stub
		//获取当前最新版本号
		List list = (List)dao.find("select max(y.fversion) from YBasicentriesMemberdistribution y inner join y.YBasicPosition p  "
				+ "where p.fsocialGroupsId = ?", demo.getFsocialGroupsId()); 
		if(list.get(0) == null ){
			demo.setVersion(1);
		}else{
			demo.setVersion((int)list.get(0) + 1);
			List<YBasicentriesMemberdistribution> yms = (List<YBasicentriesMemberdistribution>) dao.find("from YBasicentriesMemberdistribution y where y.YBasicPosition.fid = ? order by y.fversion desc", demo.getFid());
			if(yms!=null && yms.size()>0){
				YBasicentriesMemberdistribution ym = yms.get(0);
				ym.setFversion((int)list.get(0) + 1);
				dao.update(ym);
				dao.flush();
			}
		}
		dao.update(demo);
	}

	@Override
	@SystemServiceLog(description = "hql查询职位")
	public List<YBasicPosition> findByHql(String hql) {
		
		return (List<YBasicPosition>)dao.findAllByHQL(hql);
	}
	
	/**
	 * 检查通讯录成员是否有改动
	 * @param businessId
	 * @param CacheVersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "检查通讯录成员是否有改动")
	public int getPositionByVersion(String businessId,int CacheVersion){
		List list = (List)dao.find("select max(y.fversion) from YBasicentriesMemberdistribution y inner join y.YBasicPosition p  "
				+ "where p.fsocialGroupsId = ?", businessId);
		if(list!=null&&list.size()>0){
			int maxVersion = (int) list.get(0);
			if(maxVersion > CacheVersion){
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * 根据团体ID和版本号查询职位是否有更新
	 * @param businessId
	 * @param CacheVersion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据团体id和版本号查询职位是否有更新")
	public int getPositionByPositionVersion(String businessId,int CacheVersion){
		
		List<Integer> list  = (List<Integer>) dao.getListBySql("select max(y.fversion) from y_basic_position y where y.FSocialGroupsID = '" + businessId + "'" );
		if(list!=null&&list.size()>0){
			int maxVersion = list.get(0);
			if(maxVersion > CacheVersion){
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * 根据团体ID、版本号、页面大小查询，获取通讯录
	 * @param businessId
	 * @param CacheVersion
	 * @return
	 */
	@SystemServiceLog(description = "根据团体ID、版本号、页面大小查询，获取通讯录")
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPositionByVersionAndPageSize(String businessId,int cacheVersion,int pageSize){
		List<MemberVo> valueList = new ArrayList<MemberVo>();		//返回结果集合	
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "";
		StringBuffer inSql = new StringBuffer();
		StringBuffer beforeSql = new StringBuffer("SELECT yyyy.FName AS n1,yyyy.FMobilePhone AS mid,yyyy.FSex as sex,yyyy.FMobilePhone as tel,"
				+ "yyyy.FIMkey as n2,yyyy.FheadImage AS n3 ,yyyy.FCName AS n4,yyyy.FCompanyPosition AS n5,"
				+ "td.FName as n6,yyyy.fPositionName AS n7,yyyy.FVersion AS n8,yyyy.FSeq AS n9,yyyy.FCID AS n10,"
				+ "yyyy.FIsHidePhone AS n11,yyyy.FpositionID AS g1 from "
				+ "(SELECT yyy.*,py.FCName,py.FCompanyPosition,py.FCID,py.FTradeID from "
				+ "(select m.FName,yy.FMemberID,m.FSex,m.FMobilePhone,m.FHeadImage,yy.FVersion,yy.FSeq,m.FIsHidePhone,yy.FpositionID,yy.FName as fPositionName,i.FIMkey,m.FBillState,m.FIsHidePhone as hide "
				+ "from (");
		
		inSql.append("select y.FMemberID,y.FpositionID,y.FVersion,y.FSeq,p.FName from y_basicentries_memberdistribution y left join "
				+ "y_basic_position p on y.FPositionID = p.fid where p.FSocialGroupsID =  '"+businessId+"' order by y.FVersion");
		
		StringBuffer alterSql = null;
		try {
			//判断是否传入版本好
			if(cacheVersion == 0){
				alterSql = new StringBuffer(") yy "
						+ "right JOIN y_basic_member m ON m.FID = yy.FMemberID "
						+ "left JOIN y_basic_imaccount i ON i.fimtel = m.FMobilePhone where m.FSocialGroupsID='"+businessId+"' ) yyy "
						+ "LEFT JOIN y_basic_membercompany py ON py.FMemberID = yyy.FMemberID) yyyy "
						+ "LEFT JOIN y_basic_trade td ON td.FID = yyyy.FTradeID group by mid order by yyyy.FVersion  limit 0,"+pageSize+"");
			}else{
				alterSql = new StringBuffer(") yy "
						+ "right JOIN y_basic_member m ON m.FID = yy.FMemberID "
						+ "left JOIN y_basic_imaccount i ON i.fimtel = m.FMobilePhone where m.FSocialGroupsID='"+businessId+"' ) yyy "
						+ "LEFT JOIN y_basic_membercompany py ON py.FMemberID = yyy.FMemberID) yyyy "
						+ "LEFT JOIN y_basic_trade td ON td.FID = yyyy.FTradeID group by mid HAVING yyyy.FVersion > " + cacheVersion + " order by yyyy.FVersion  limit 0,"+pageSize+"");
			}
			sql = beforeSql.append(inSql).append(alterSql).toString();
			List<MemberVo> list = (List<MemberVo>) dao.getListBySqlVO(sql, MemberVo.class);
			//处理用户头像的逗号
			if(list!=null&&list.size()>0){
				for(MemberVo o : list){
					if(o.getN3()!=null && !o.getN3().equals("")){
						if(o.getN3().indexOf(",") != -1){
							String img = o.getN3().substring(0,o.getN3().length() - 1);
							o.setN3(img);
						}
					}
					valueList.add(o);
				}
			}
			map.put(ResponseCode.MSGR.msg(), valueList);
			map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		
		return map;
	}
	
	
	/**
	 * 获取通讯录职位
	 * @param businessId
	 * @param CacheVersion
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "获取通讯录职位")
	public List<Map<String, Object>> getPositionInfo(String businessId,int cacheVersion){
		List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();		//返回结果集合
		Map<String, Object> value = null;
		
		List<YBasicPosition> pts = null;
		String hql = "";
		try {
			if(cacheVersion == 0){
				hql = "select yy from YBasicPosition yy where yy.fid in (select y.fid from YBasicPosition y where y.fsocialGroupsId = ? and y.fbillState = 5 order by y.version) order by yy.fseq";
				pts = (List<YBasicPosition>) dao.find(hql, businessId);
			}else{
				hql = "select yy from YBasicPosition yy where yy.fid in (select y.fid from YBasicPosition y where y.fsocialGroupsId = ? and y.fbillState = 5 and y.version > ? order by y.version) order by yy.fseq";
				pts = (List<YBasicPosition>) dao.find(hql, businessId,cacheVersion);
			}
			
			if(pts == null || pts.size() == 0){
				logger.info("没有职位信息");
				return null;
			}else{
				for(YBasicPosition obj : pts){
					value = new HashMap<String, Object>();
					value.put("g1", obj.getFid());
					value.put("g2", obj.getFname());
					value.put("g3", obj.getVersion());
					value.put("g4", obj.getFseq());
					value.put("g5", obj.getFhide());
					valueList.add(value);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return valueList;
	}
}
