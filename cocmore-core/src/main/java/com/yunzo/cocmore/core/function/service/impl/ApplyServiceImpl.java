package com.yunzo.cocmore.core.function.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.service.ApplyService;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.MD5Util;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;

/**
 * @author：jackpeng
 * @date：2014年12月9日上午10:56:37
 * 入会申请service实现类
 */
@Service("applyService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ApplyServiceImpl implements ApplyService {
	
	private static final Logger logger = Logger.getLogger(ApplyServiceImpl.class);
	
	//加密前段
	private static final String BEGINSTRING="yunzo";
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部入会申请，并分页")
	public PagingList<YInitiationApply> getAllApplyPagingList(Integer page,Integer pageSize,String groupsId, String name) {
		// TODO Auto-generated method stub
		logger.info("查询全部入会申请，并分页...");
		PagingList<YInitiationApply> pagingList = new PagingList<YInitiationApply>();
		List<Object> list = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YInitiationApply y where 1=1");
		StringBuffer hqlCount = new StringBuffer("select count(0) from YInitiationApply y where 1=1");
		if(groupsId != null && !groupsId.equals("")){
			hqlList.append(" and y.fgroupsId=?");
			hqlCount.append(" and y.fgroupsId=?");
			list.add(groupsId);
		}
		if(name != null){
			hqlList.append(" and y.fname like '%"+name+"%'");
			hqlCount.append(" and y.fname like '%"+name+"%'");
		}
		//获得此页数据
		pagingList.setList((List<YInitiationApply>)dao.find(hqlList.toString(), page, pageSize,list.toArray()));
		//获得总条数
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, list.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询入会申请")
	public YInitiationApply getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YInitiationApply getById(String id) id="+id);
		return (YInitiationApply) dao.findById(YInitiationApply.class, id);
	}

	@Override
	@SystemServiceLog(description = "新增入会申请")
	public void save(YInitiationApply apply) {
		// TODO Auto-generated method stub
		dao.save(apply);
	}

	@Override
	@SystemServiceLog(description = "删除入会申请")
	public void delete(YInitiationApply apply) {
		// TODO Auto-generated method stub
		dao.delete(apply);
	}
	
	@Override
	public void update(YInitiationApply apply) {
		// TODO Auto-generated method stub
		dao.update(apply);
	}

	@Override
	@SystemServiceLog(description = "入会申请 通过 or 不通过")
	public void update(String fids,int state,String fcompanyNames,String fcompanyPositions,String groupId) {
		YInitiationApply apply = null;
		String content = ""; 	//推送内容
		YBasicSocialgroups sp = (YBasicSocialgroups) dao.get(YBasicSocialgroups.class, groupId);
		
		String[] ids = fids.split(",");
			// 通过
			if(state == 16){
				String[] names = fcompanyNames.split(",");
				String[] positions =  fcompanyPositions.split(",");
				for(int i = 0; i < ids.length ;i++){
					apply = (YInitiationApply) dao.get(YInitiationApply.class, ids[i]);
					
					//创建会员对象
					YBasicMember member = new YBasicMember();
					member.setFid(UUID.randomUUID().toString());
					member.setFmobilePhone(apply.getFphone());
					member.setFpassword(MD5Util.md5(BEGINSTRING + "888888"));
					//设置商会对象
					member.setYBasicSocialgroups(sp);
					member.setFnickName(apply.getFname());
					member.setFnativePlace(apply.getFnativePlace());
					member.setFbillState(5);
					dao.save(member);
					dao.flush();
					
					//创建该会员公司信息
					YBasicMembercompany com = new YBasicMembercompany();
					com.setFcid(UUID.randomUUID().toString());
					com.setYBasicMember(member);
					com.setFcname(names[i]);
					com.setFcompanyPosition(positions[i]);
					dao.save(com);
					dao.flush();
					
					apply.setFstate(state);
					dao.update(apply);
					dao.flush();
					
					//推送消息通知申请人
					content = "恭喜" + apply.getFname() + "申请加入" + sp.getFname() + "成功";
					try {
						push(content,apply.getFphone());
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				
			}else if(state == 17){
				for(String id : ids){
					apply = (YInitiationApply) dao.get(YInitiationApply.class, id);
					
					apply.setFstate(state);
					dao.update(apply);
					dao.flush();
					//推送消息通知申请人
					content = "抱歉" + apply.getFname() + "申请加入" + sp.getFname() + "失败";
					try {
						push(content,apply.getFphone());
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// 存入到消息通知表
				}
			}
	
	}
	
	
	/**
	 * 推送成功加入商会的
	 * @param name
	 * @param groupId
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "推送会员申请加入商会的信息")
	public void push(String content,String phone) throws NumberFormatException, Exception{
		List<YAppdevice> memberAppDeviceList = (List<YAppdevice>) dao.findAllByHQL("from  YAppdevice device where device.fuserName = '"+ phone + "'");
		//如果存在此用户的clientId信息就推送
		if(memberAppDeviceList!=null&&memberAppDeviceList.size()>0){
			String logo ="";
			String logoUrl="";
			String transmissionContent="";
			int transmissionType =1;
			for(YAppdevice yAppdevice:memberAppDeviceList){
			//使用激活应用模板
			PushToListMessage.sendDownLoadMessageToSingel(yAppdevice.getFdeviceId(), new Integer(yAppdevice.getFappChannelNo()), TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", content, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
			}
		}
	}
	
}
