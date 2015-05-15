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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMemberbymemberhide;
import com.yunzo.cocmore.core.function.service.MBMHideServiceI;

/**
 * Description: <>. <br>
 * @date:2014年12月17日 上午9:52:43
 * @author beck
 * @version V1.0
 */
@Service("mbmService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class MBMHideServiceImpl implements MBMHideServiceI{
	
	private static final Logger logger = Logger
			.getLogger(MBMHideServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	/**
	 * 根据传入的发起隐藏人的ID查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据传入的发起隐藏人的ID查询")
	public List<Map<String, Object>> findAllByInId(String phone,String businessId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> objList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		YBasicMember member = null;
		
		//通过用户电话和商会ID查询出用户ID
		List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ? and y.YBasicSocialgroups.fid = ?",phone,businessId);
		if(members!=null && members.size()>0){
			member = members.get(0);
			//查询用户隐私设置通讯录列表
			List<YBasicMemberbymemberhide> list = (List<YBasicMemberbymemberhide>) dao.findAllByHQL("from YBasicMemberbymemberhide y where y.YBasicMemberByFinMemberId.fid = '" +member.getFid() + "'");
			for(YBasicMemberbymemberhide temp : list){
				map = new HashMap<String, Object>();
				map.put("m", temp.getYBasicMemberByFoutMemberId().getFid());
				map.put("h", 0);
				objList.add(map);
			}
		}
		
		return objList;
	}

	@Override
	@SystemServiceLog(description = "新增会员对会员黑名单")
	public void save(YBasicMemberbymemberhide demo) {
		// TODO Auto-generated method stub
		dao.save(demo);
	}
	
	@SystemServiceLog(description = "根据发起人和隐藏人查询")
	public YBasicMemberbymemberhide findByInOut(String in,String out){
		@SuppressWarnings("unchecked")
		List<YBasicMemberbymemberhide> list = (List<YBasicMemberbymemberhide>) dao.find("from YBasicMemberbymemberhide y where y.YBasicMemberByFinMemberId.fid = ? and "
				+ "y.YBasicMemberByFoutMemberId.fid = ?", in,out);
		return list.get(0);		
	}
	
	/**
	 * 删除
	 * @return
	 */
	@SystemServiceLog(description = "删除会员对会员黑名单")
	public void delete(YBasicMemberbymemberhide demo){
		dao.delete(demo);
	}
	
	/**
	 * 设置通讯录隐私
	 * @param infoMap
	 */
	public void setContactsPrivacy(String infoMap){
		// Json字符串转换为Map
		ObjectMapper mapper = new ObjectMapper();
		YBasicMemberbymemberhide mbm = null;
		YBasicMember member = null;
		YBasicMember outObj = null;
		List<YBasicMember> list = null;
		try {
			Map<String, Object> jsonObj = mapper.readValue(infoMap, Map.class);
			// 获取电话号码和商会ID
			String mid = (String) jsonObj.get("mid");
			String businessId = (String) jsonObj.get("businessId");
			// 获取发起人
			List<YBasicMember> members =  (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ? and y.YBasicSocialgroups.fid = ?",mid,businessId);
			if(members != null && members.size() > 0){
				member = members.get(0);
			}
			
			JsonNode jsonObjNode = mapper.readTree(infoMap);
			JsonNode memList = jsonObjNode.path("userList");
	
			for (int i = 0; i < memList.size(); i++) {
	
				mbm = new YBasicMemberbymemberhide();
				JsonNode node = memList.get(i);
				String out = null;
				String isHide = null;
				// 获取电话号码和状态
				out = node.get("mid").textValue();
				isHide = node.get("isHide").textValue();
	
				// 获取电话号码对应的会员
				list = (List<YBasicMember>) dao.find("from YBasicMember y where y.fmobilePhone = ? and y.YBasicSocialgroups.fid = ?",out,businessId);
				if(list!=null && list.size()>0){
					outObj = list.get(0);
				}
				
				// 通讯录隐藏状态(0影藏，1显示)
				if (isHide.equals("0")) {				
					mbm.setYBasicMemberByFinMemberId(member);
					mbm.setYBasicMemberByFoutMemberId(outObj);
					dao.save(mbm);		// 添加到会员对会员隐藏表
				} else {
					YBasicMemberbymemberhide obj = findByInOut(member.getFid(),outObj.getFid());
					dao.delete(obj);	// 删除会员对会员隐藏表的关系数据
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
