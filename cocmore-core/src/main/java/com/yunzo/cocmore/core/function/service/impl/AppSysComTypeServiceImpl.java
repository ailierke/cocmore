package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.model.mysql.AppSystemCommandLog;
import com.yunzo.cocmore.core.function.model.mysql.AppSystemCommandType;
import com.yunzo.cocmore.core.function.model.mysql.SystemConfig;
import com.yunzo.cocmore.core.function.service.AppSysComTypeServiceI;

/**
 * Description: <手机执行命令类型service接口实现类>. <br>
 * @date:2015年3月12日 下午4:05:21
 * @author beck
 * @version V1.0
 */
@Service("appSCTypeService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class AppSysComTypeServiceImpl implements AppSysComTypeServiceI{
	private static final Logger logger = Logger.getLogger(AppSysComTypeServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	public void save(AppSystemCommandType demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}

	@Override
	public void update(AppSystemCommandType demo) {
		// TODO Auto-generated method stub
		dao.update(demo);
	}

	@Override
	public void delete(AppSystemCommandType demo) {
		// TODO Auto-generated method stub
		dao.delete(demo);
	}

	@Override
	public AppSystemCommandType getById(String id) {
		// TODO Auto-generated method stub
		return (AppSystemCommandType) dao.get(AppSystemCommandType.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String fmid = (String) map.get("userName");		//电话
		if(fmid == null || fmid.equals("")){
			return null;
		}
		String hql = "from AppSystemCommandLog where fmid = '" + fmid + "'";
		List<AppSystemCommandLog> logs = (List<AppSystemCommandLog>) dao.find(hql);
		
		if(logs!=null && logs.size()>0){
			return null;
		}else{
			Map<String, Object> result = new HashMap<String, Object>();
			List<Map<String, Object>> exeList = new ArrayList<Map<String,Object>>();
			Map<String, Object> ap = new HashMap<String, Object>();
			List<SystemConfig> list = (List<SystemConfig>) dao.find("from SystemConfig where fkey = 'appSystemCommandType_time'");
			SystemConfig sc = null;
			if(list != null && list.size() > 0){
				sc = list.get(0);
			}
			result.put("fTimer", sc.getFvalue());
			
			List<AppSystemCommandType> aps = (List<AppSystemCommandType>) dao.find("from AppSystemCommandType");
			for(AppSystemCommandType temp : aps){
				ap.put("exeCode", temp.getFid());
				ap.put("exeType", temp.getFascTypeFlag());
				ap.put("exeInfo", temp.getFcomment());
				exeList.add(ap);
			}
			result.put("exeList", exeList);
			
			return result;
		}
	}
}
