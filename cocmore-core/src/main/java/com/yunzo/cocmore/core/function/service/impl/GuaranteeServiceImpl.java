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
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.service.GuaranteeServiceI;
import com.yunzo.cocmore.core.function.util.ResponseCode;

/**
 * @author：jackpeng
 * @date：2014年12月17日上午3:22:37
 * 供应、担保关系service实现类
 */
@Service("guaranteeService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class GuaranteeServiceImpl implements GuaranteeServiceI {
	
	private static final Logger logger = Logger.getLogger(GuaranteeServiceImpl.class);

	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	
	@Override
	public void save(YSupplyGroup guarantee) {
		// TODO Auto-generated method stub
		dao.save(guarantee);
	}
	
	@Override
	public void update(YSupplyGroup guarantee) {
		dao.update(guarantee);
	}
	
	@Override
	public List<YSupplyGroup> findByHql(String hql) {
		return (List<YSupplyGroup>)dao.findAllByHQL(hql);
	}
	
	@SystemServiceLog(description = "修改我的供需认证")
	public Map<String, Object> updateByGroup(String supplyId,List<String> list){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			List<YSupplyGroup> supplys=new ArrayList<YSupplyGroup>();
			supplys=(List<YSupplyGroup>)dao.findAllByHQL("from YSupplyGroup y where y.YBasicSocialgroupssupply.fid='"+supplyId+"'");
			for(String id:list){
				boolean b=true;
				for(YSupplyGroup s:supplys){
					if(id.equals(s.getYBasicAssurancecontent().getFid())){
						if("17".equals(s.getIspass())){
							s.setIspass("15");
							dao.update(s);
						}
						b=false;
					}
				}
				if(b){
					YBasicSocialgroupssupply YBasicSocialgroupssupply=new YBasicSocialgroupssupply() ;
					YBasicSocialgroupssupply.setFid(supplyId);
					YBasicAssurancecontent YBasicAssurancecontent=new YBasicAssurancecontent();
					YBasicAssurancecontent assurance=(YBasicAssurancecontent)dao.findById(YBasicAssurancecontent.getClass(), id);
					
					YSupplyGroup su=new YSupplyGroup();
					su.setUpdatetime(new Date());
					su.setIspass("15");
					su.setYBasicAssurancecontent(assurance);
					su.setYBasicSocialgroupssupply(YBasicSocialgroupssupply);
					su.setGroupid(assurance.getYBasicSocialgroups().getFid());
					dao.save(su);
				}
			}
			map.put("responseCode", ResponseCode.SUCCESS.value());
			map.put("message", ResponseCode.SUCCESS.msg());
		} catch (Exception e) {
			map.put("responseCode", ResponseCode.EXCEPTION.value());
			map.put("message", ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		}
		return map;
	}

	@Override
	@SystemServiceLog(description = "根据id查询供需认证")
	public YSupplyGroup getById(String fid) {
		YSupplyGroup group=new YSupplyGroup();
		group=(YSupplyGroup)dao.findById(YSupplyGroup.class, fid);
		return group;
	}

	
}
