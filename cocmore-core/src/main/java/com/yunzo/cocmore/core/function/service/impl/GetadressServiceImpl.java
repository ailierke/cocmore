package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicGetadress;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.service.GetadressServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;

@Service("getadressService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class GetadressServiceImpl implements GetadressServiceI {

	
	private static final Logger logger = Logger
			.getLogger(GetadressServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	
	@Override
	@SystemServiceLog(description = "新增收货地址")
	public void save(YBasicGetadress getadress) {
		dao.save(getadress);
	}

	@Override
	@SystemServiceLog(description = "修改收货地址")
	public void update(YBasicGetadress getadress) {
		dao.update(getadress);
	}

	@Override
	@SystemServiceLog(description = "删除收货地址")
	public void delete(YBasicGetadress getadress) {
		dao.delete(getadress);
	}

	@Override
	@SystemServiceLog(description = "根据id查询收货地址")
	public YBasicGetadress getById(String fid) {
		return (YBasicGetadress)dao.findById(YBasicGetadress.class, fid);
	}

	@Override
	@SystemServiceLog(description = "获取我的收货地址列表")
	public List<Map<String, Object>> findByMemberList(List<YBasicMember> memberlist) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<YBasicGetadress> list=new ArrayList<YBasicGetadress>();
		try {
			StringBuffer hqlbuffer = new StringBuffer("from YBasicGetadress as y where ");
			for(int i=0;i<memberlist.size();i++){
				if(i!=0){
					hqlbuffer.append(" or ");
				}
				hqlbuffer.append("y.YBasicMember.fid='"+memberlist.get(i).getFid()+"'");
			}
			list =(List<YBasicGetadress>)dao.findAllByHQL(hqlbuffer.toString());
			for(YBasicGetadress adress:list){
				Map<String ,Object> map=new HashMap<String, Object>();
				map.put("consigneeAddress", adress.getFadress());//				consigneeAddress	String	收货地址
				map.put("consignee", adress.getFharvestingName());//				consignee String	收货人
				map.put("consigneePhone", adress.getFharvestPhone());//				consigneePhone	String	收货人电话
				map.put("provincialId", adress.getYBasicProvince().getFid());//				provincialId	String	省份ID
				map.put("cityId", adress.getYBasicCity().getFid());//				cityId	String	城市ID
				map.put("countryId", adress.getYBasicCounty().getFid());//				countryId	String	县ID
				map.put("provincialName", adress.getYBasicProvince().getFname());//				provincialName	String	省份名
				map.put("cityName", adress.getYBasicCity().getFname());//				cityName	String	城市名
				map.put("countryName", adress.getYBasicCounty().getFname());//				countryName	String	县名
				map.put("consigneeAddressId", adress.getFid());//				consigneeAddressId	String	收货信息ID
				resultList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@Override
	@SystemServiceLog(description = "根据会员id查询会员的收货地址")
	public List<YBasicGetadress> findByMember(List<YBasicMember> memberlist) {
		List<YBasicGetadress> list=new ArrayList<YBasicGetadress>();
		try {
			StringBuffer hqlbuffer = new StringBuffer("from YBasicGetadress as y where ");
			for(int i=0;i<memberlist.size();i++){
				if(i!=0){
					hqlbuffer.append(" or ");
				}
				hqlbuffer.append("y.YBasicMember.fid='"+memberlist.get(i).getFid()+"'");
			}
			list =(List<YBasicGetadress>)dao.findAllByHQL(hqlbuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@SystemServiceLog(description = "hql查询收货地址")
	public List<YBasicGetadress> findByHql(String hql) {
		List<YBasicGetadress> list=new ArrayList<YBasicGetadress>();
		list=(List<YBasicGetadress>)dao.findAllByHQL(hql);
		return list;
	}

	@Override
	public Map<String,Object> saveAdress(String memberId, String consigneeAddress,
			String consignee, String consigneePhone, String provincialId,
			String cityId, String countryId) {
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			YBasicMember member=new YBasicMember();
			member=(YBasicMember)dao.findById(YBasicMember.class, memberId);
			
			YBasicCity city=new YBasicCity();
			city=(YBasicCity)dao.findById(YBasicCity.class, cityId);
			YBasicProvince province=new YBasicProvince();
			province=(YBasicProvince)dao.findById(YBasicProvince.class, provincialId);
			
			YBasicCounty county=new YBasicCounty();
			county=(YBasicCounty)dao.findById(YBasicCounty.class, countryId);
			if(null!=member&&null!=city&&null!=province&&null!=county){
				YBasicGetadress getadress=new YBasicGetadress();
				getadress.setFadress(consigneeAddress);
				getadress.setFharvestingName(consignee);
				getadress.setFharvestPhone(consigneePhone);
				getadress.setFupdatetime(new Date());
				getadress.setYBasicMember(member);
				getadress.setYBasicProvince(province);
				getadress.setYBasicCity(city);
				getadress.setYBasicCounty(county);
				dao.save(getadress);
				map.put("responseCode", ResponseCode.SUCCESS.value());
				map.put("message", ResponseCode.SUCCESS.msg());
			}else{
				map.put("responseCode", ResponseCode.EXCEPTION.value());
				map.put("message", ResponseCode.EXCEPTION.msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
