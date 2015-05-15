package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hpbf.extractor.PublisherTextExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinform;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsinformrecord;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessagerecord;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;
import com.yunzo.cocmore.utils.base.FilterHtmlUtil;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;
/**
 * 社会团体通知业务实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsInformService")
public class GroupsInformServiceImpl implements GroupsInformService {
	private static final Logger logger = Logger.getLogger(GroupsInformServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	@Resource
	private DynmicandInfoPushService dynmicandInfoPushService;
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	@SystemServiceLog(description = "添加团体通知")
	public void addInform(YBasicSocialgroupsinform inform, String[] memberIds) throws Exception {
			cOC_HibernateDAO.save(inform);
			//得到所有的要通知推送的用户id
			YBasicSocialgroupsinformrecord basicSocialgroupsinformrecord = null;
			YBasicMember member = null;
			Set<String> set1 = new HashSet<String>(Arrays.asList(memberIds));
			if(set1!=null&&set1.size()>0){
				for(String merberId : set1){
					basicSocialgroupsinformrecord = new YBasicSocialgroupsinformrecord();
					member = (YBasicMember) cOC_HibernateDAO.findById(YBasicMember.class,merberId);
					System.out.println("##################"+member+"##################");
					basicSocialgroupsinformrecord.setFinformPeopleId(merberId);//通知人id
					basicSocialgroupsinformrecord.setFinformPeopleName(member.getFname());//通知人名称
					System.out.println("############"+inform.getFid()+"#####################");
					basicSocialgroupsinformrecord.setYBasicSocialgroupsinform(inform);//对应的通知记录id
					basicSocialgroupsinformrecord.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));
					cOC_HibernateDAO.save(basicSocialgroupsinformrecord);//保存发送人列表
				}
				inform.setFinformPeopleNum(memberIds.length);
				cOC_HibernateDAO.saveOrUpdate(inform);

			}else{
				//如果不选择就推送给所有的所属团体会员
				YBasicSocialgroups groups = (YBasicSocialgroups) cOC_HibernateDAO.findById(YBasicSocialgroups.class,inform.getYBasicSocialgroups().getFid());
				Set<YBasicMember> set = groups.getYBasicMembers();
				if(set!=null&&set.size()>0){
					for(YBasicMember member1:set){
						basicSocialgroupsinformrecord = new YBasicSocialgroupsinformrecord();
						basicSocialgroupsinformrecord.setFinformPeopleId(member1.getFid());//通知人id
						basicSocialgroupsinformrecord.setFinformPeopleName(member1.getFname());//通知人名称
						basicSocialgroupsinformrecord.setYBasicSocialgroupsinform(inform);//对应的通知记录id
						basicSocialgroupsinformrecord.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));
						cOC_HibernateDAO.save(basicSocialgroupsinformrecord);//保存发送人列表
					}
					//通知人数
					//					
					inform.setFinformPeopleNum(set.size());
					inform.setFdetailAddress("webApp/informDetail.html?id="+inform.getFid());
					cOC_HibernateDAO.saveOrUpdate(inform);
				}
			}
	}


	@Override
	@SystemServiceLog(description = "删除团体通知")
	public void deleteInform(YBasicSocialgroupsinform inform) {
		cOC_HibernateDAO.delete(inform);

	}

	@Override
	@SystemServiceLog(description = "审核、反审核团体通知")
	public Boolean auditInform(YBasicSocialgroupsinform inform) {
		boolean flag = true;
		if(inform.getFbillState()==Status.UNAUDIT.value()||inform.getFbillState()==Status.AUDIT.value()||inform.getFbillState()==Status.SUBMIT.value()){//如果是审核、反审核、提交状态就进行下列操作
			flag = false;
			YBasicSocialgroupsinform inform1 = (YBasicSocialgroupsinform) cOC_HibernateDAO.get(YBasicSocialgroupsinform.class, inform.getFid());
			if(null != inform1){
				inform1.setFbillState(inform.getFbillState());//更改需求信息的审核状态 审核或者反审核
				cOC_HibernateDAO.update(inform1);//更新到持久层
				flag = true;
			}else{
				logger.info("数据不存在!");
			}
		}
		return flag;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	@SystemServiceLog(description = "生效、失效团体通知")
	public void effectInform(YBasicSocialgroupsinform inform) throws NumberFormatException, Exception {
		try {
			if(inform.getFbillState()==Status.UNEFFECT.value()||inform.getFbillState()==Status.EFFECT.value()){//如果是生效或者失效的才进行下面的操作
				YBasicSocialgroupsinform inform1 = (YBasicSocialgroupsinform) cOC_HibernateDAO.get(YBasicSocialgroupsinform.class, inform.getFid());
				if(null != inform1){
					inform1.setFbillState(inform.getFbillState());//更改需求信息的生效状态  生效  失效
					cOC_HibernateDAO.update(inform1);//更新到持久层
					/**
					 * 如果点击生效进行推送
					 */
					if(inform.getFbillState()==Status.EFFECT.value()){
						List<YBasicSocialgroupsinformrecord> informRecordList = (List<YBasicSocialgroupsinformrecord>) cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroupsinformrecord record where record.YBasicSocialgroupsinform.fid='"+inform.getFid()+"'");
						//将用户id拼接成sql语句的in ('XX','XX1')的格式
						StringBuffer condition = null;
						Set<String> tels = new HashSet<String> ();
						List<String> Alltels = null;
						if(informRecordList!=null&&informRecordList.size()>0){
							for(YBasicSocialgroupsinformrecord record:informRecordList){
								Alltels = (List<String>) cOC_HibernateDAO.findAllByHQL("select  ymember.fmobilePhone from  YBasicMember ymember where ymember.fid ='"+record.getFinformPeopleId()+"'");
								if(null!=Alltels&&Alltels.size()>0){
									//正式环境
									tels.add(Alltels.get(0));
								}
							}
							//正式环境
//							Set<String> tels = new HashSet<String>(telsList);
							//测试环境
//							tels.add("59444947424947494145444243");
//							tels.add("59484946444141494a42464243");
//							tels.add("5944414642424a434944494243");
							//查询所有用户的deviceID,使用map来装key是clinetID,value是用户的加密电话号码的集合
							//这样做的前提是，几个用户最后都是用的一个设备登录，所以他们的渠道号都是一样的,渠道号一致赋值,最后以最后一个作为渠道号渠道号
							if(tels!=null&&tels.size()>0){
								Map<String,PushVo> deviceIdMap  =  getOutRepeat(tels);
								/**
								 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
								 */
								PushThread pushThread = new PushThread(new Integer(5), deviceIdMap, inform1.getFheadline(), inform1.getFid(), inform1.getYBasicSocialgroups().getFid(), dynmicandInfoPushService);
								pushThread.start();
							
							}else{
								logger.info("没有登录过的设备信息....");
							}
						}else{
							logger.info("没有参与通知的人....");
						}
					}
				}else{
					logger.info("数据不存在!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	@SystemServiceLog(description = "修改团体通知")
	public void updateInform(YBasicSocialgroupsinform inform, String[] memberIds,HttpServletRequest request) {
		String urlPath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort()+""
				+ request.getContextPath() + "/";
		//得到所有的要通知推送的用户id
		YBasicSocialgroupsinformrecord basicSocialgroupsinformrecord = null;
		YBasicMember member = null;
		//在重新加入
		Set<String> set1 = null;
		if(null!=memberIds){
			set1 = new HashSet<String>(Arrays.asList(memberIds));
		}
		
		if(set1!=null&&set1.size()>0){
			for(String merberId : set1){
				basicSocialgroupsinformrecord = new YBasicSocialgroupsinformrecord();
				member = (YBasicMember) cOC_HibernateDAO.findById(YBasicMember.class,merberId);
				basicSocialgroupsinformrecord.setFinformPeopleId(merberId);//通知人id
				basicSocialgroupsinformrecord.setFinformPeopleName(member.getFname());//通知人名称
				basicSocialgroupsinformrecord.setYBasicSocialgroupsinform(inform);//对应的通知记录id
				basicSocialgroupsinformrecord.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));
				cOC_HibernateDAO.save(basicSocialgroupsinformrecord);//保存发送人列表
			}
			inform.setFinformPeopleNum(memberIds.length);
			inform.setFdetailAddress("webApp/informDetail.html?id="+inform.getFid());
			cOC_HibernateDAO.update(inform);

		}else{
			//如果不选择就推送给所有的所属团体会员
			YBasicSocialgroups groups = (YBasicSocialgroups) cOC_HibernateDAO.findById(YBasicSocialgroups.class,inform.getYBasicSocialgroups().getFid());
			Set<YBasicMember> set = groups.getYBasicMembers();
			if(set!=null&&set.size()>0){
				for(YBasicMember member1:set){
					basicSocialgroupsinformrecord = new YBasicSocialgroupsinformrecord();
					basicSocialgroupsinformrecord.setFinformPeopleId(member1.getFid());//通知人id
					basicSocialgroupsinformrecord.setFinformPeopleName(member1.getFname());//通知人名称
					basicSocialgroupsinformrecord.setYBasicSocialgroupsinform(inform);//对应的通知记录id
					basicSocialgroupsinformrecord.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));
					cOC_HibernateDAO.save(basicSocialgroupsinformrecord);//保存发送人列表
				}
				//通知人数
				inform.setFinformPeopleNum(set.size());
				inform.setFdetailAddress(urlPath+"webApp/informDetail.html?id="+inform.getFid());
				cOC_HibernateDAO.saveOrUpdate(inform);
			}else{
				inform.setFinformPeopleNum(0);
				inform.setFdetailAddress("webApp/informDetail.html?id="+inform.getFid());
				cOC_HibernateDAO.saveOrUpdate(inform);
			}
		}
	}
	@Override
	@SystemServiceLog(description = "删除团体通知")
	public void deleteInformRecord(YBasicSocialgroupsinform inform){
		//首先删除所有的原推送记录表信息
		List<YBasicSocialgroupsinformrecord> recordList = (List<YBasicSocialgroupsinformrecord>) cOC_HibernateDAO.findAllByHQL("from YBasicSocialgroupsinformrecord y where y.YBasicSocialgroupsinform.fid='"+inform.getFid()+"'");
		if(recordList !=null){
			cOC_HibernateDAO.deleteAll(recordList);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部、或根据条件查询团体通知")
	public PagingList<YBasicSocialgroupsinform> getAllInformPagingList(Integer page, Integer pageSize,String groupId,String fheadline) {
		PagingList<YBasicSocialgroupsinform> pagingList = new PagingList<YBasicSocialgroupsinform>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsinform inform"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsinform inform"); 
		/**
		 *团体id
		 */
		if(null!=groupId&&!"".equals(groupId)){
			hqlList.append(" where inform.YBasicSocialgroups.fid=?");
			hqlCount.append(" where inform.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		/**
		 * 如果传来模糊查询name
		 */
		if(null!=fheadline&&!"".equals(fheadline)){
			if(values.size()>0){
				hqlList.append(" and ");
				hqlCount.append(" and ");
			}else{
				hqlList.append(" where ");
				hqlCount.append(" where ");
			}
			hqlList.append("inform.fheadline like ?");
			hqlCount.append("inform.fheadline like ?");
			values.add("%"+fheadline+"%");
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicSocialgroupsinform>) cOC_HibernateDAO.find(hqlList.append(" order by inform.flag desc").toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}


	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "通过id获取详情")
	public Map<String,Object> getInformById(String fid,String tel,String businessId) {
		Map<String,Object> resultObject = new HashMap<String,Object>();
		List<YComment> ycommentList = null; 
		List<YPointlike> ypointlikeList = null;
		List<YPointlike> mineypointlikeList = null;
		YBasicSocialgroupsinform inform = (YBasicSocialgroupsinform)cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class, fid);
		if(inform!=null){ //如果这条数据存在 获取他的类型   通过它的类型和fid来查询它是否点赞 点赞数  评论数
			String hql =null;
			hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='3002'";
			ypointlikeList =  (List<YPointlike>)cOC_HibernateDAO.findAllByHQL(hql);

			if(ypointlikeList!=null&&ypointlikeList.size()>0){ //只要根据  类型和动态的id查找到数据 在你筛选出点赞总数 0赞，1取消点赞（不赞同），2不赞同
				int pointlikecount = 0;//点赞数
				int nopointlikecount = 0;//不支持数
				for(YPointlike ypointlike:ypointlikeList){
					if(ypointlike.getFpointLikeType()==0){
						pointlikecount =pointlikecount+1;
					}else if(ypointlike.getFpointLikeType()==2){
						nopointlikecount = nopointlikecount+1;
					}
				}
				resultObject.put("supportNum",pointlikecount);//点赞总数
				resultObject.put("notSupport",nopointlikecount);//点赞总数
			}else{
				resultObject.put("supportNum", 0);
				resultObject.put("notSupport",0);//点赞总数
			}
		if(null!=tel&&!"".equals(tel)&&null!=businessId&&!"".equals(businessId)){
			List<YBasicMember> memberList = (List<YBasicMember>)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'");
			if(null!=memberList&&memberList.size()>0){
				YBasicMember ymember =memberList.get(0);
				hql= "from YBasicSocialgroupsinformrecord informrecord where informrecord.finformPeopleId='"+ymember.getFid()+"' and informrecord.YBasicSocialgroupsinform.fid='"+fid+"'";
				List<YBasicSocialgroupsinformrecord>  informrecordList=  (List<YBasicSocialgroupsinformrecord>)cOC_HibernateDAO.findAllByHQL(hql);
	
				if(informrecordList!=null&&informrecordList.size()>0){
					resultObject.put("isJoin", new Integer(informrecordList.get(0).getFynparticipation()));
				}else{
					resultObject.put("isJoin", 1);
				}
				hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='3002' and ypointlike.YBasicMember.fid='"+ymember.getFid()+"'";
				mineypointlikeList =  (List<YPointlike>)cOC_HibernateDAO.findAllByHQL(hql);
				if(mineypointlikeList!=null&&mineypointlikeList.size()>0){//根据当前用户来查询
					int type = mineypointlikeList.get(0).getFpointLikeType();//表态类型
					resultObject.put("isSupport", type==0?0:1);//当前用户已点赞
					resultObject.put("isNotSupport", type==2?0:1);//当前用户已不支持
				}else{
					resultObject.put("isSupport", 1);//未点赞
					resultObject.put("isNotSupport",1);//未点不赞同
				}
			}else{
				resultObject.put("isSupport", 1);//当前用户未点赞
				resultObject.put("isNotSupport",1);//未点不赞同
				resultObject.put("isJoin", 1);//未参加
			}
		}else{
			resultObject.put("isSupport", 1);//当前用户未点赞
			resultObject.put("isNotSupport",1);//未点不赞同
			resultObject.put("isJoin", 1);//未参加
		}
			hql = "from YComment ycomment where ycomment.fforeignId='"+fid+"' and ycomment.YBasicType.fid='3002'";
			ycommentList =  (List<YComment>)cOC_HibernateDAO.findAllByHQL(hql);
			if(ycommentList!=null&&ycommentList.size()>0){
				resultObject.put("reviewNum", ycommentList.size());//获取评论总数
			}else{
				resultObject.put("reviewNum", 0);//如果不存在，评论数就是0
			}
		}else{//如果动态不存在
			//暂时不做处理
		}
		return resultObject;
	}

	/**
	 * 获取接口通知列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取通知列表")
	public List<Map<String, Object>> getInformList(String fid, int pageSize,String businessId,String tel,HttpServletRequest request) {
		Map<String,Object> resultObject =null;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		int ycomment= 0;
		List<YBasicSocialgroupsinform> informList = null;
		String hql = null;
		if(fid==null||fid.equals("")){
			//档通知id不存在时查找最新的pageSize条
			//首先判断是否是游客，根据电话号码是否是空来判断
			if(tel!=null){
				if(businessId!=null&&!"".equals(businessId)){
					hql ="from YBasicSocialgroupsinform inform where inform.YBasicSocialgroups.fid='"+businessId+"' and inform.fbillState=5 order by inform.flag desc";
				}else{
					hql ="from YBasicSocialgroupsinform inform where  inform.fbillState=5 order by inform.flag desc";
				}
			}else{
				if(businessId!=null&&!"".equals(businessId)){
					hql="from YBasicSocialgroupsinform inform where inform.YBasicSocialgroups.fid='"+businessId+"' and inform.fbillState=5 order by inform.flag desc";
				}else{
					hql="from YBasicSocialgroupsinform inform where  inform.fbillState=5 order by inform.flag desc";
				}
			}
			informList =  (List<YBasicSocialgroupsinform>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}else{
			//根据当前的inform的flag前几条
			YBasicSocialgroupsinform inform = (YBasicSocialgroupsinform) cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class, fid);
			if(tel!=null){
				if(businessId!=null&&!"".equals(businessId)){
					hql="from YBasicSocialgroupsinform inform where inform.YBasicSocialgroups.fid='"+businessId+"' and inform.flag< "+inform.getFlag()+" and inform.fbillState=5 order by flag desc";
				}else{
					hql="from YBasicSocialgroupsinform inform where  inform.flag< "+inform.getFlag()+" and inform.fbillState=5 order by flag desc";
				}
			}else{
				if(businessId!=null&&!"".equals(businessId)){
					hql="from YBasicSocialgroupsinform inform where inform.YBasicSocialgroups.fid='"+businessId+"' and inform.fbillState=5 order by inform.flag desc";
				}else{
					hql="from YBasicSocialgroupsinform inform where  inform.fbillState=5 order by inform.flag desc";
				}
			}
		
			
			informList = (List<YBasicSocialgroupsinform>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}
		if(informList!=null&&informList.size()>0){
			for(YBasicSocialgroupsinform inform :informList){//只循环前pagesize条
				resultObject = new HashMap<String,Object>();
				String logo=inform.getFlogoImage();
				String url=null;
				if(null!=logo){
					url = logo.replace(",", "");
				}
				resultObject.put("noticeImageurl", url);//通知图片地址
				resultObject.put("noticeId", inform.getFid());//通知Id
				resultObject.put("noticeTitle", inform.getFheadline());//通知标题
				resultObject.put("noticeDescription", FilterHtmlUtil.filterHtml(inform.getFmessage()));//通知内容
				Date date=inform.getFstartTime();
				String startTime=null;
				if(null!=date){
					startTime= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(date);
				}
				resultObject.put("noticeDate",startTime);//通知时间
				resultObject.put("noticeDetailUrl", "webApp/informDetail.html?id="+inform.getFid());//通知详情地址
				hql = "select count(0) from YComment ycomment where ycomment.fforeignId='"+inform.getFid()+"' and ycomment.YBasicType.fid='3002'";
				ycomment =  cOC_HibernateDAO.getTotalCountByCondition(hql, null, null, null).intValue();//获取所有评论数
				resultObject.put("reviewNum",ycomment);//评论数
				if(tel!=null){
					boolean flag = true;
					try {
						 flag = dynmicandInfoPushService.findRead(inform.getFid(),tel);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(flag ==true){
						resultObject.put("isRead", 0);
					}else{
						resultObject.put("isRead", 1);
					}
				}else{
					resultObject.put("isRead",0);
				}
				resultList.add(resultObject);
			}
		}
		return resultList;
	}
	/**
	 * 点赞  点不赞同  点取消如果不存在就新增，如果存在就更改状态
	 * @param fid  informId 
	 * @param userId 当前用户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "团体通知点赞")
	public boolean pointLike(String fid,String tel,String businessId ,int type) {
		boolean flag = true;
		List<YPointlike> mineypointlikeList = null;
		YBasicMember ymember =(YBasicMember)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'").get(0);
		String hql = "";
		try{
			YBasicSocialgroupsinform inform = (YBasicSocialgroupsinform)cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class, fid);

			hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='3002' and ypointlike.YBasicMember.fid='"+ymember.getFid()+"'";
			mineypointlikeList =  (List<YPointlike>)cOC_HibernateDAO.findAllByHQL(hql);
			YPointlike ypointlike = null;
			if(mineypointlikeList!=null&&mineypointlikeList.size()>0){
				//如果此用户、此模块、此通知id在点赞表中存在     就修改表态状态
				ypointlike = mineypointlikeList.get(0);
				ypointlike.setFpointLikeType(type);//更改表态为点赞或者取消
				ypointlike.setFpointLikeTime(new java.sql.Date(new java.util.Date().getTime()));
				cOC_HibernateDAO.update(ypointlike);//更新数据库
			}else{
				//如果此条记录不存在就新增
				ypointlike = new YPointlike(); 
				ypointlike.setYBasicMember(ymember);//操作用户
				ypointlike.setYBasicType(new YBasicType("3002"));//模块类型
				ypointlike.setFpointLikeType(type);//点赞或者取消
				ypointlike.setFpointLikeTime(new java.sql.Date(new java.util.Date().getTime()));
				ypointlike.setFmanShowId(fid);
				cOC_HibernateDAO.save(ypointlike);
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 是否参加商会通知   没有就新增一条数据 有就更改状态
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "是否参加商会通知")
	public boolean saveInformJoin(String fid, String tel, String businessId,String isJoin) {
		boolean flag =  true;
		List<YBasicSocialgroupsinformrecord> recordList = null; 
		YBasicMember ymember =(YBasicMember)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'").get(0);
		String hql = "";
		try{
			YBasicSocialgroupsinform inform = (YBasicSocialgroupsinform) cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class, fid);
			inform.setFparticipationNum(new Integer(inform.getFparticipationNum()==null?0:inform.getFparticipationNum())+1);//增加通知信息中的参加人数
			cOC_HibernateDAO.update(inform);
			hql= "from YBasicSocialgroupsinformrecord record where record.YBasicSocialgroupsinform.fid='"+fid+"' and record.finformPeopleId='"+ymember.getFid()+"'";
			recordList =  (List<YBasicSocialgroupsinformrecord>)cOC_HibernateDAO.findAllByHQL(hql);
			YBasicSocialgroupsinformrecord record = null;
			if(recordList!=null&&recordList.size()>0){
				//如果存在
				record = recordList.get(0);
				record.setFynparticipation(isJoin);
					record.setFisHide("0");
				cOC_HibernateDAO.update(record);
			}else{
				//不存在就新增
				record = new YBasicSocialgroupsinformrecord();
				record.setFynparticipation(isJoin);
				record.setFinformPeopleId(ymember.getFid());
				record.setYBasicSocialgroupsinform(new YBasicSocialgroupsinform(fid));
				cOC_HibernateDAO.save(record);
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}


	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取回复列表并分页")
	public List<Map<String, Object>> getInformReplyList(String noticeId,String reviewId, int pageSize) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultObject = null;
		String hql = "";
		List<YComment> ycommentList=null;
		//首先获取需要查询的评论pagesize信息
		if(reviewId==null||reviewId.equals("")){
			//如果评论id为空就查询动态最新的pagesize条评论的回复
			hql ="from YComment ycomment where ycomment.YBasicType.fid='3002' and  ycomment.fforeignId='"+noticeId+"' order by ycomment.flag desc";
			ycommentList = (List<YComment>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}else{
			//如果评论id不为空，那么就查询id前通过flag排序的pageSize条评论的回复
			//根据当前的来分页
			YComment ycomment = (YComment) cOC_HibernateDAO.findById(YComment.class, reviewId);
			hql="from YComment ycomment where ycomment.YBasicType.fid='3002' and  ycomment.fforeignId='"+noticeId+"' and ycomment.flag< "+ycomment.getFlag()+"  order by ycomment.flag desc";
			ycommentList = (List<YComment>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}
		//获取所有的评论的所有的回复
		for(YComment ycommet:ycommentList){
			YBasicMember basicMember = ycommet.getYBasicMember();
			resultObject = new HashMap<String,Object>();
			resultObject.put("reviewId", ycommet.getFid());//评论id
			String headImage=basicMember.getFheadImage();
			String url=null;
			if(null!=headImage){
				url = headImage.replace(",", "");
			}
			resultObject.put("userImageUrl", url);//回复人头像
			resultObject.put("userNickeName", basicMember.getFname());//回复人昵称
			Date date=ycommet.getFtime();
			String time=null;
			if(date!=null){
				time=new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(date);
			}
			resultObject.put("reviewDate", time);//回复时间
			resultObject.put("reviewContent", ycommet.getFcontents());//回复内容
			resultObject.put("mid", basicMember.getFmobilePhone());//用户id
			resultList.add(resultObject);
		}
		return resultList;
	}


	@Override
	@SystemServiceLog(description = "通过评论id删除通知评论")
	public boolean delInformReview(String reviewId) {
		boolean flag =true;
		try{
			cOC_HibernateDAO.deleteByKey(reviewId, YComment.class);
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}


	@Override
	@SystemServiceLog(description = "添加通知评论")
	public void addInformReview(String tel,String businessId, String noticeId,
			String reviewContent) {
		YBasicMember ymember =(YBasicMember)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'").get(0);
		YComment comment = new YComment();
		comment.setFcontents(reviewContent);//评论类容
		comment.setFforeignId(noticeId);//设置关联外键
		comment.setFtime(new java.sql.Date(new java.util.Date().getTime()));
		comment.setYBasicMember(ymember);
		comment.setYBasicType(new YBasicType("3002"));//设置模块类型
		cOC_HibernateDAO.save(comment);
	}


	@Override
	@SystemServiceLog(description = "查看所有与回复数")
	public List<YBasicSocialgroupsinformrecord> getGroupsinformRecords(
			YBasicSocialgroupsinform inform) {
		inform =	(YBasicSocialgroupsinform) cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class,inform.getFid());
		if(inform!=null){
			return new ArrayList<YBasicSocialgroupsinformrecord>(inform.getYBasicSocialgroupsinformrecords());

		}else{
			return null;
		}
	}


	@Override
	@SystemServiceLog(description = "根据id查询通知")
	public YBasicSocialgroupsinform findInform(String fid) {
		return (YBasicSocialgroupsinform) cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class, fid);
	}


	@Override
	@SystemServiceLog(description = "修改隐藏字段为隐藏")
	public void updateBycordId(String fid,String type) {
		try {
			YBasicSocialgroupsinformrecord cord=(YBasicSocialgroupsinformrecord)cOC_HibernateDAO.findById(YBasicSocialgroupsinformrecord.class, fid);
			cord.setFisHide(type);
			cOC_HibernateDAO.update(cord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	@SystemServiceLog(description = "根据id查询通知")
	public YBasicSocialgroupsinform getById(String fid) {
		YBasicSocialgroupsinform inform=new YBasicSocialgroupsinform();
		inform=(YBasicSocialgroupsinform)cOC_HibernateDAO.findById(YBasicSocialgroupsinform.class, fid);
		return inform;
	}


	@Override
	@SystemServiceLog(description = "修改团体通知")
	public void update(YBasicSocialgroupsinform inform) {
		cOC_HibernateDAO.update(inform);
	}


	@Override
	public ArrayList<Integer> findPointLikeAndNotLike(String informId) {
		ArrayList<Integer> counts = new ArrayList<Integer>();
		String hql = "select count(0) from YPointlike ypointlike where ypointlike.fmanShowId='"+informId+"' and ypointlike.YBasicType.fid='3002' and ypointlike.fpointLikeType=0";
		 Integer pointLikecount = cOC_HibernateDAO.getTotalCountByCondition(hql, null, null, null);
		 hql = "select count(0) from YPointlike ypointlike where ypointlike.fmanShowId='"+informId+"' and ypointlike.YBasicType.fid='3002' and ypointlike.fpointLikeType=2";
		 Integer notLikecount = cOC_HibernateDAO.getTotalCountByCondition(hql, null, null, null);
		 if(pointLikecount==null){
			 pointLikecount=0;
		 }
		 if(notLikecount==null){
			 notLikecount=0;
		 }
		 counts.add(pointLikecount);//点赞数
		 counts.add(notLikecount);//不赞同数
		return counts;
	}


	@SuppressWarnings("unchecked")
	@Override
	public PagingList<YBasicSocialgroupsinform> getAllInformPagingList(Integer start, Integer limit, Integer type) {
		PagingList<YBasicSocialgroupsinform> pagingList = new PagingList<YBasicSocialgroupsinform>();
		List<Object> values = new ArrayList<Object>();
		/**
		 * type ：0 点赞数  1排序数
		 */
		if(type==0){
			String sql = "select  inform.fid as fid,inform.fnumber,inform.fheadline,inform.flogoImage,inform.fmessage,inform.finformPeopleNum,inform.fparticipationNum,inform.fstartTime,inform.ffinishTime,inform.fbillState,inform.fcomment,inform.flag,inform.fdetailAddress from  y_basic_socialgroupsinform inform  left join (select pointlike.FManShowID,count(pointlike.FManShowID) as totalcount from  y_pointlike pointlike group by FManShowID) tmp on inform.fid=tmp.FManShowID ORDER BY  totalcount desc limit "+start+","+limit+"";
			pagingList.setList((List<YBasicSocialgroupsinform>) cOC_HibernateDAO.getListBySqlVO(sql, YBasicSocialgroupsinform.class));
		}else if(type==1){
			String sql = "select inform.fid as fid,inform.fnumber,inform.fheadline,inform.flogoImage,inform.fmessage,inform.finformPeopleNum,inform.fparticipationNum,inform.fstartTime,inform.ffinishTime,inform.fbillState,inform.fcomment,inform.flag,inform.fdetailAddress from  y_basic_socialgroupsinform inform  left join (select coment.FForeignID,count(coment.FForeignID) as totalcount from  y_comment coment group by FForeignID) tmp on inform.fid=tmp.FForeignID ORDER BY  totalcount desc limit "+start+","+limit+"";
			pagingList.setList((List<YBasicSocialgroupsinform>) cOC_HibernateDAO.getListBySqlVO(sql, YBasicSocialgroupsinform.class));
		}else{
			StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsinform inform"); 
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<YBasicSocialgroupsinform>) cOC_HibernateDAO.find(hqlList.toString(), start, limit, values.toArray()));
		}
		
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsinform inform"); 
		
		/**
		 * 获得总条数
		 */
		pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), start, limit,null));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
	/**
	 * 去重方法，返回需要推送的对象
	 * @param tels
	 * @return
	 */
		@SuppressWarnings("unchecked")
		@Override
		public Map<String,PushVo> getOutRepeat(Set<String> tels){
			Map<String,PushVo> deviceIdMap = new HashMap<String,PushVo>();//clientId去重
			List<YAppdevice> yappdeviceList =  null;
			for(String record:tels){
				String clientId = null;
				PushVo pushVo = null;
				String tel = null;
				String channelNo = null;
				List<String> telList  = null;
				if(record!=null){
					try {
						yappdeviceList = (List<YAppdevice>) cOC_HibernateDAO.findAllByHQL("from  YAppdevice  device where device.fuserName ='"+record+"'");
						if(yappdeviceList!=null&&yappdeviceList.size()>0){
							
							clientId = yappdeviceList.get(0).getFdeviceId();//设备id
							tel = yappdeviceList.get(0).getFuserName();//电话号码
							channelNo = yappdeviceList.get(0).getFappChannelNo();//渠道号
						
							if(clientId!=null&&!"".equals(clientId)&&tel!=null&!"".equals(tel)&&null!=channelNo&&!"".equals(channelNo)){
								//如果clientid的key已经存在就直接将电话号码加到value 的list里面去，不然就在map加入key
								if(deviceIdMap.containsKey(clientId)){
									pushVo = deviceIdMap.get(clientId);
									telList = pushVo.getTels();
								}else{
									pushVo = new PushVo();
									telList = new ArrayList<String> ();
								}
								telList.add(tel);
								pushVo.setTels(telList);
								pushVo.setChannelno(new Integer(channelNo));
								deviceIdMap.put(clientId,pushVo);
							}
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
			return deviceIdMap;
		}
}
