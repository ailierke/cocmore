package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
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
import com.yunzo.cocmore.core.function.model.mysql.Informationview;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinformrecord;
import com.yunzo.cocmore.core.function.model.mysql.YInitiationApply;
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.service.InformationviewServiceI;

/**
 * 通知视图 接口实现类
 * @author yunzo
 *
 */

@Service("InformationviewService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class InformationviewServiceImpl implements InformationviewServiceI {
	private static final Logger logger = Logger
			.getLogger(InformationviewServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	@SystemServiceLog(description = "hql查询通知")
	public List<Informationview> getByHql(String hql) {
		List<Informationview> list=(List<Informationview>)dao.findAllByHQL(hql);
		return list;
	}

	@Override
	@SystemServiceLog(description = "查询全部通知，并分页")
	public List<Map<String, Object>> findByMemberList(List<YBasicMember> memberlist,String messageId,int pageSize) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			List<Informationview> views=new ArrayList<Informationview>();
			
			StringBuffer hqlbuffer = new StringBuffer("from Informationview as v where ");
			
			if(null!=messageId&&!"".equals(messageId)){
				Informationview view=(Informationview)dao.findById(Informationview.class, messageId);
				//hql="from Informationview as v where v.memberId='"+memberId+"' and v.newsDate<='"+view.getNewsDate()+"' order by v.newsDate desc";				
				hqlbuffer.append(" v.newsDate<'"+view.getNewsDate()+"' and (");

			}else{
				hqlbuffer.append(" (");
			}
			
			for(int i=0;i<memberlist.size();i++){
				if(i!=0){
					hqlbuffer.append(" or ");
				}
				hqlbuffer.append(" v.memberId='"+memberlist.get(i).getFid()+"'");
			}
			
			hqlbuffer.append(")  order by v.newsDate desc");
			System.out.println(hqlbuffer.toString());
			views=(List<Informationview>)dao.find(hqlbuffer.toString(), 0, pageSize, null);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Informationview v:views){
				Map<String,Object> resultObject = new HashMap<String, Object>();
				resultObject.put("messageType",v.getType());//messageType	int	消息类型（1商会通知，2供需申请商会通知｛只有供需消息被一个或多个商会拒绝才能出现在此处｝，3申请加入商会通知）
				resultObject.put("messageId", v.getFid()); //messageId	String	消息ID
//				String img[]=  v.getImageUrl().split(",");
//				resultObject.put("messageImageUrl", img[0]);//messageImageUrl	String	消息图片地址
//				resultObject.put("messageContent", v.getContent());//messageContent	String	消息内容
				if(null!=v.getNewsDate()&&!"".equals(v.getNewsDate())){
					resultObject.put("messageDate", sdf.format(v.getNewsDate()));//messageDate	String	消息时间
				}else{
					resultObject.put("messageDate", null);//messageDate	String	消息时间
				}
				resultObject.put("fID", v.getFid());//fID	String	fID（当消息类型为1的时候返通知ID，2表示供需ID，3表示申请加入商会ID）
				
				switch(v.getType()){
				case 1://社会团体通知
					if("0".equals(v.getState())){ //messageState	int	消息状态 （如果是商会通知：1表示已参加，0表示没有参加）（如果是供需申请商会通知： 2供需认证正在被商会审核，3商会审核供需失败）(如果是加入商会通知:0正在审核，1审核成功，2审核失败)
						resultObject.put("messageState", 1);  
					}else{
						resultObject.put("messageState", 0);
					}
					YBasicSocialgroupsinformrecord cord=(YBasicSocialgroupsinformrecord)dao.findById(YBasicSocialgroupsinformrecord.class, v.getFid());
					cord.setFisHide("2");
					dao.saveOrUpdate(cord);
				
					StringBuffer messageContent1 = new StringBuffer("您于");
					messageContent1.append(cord.getUpdatetime()==null?(sdf.format(new Date())):(sdf.format(cord.getUpdatetime())));
					messageContent1.append("报名参加了");
					messageContent1.append(cord.getYBasicSocialgroupsinform()==null?"":cord.getYBasicSocialgroupsinform().getYBasicSocialgroups()==null?"":cord.getYBasicSocialgroupsinform().getYBasicSocialgroups().getFname());
					messageContent1.append("的");
					messageContent1.append(cord.getYBasicSocialgroupsinform()==null?"":cord.getYBasicSocialgroupsinform().getFheadline());
					messageContent1.append("。");
					resultObject.put("messageContent", messageContent1.toString());//您于【报名时间】（2014-12-15  14：05：29）  报名参加了【商会名称】（北京江西商会）的【活动主题】（元旦欢庆晚会）
					resultObject.put("businessId",v.getBusinessId());//businessId	String	商会ID(当消息类型为1,和3的时候返回)
					resultObject.put("noticeId",v.getNoticeId());//noticeId	String	通知ID（当消息类型为通知的时候返回）
					break;
				case 3://入会申请
					if("16".equals(v.getState())){ //messageState	int	消息状态 （如果是商会通知：1表示已参加，0表示没有参加）（如果是供需申请商会通知： 2供需认证正在被商会审核，3商会审核供需失败）(如果是加入商会通知:0正在审核，1审核成功，2审核失败)
						resultObject.put("messageState", 1);  
					}else if("17".equals(v.getState())){
						resultObject.put("messageState", 2);
					}else{
						resultObject.put("messageState", 0);
					}
					YInitiationApply apply = new YInitiationApply();
					apply = (YInitiationApply)dao.findById(YInitiationApply.class,v.getFid());
					apply.setFisHide("2");
					dao.saveOrUpdate(apply);
					YBasicSocialgroups group=new YBasicSocialgroups();
					group=(YBasicSocialgroups)dao.findById(YBasicSocialgroups.class,apply.getFgroupsId());
					StringBuffer messageContent3 = new StringBuffer("您于");
					messageContent3.append(apply.getFapplyDate()==null?sdf.format(new Date()):sdf.format(apply.getFapplyDate()));
					messageContent3.append("申请加入");
					messageContent3.append(group.getFname());
					messageContent3.append("。");
					resultObject.put("messageContent", messageContent3.toString());//messageContent	String	消息内容//您于【申请时间】申请加入【商会名称】。时间格式为：年、月、日、时、分、秒。具体格式如：（2014-06-16  06：16）
					resultObject.put("businessId",v.getBusinessId());//businessId	String	商会ID(当消息类型为1,和3的时候返回)
					break;
				case 2://供应信息
					resultObject.put("messageState", 3);//messageState	int	消息状态 （如果是商会通知：1表示已参加，0表示没有参加）（如果是供需申请商会通知： 2供需认证正在被商会审核，3商会审核供需失败）(如果是加入商会通知:0正在审核，1审核成功，2审核失败)
					List<Map<String , Object>> typeMap=new ArrayList<Map<String,Object>>();
					List<YSupplyGroup> supplygroupList=new ArrayList<YSupplyGroup>();
					YSupplyGroup supplygroup=new YSupplyGroup();
					supplygroup=(YSupplyGroup)dao.findById(YSupplyGroup.class, v.getFid());
					supplygroupList=(List<YSupplyGroup>)dao.findAllByHQL("from YSupplyGroup y where y.YBasicSocialgroupssupply.fid='"+supplygroup.getYBasicSocialgroupssupply().getFid()+"'");
					for(YSupplyGroup s:supplygroupList){
						Map<String , Object> groupMap=new HashMap<String, Object>();
						groupMap.put("businessId", s.getGroupid()); //businessId	String	商会ID
						groupMap.put("businessName", s.getYBasicSocialgroupssupply().getYBasicSocialgroups().getFname());
						if("16".equals(s.getIspass())){
							groupMap.put("warrantState",0 );
						}else if("17".equals(s.getIspass())){
							groupMap.put("warrantState", 3);
						}else{
							groupMap.put("warrantState",2 );
						}
						List<Map<String,Object>> typelist=new ArrayList<Map<String,Object>>();
						Map<String,Object> supplytypeMap=new HashMap<String, Object>();
						supplytypeMap.put("warrantTypeVal", s.getYBasicAssurancecontent().getFid());
						supplytypeMap.put("warrantTypeName", s.getYBasicAssurancecontent().getFcontent());
						typelist.add(supplytypeMap);
						groupMap.put("warrantType", typelist);
						typeMap.add(groupMap);
					}
					    
					supplygroup.setFisHide("2");
					dao.saveOrUpdate(supplygroup);
					StringBuffer messageContent2 = new StringBuffer("您发布的供应“");
					messageContent2.append(supplygroup.getYBasicSocialgroupssupply()==null?"":supplygroup.getYBasicSocialgroupssupply().getFheadline());
					messageContent2.append("”未通过");
					messageContent2.append(supplygroup.getYBasicAssurancecontent()==null?"":supplygroup.getYBasicAssurancecontent().getYBasicSocialgroups()==null?"":supplygroup.getYBasicAssurancecontent().getYBasicSocialgroups().getFname());
					messageContent2.append("的审核");
					messageContent2.append("。");
					resultObject.put("messageContent", messageContent2.toString());
					//resultObject.put("messageContent", "您发布的供应“"+supplygroup.getYBasicSocialgroupssupply()==null?"":supplygroup.getYBasicSocialgroupssupply().getFheadline()+"”未通过"+supplygroup.getYBasicAssurancecontent()==null?"":supplygroup.getYBasicAssurancecontent().getYBasicSocialgroups()==null?"":supplygroup.getYBasicAssurancecontent().getYBasicSocialgroups().getFname()+"的审核"+"。");//messageContent	String	消息内容//您发布的供应【类别】“降机移动式液压登车桥”【供应/需求】未通过北京江西商会的审核【类别】。

					resultObject.put("businessList",typeMap);        // warrantState	Int	担保状态(0发布成功状态, 2供需认证正在被商会审核，3商会审核供需失败)
					break;
				}
				resultList.add(resultObject);				
			}
//			for(Informationview v:views){
//				switch (v.getType()) {
//				case 1:
//					YBasicSocialgroupsinformrecord cord=(YBasicSocialgroupsinformrecord)dao.findById(YBasicSocialgroupsinformrecord.class, v.getFid());
//					cord.setFisHide("2");
//					dao.update(cord);
//					continue;
//				case 2:
//					YSupplyGroup guarantee = new YSupplyGroup();
//					guarantee = (YSupplyGroup)dao.findById(YSupplyGroup.class,v.getFid());
//					guarantee.setFisHide("2");
//					dao.update(guarantee);
//					continue;
//				case 3:
//					YInitiationApply apply = new YInitiationApply();
//					apply = (YInitiationApply)dao.findById(YInitiationApply.class,v.getFid());
//					apply.setFisHide("2");
//					dao.update(apply);
//					continue;
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询通知")
	public Informationview getById(String fid) {
		Informationview view = new Informationview();
		view=(Informationview)dao.findById(Informationview.class, fid);
		return view;
	}

	@Override
	@SystemServiceLog(description = "根据用户id查询未读消息通知")
	public List<Informationview> getListByUserId(List<YBasicMember> memberlist) {
		List<Informationview> views = new ArrayList<Informationview>();
		try {
			StringBuffer hqlbuffer = new StringBuffer("from Informationview as v where ");
			hqlbuffer.append("  v.status='0'  and (");
			for(int i=0;i<memberlist.size();i++){
				if(i!=0){
					hqlbuffer.append(" or ");
				}
				hqlbuffer.append("v.memberId='"+memberlist.get(i).getFid()+"'");
			}
			
			hqlbuffer.append(" )");
			views=(List<Informationview>)dao.findAllByHQL(hqlbuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return views;
	}
	
	@Override
	@SystemServiceLog(description = "根据用户电话查询未读消息通知")
	@SuppressWarnings("unchecked")
	public int getListByUserTel(String tel) {
		List<Informationview> views = new ArrayList<Informationview>();
		try {
			List<YBasicMember> memberlist = (List<YBasicMember>) dao.find("from YBasicMember as y where y.fmobilePhone='"+tel+"'");
			if(memberlist!=null && memberlist.size()>0){
				StringBuffer hqlbuffer = new StringBuffer("from Informationview as v where ");
				hqlbuffer.append("  v.status='0'  and (");
				for(int i=0;i<memberlist.size();i++){
					if(i!=0){
						hqlbuffer.append(" or ");
					}
					hqlbuffer.append("v.memberId='"+memberlist.get(i).getFid()+"'");
				}
				
				hqlbuffer.append(" )");
				views=(List<Informationview>)dao.findAllByHQL(hqlbuffer.toString());
				if(views!=null && views.size()>0){
					return views.size();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Informationview> findInformationviewPhone(String tel){
		List<Informationview> views = new ArrayList<Informationview>();
		try {
			//通过电话获取用户
			List<YBasicMember> memberlist = (List<YBasicMember>) dao.find("from YBasicMember as y where y.fmobilePhone='"+tel+"'");
			YBasicMember member = memberlist.get(0);
			if(memberlist != null && memberlist.size() > 0){
				StringBuffer hqlbuffer = new StringBuffer("from Informationview as v where v.memberId = '"+member.getFid()+"'");
				views = (List<Informationview>) dao.findAllByHQL(hqlbuffer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return views;
	}
	
	@Override
	public void update(Informationview informationview){
		dao.update(informationview);
	}
	
}
