package com.yunzo.cocmore.core.function.service.impl;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationEncodingrules;
import com.yunzo.cocmore.core.function.service.GetNumberService;
@Component("getNumberService")
@Transactional
public class GetNumberServiceImpl implements GetNumberService {
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	static DecimalFormat df = new DecimalFormat("000000000");//不足9位补0
	
	@Override
	@Transactional(readOnly=true)
	public Map<String,Object> checkExist(String fid) {
		Map<String,Object> map= new HashMap<String,Object>();
		boolean flag = false;
		YSystemconfigurationEncodingrules ySystemconfigurationEncodingrules = (YSystemconfigurationEncodingrules) dao.get(YSystemconfigurationEncodingrules.class,fid);
		if(null!=ySystemconfigurationEncodingrules){
			flag = true;
			String serialNumber="";
			if(ySystemconfigurationEncodingrules.getFserialNumber()!=null&&!ySystemconfigurationEncodingrules.getFserialNumber().equals("")){
				serialNumber = df.format(new BigInteger(ySystemconfigurationEncodingrules.getFserialNumber()).add(new BigInteger("1")));
			}else{
				serialNumber="000000001";
			}
			if(ySystemconfigurationEncodingrules.getFdateValue().equals("是")){
				serialNumber = fid + "-" + new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "-" + serialNumber;
			}else{
				serialNumber = fid + "-" + serialNumber;
			}
		    
			map.put("serialNumber",serialNumber);
		}
		map.put("success", flag);
		return map;
	}

	@Override
	public void addSerialNumber(String fid) {
		String fSerialNumber ="";//流水号
		YSystemconfigurationEncodingrules ySystemconfigurationEncodingrules = (YSystemconfigurationEncodingrules) dao.get(YSystemconfigurationEncodingrules.class,fid);
		if(null!=ySystemconfigurationEncodingrules){
			fSerialNumber = ySystemconfigurationEncodingrules.getFserialNumber();
			if(null==fSerialNumber||"".equals(fSerialNumber)){
				ySystemconfigurationEncodingrules.setFserialNumber("000000001");
			}else{
				ySystemconfigurationEncodingrules.setFserialNumber(df.format(new BigInteger(fSerialNumber).add(new BigInteger("1"))));
			}
			dao.update(ySystemconfigurationEncodingrules);
		}
	}
}
