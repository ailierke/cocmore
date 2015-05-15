package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YBasicPosition;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.model.mysql.YCompanyproduct;
import com.yunzo.cocmore.core.function.service.MembercompanyServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.LatitudeUtils;

/** 
 *Description: <会员公司服务实现类>. <br>
 * @date:2014年11月26日 下午3:02:32
 * @author beck
 * @version V1.0                             
 */
@Service("companyService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class MembercompanyServiceImpl implements MembercompanyServiceI{
	private static final Logger logger = Logger
			.getLogger(MembercompanyServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部公司，并分页")
	public PagingList<YBasicMembercompany> getCompanyPagingList(Integer page,
			Integer pageSize, String groupId, String tradeId, String industryId) {
		// TODO Auto-generated method stub
		logger.info("查询全部公司,并分页...");
		PagingList<YBasicMembercompany> pagingList = new PagingList<YBasicMembercompany>();
		List<Object> list = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicMembercompany y where 1=1");
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicMembercompany y where 1=1");
		if(groupId != null){
			hqlList.append(" and y.YBasicSocialgroups.fid=?");
			hqlCount.append(" and y.YBasicSocialgroups.fid=?");
			list.add(groupId);
		}
		if(tradeId != null && !"".equals(tradeId)){
			hqlList.append(" and y.YBasicTrade.fid = '"+tradeId+"'");
			hqlCount.append(" and y.YBasicTrade.fid = '"+tradeId+"'");
		}
		if(industryId != null && !"".equals(industryId)){
			hqlList.append(" and y.YBasicIndustry.fid = '"+industryId+"'");
			hqlCount.append(" and y.YBasicIndustry.fid = '"+industryId+"'");
		}
		pagingList.setList((List<YBasicMembercompany>)dao.find(hqlList.toString(), page, pageSize,list.toArray()));
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, list.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
 		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询公司")
	public YBasicMembercompany getById(String fid) {
		// TODO Auto-generated method stub
		logger.info("YBasicMembercompany getById(String fid) || id==" + fid);
		return (YBasicMembercompany) dao.findById(YBasicMembercompany.class, fid);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据行业、产业查询")
	public List<YBasicMembercompany> findAllByIndAndTrade(String hql) {
		// TODO Auto-generated method stub
		return (List<YBasicMembercompany>)dao.findAllByHQL(hql);
	}
	
	/**
	 * 根据会员ID查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据会员id查询")
	public List<Map<String, Object>> findAllByUserId(String phone,String businessId){
		List<Map<String, Object>> comList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> psList = null;
		Map<String,Object> map = null;
		Map<String,Object> map1 = null;
		//YBasicMember member = null;
		
		//通过用户电话和商会ID查询出用户ID
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ?",phone);
		if(members != null && members.size() > 0){
			for(YBasicMember member : members){

			//member = members.get(0);
			List<YBasicMembercompany> coms = (List<YBasicMembercompany>) dao.find("from YBasicMembercompany y where y.YBasicMember.fid = ?",member.getFid());
			for(YBasicMembercompany com : coms){
				map = new HashMap<String, Object>(); 
				map.put("companyNature", com.getFcompanyNature());
				map.put("companySize", com.getFcompanyScale());
				map.put("positionId", com.getFcompanyPosition());
				map.put("jobName", com.getYBasicTrade() == null ? null : com.getYBasicTrade().getFname());
				map.put("companyId", com.getFcid());
				map.put("companyAddress", com.getFclocation());
				map.put("companyName", com.getFcname());
				map.put("url", com.getFcurl());
				map.put("companyPhone", com.getFctelphone());
				map.put("companyPosition", com.getFcompanyPosition());
				map.put("companyEmail", com.getFcemail());
				map.put("companyDetail", com.getFcintroduction());
				map.put("companyImageUrl", com.getFcompanyLogo());
				
				//获取公司主营产品
				if(com.getFcproducts()!=null&&com.getFcproducts().indexOf(",") != -1){
					psList = new ArrayList<Map<String,Object>>();
					StringBuffer sb = new StringBuffer("from YCompanyproduct y where ");
					String[] products = com.getFcproducts().split(",");
					for (int i = 0; i < products.length; i++) {
						sb.append("y.fid = ? or ");
					}
					String hql = sb.toString().substring(0,sb.toString().lastIndexOf("or"));
					List<YCompanyproduct> ps = (List<YCompanyproduct>) dao.find(hql,products);
					for(YCompanyproduct p : ps){
						map1 = new HashMap<String, Object>();
						map1.put("productId", p.getFid());
						map1.put("productLogo", p.getFlogoImage());
						map1.put("productName", p.getFname());
						map1.put("productDetail", p.getFdescription());
						psList.add(map1);
					}
				}
				
				map.put("productList", psList);
				comList.add(map);
			}
			}
		}
		return comList;
	}

	@Override
	@SystemServiceLog(description = "新增公司")
	public void save(YBasicMembercompany demo) {
		demo.setFcompanyLatitude(LatitudeUtils.getGeocoderLatitude(demo.getFclocation()));
		dao.save(demo);
	}
	
	@Override
	@SystemServiceLog(description = "修改公司")
	public void update(YBasicMembercompany demo) {
		//设置经纬度
		demo.setFcompanyLatitude(LatitudeUtils.getGeocoderLatitude(demo.getFclocation()));
		
		//获取原有产品对象集合
		List<YCompanyproduct> list = findProductByComID(demo.getFcid());
		Set<YCompanyproduct> st = demo.getCompanyproducts();
		//循环删除前台选中删除的条款记录
		for(YCompanyproduct cla : list){
			boolean b = true;
			for(YCompanyproduct clas : st){
				if(cla.getFid().equals(clas.getFid())){
					b = false;
					break;
				}
			}
			if(b){
				dao.updateBySQL("delete from y_companyproduct where FID='" + cla.getFid() +"'");
			}
		}
		dao.update(demo);
	}
	
	/**
	 * 根据公司ID查询主营产品
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据公司ID查询主营产品")
	public List<YCompanyproduct> findProductByComID(String fid){
		List<YCompanyproduct> list = null;
		list = (List<YCompanyproduct>) dao.findAllByHQL("from YCompanyproduct y where y.YBasicMembercompany.fcid = '" + fid + "'");
		return list;
	}
	
	/**
	 * 根据会员ID查询公司
	 * @param fid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据会员ID查询公司")
	public YBasicMembercompany getByMemberId(String fid){
		YBasicMembercompany obj = null;
		 List<YBasicMembercompany> list = (List<YBasicMembercompany>) dao.findAllByHQL("from YBasicMembercompany y where y.YBasicMember.fid = '" + fid + "'");
		if(list!=null&&list.size() > 0){
			obj = list.get(0);
			List<YCompanyproduct> products = (List<YCompanyproduct>) dao.findAllByHQL("from YCompanyproduct y where y.YBasicMembercompany.fcid = '" + obj.getFcid() + "'");
			Set<YCompanyproduct> set = new HashSet<YCompanyproduct>(products);
			obj.setCompanyproducts(set);
		}
		return obj;
	}
	
	
	/**
	 * 根据产品ID获取产品
	 * @param fid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据产品ID获取产品")
	public YCompanyproduct getProductById(String fid){
		return (YCompanyproduct) dao.get(YCompanyproduct.class, fid);
	}

	@Override
	public void saveByApp(YBasicMembercompany company) {
		dao.save(company);
	}

	@Override
	public void updateByApp(YBasicMembercompany company) {
		dao.update(company);
	}
}
