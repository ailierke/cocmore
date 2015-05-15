package com.yunzo.cocmore.core.function.service.impl;

import java.io.UnsupportedEncodingException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdynamic;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessagerecord;
import com.yunzo.cocmore.core.function.service.DynmicandInfoPushService;
import com.yunzo.cocmore.core.function.service.GroupsDynamicService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.LoginUserGroupPushThread;
import com.yunzo.cocmore.core.thread.PushThread;
import com.yunzo.cocmore.utils.base.FilterHtmlUtil;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;
/**
 * 社会团体动态业务实现类
 * @author jiangxing
 *
 */
@Transactional
@Service("groupsDynamicService")
public class GroupsDynamicServiceImpl implements GroupsDynamicService {
	private static final Logger logger = Logger.getLogger(GroupsDynamicServiceImpl.class);
	@Resource
	COC_HibernateDAO cOC_HibernateDAO;
	@Resource
	private DynmicandInfoPushService dynmicandInfoPushService;
	@Resource
	GroupsInformService groupsInformService;
	@Override
	@SystemServiceLog(description = "新增团体动态")
	public Boolean addDynamic(YBasicSocialgroupsdynamic dynamic) {
		boolean flag = true;
		try{
			cOC_HibernateDAO.save(dynamic);
			
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
	    return 	flag;
	}


	@Override
	@SystemServiceLog(description = "删除团体动态")
	public void deleteDynamic(YBasicSocialgroupsdynamic dynamic) {
		cOC_HibernateDAO.delete(dynamic);

	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "审核、反审核团体动态")
	public Boolean auditDynamic(YBasicSocialgroupsdynamic dynamic) throws NumberFormatException, Exception {
		boolean flag = true;
			flag = false;
			YBasicSocialgroupsdynamic dynamic1 = (YBasicSocialgroupsdynamic) cOC_HibernateDAO.get(YBasicSocialgroupsdynamic.class, dynamic.getFid());
			if(null != dynamic1){
				dynamic1.setFbillState(dynamic.getFbillState());//更改需求信息的审核状态 审核或者反审核
				cOC_HibernateDAO.update(dynamic1);//更新到持久层
				/**
				 * 如果点击生效进行推送
				 */
				try {
					
				
				if(dynamic.getFbillState()==Status.EFFECT.value()){
						List<String> telsList = (List<String>) cOC_HibernateDAO.findAllByHQL("select  ymember.fmobilePhone from  YBasicMember as ymember where ymember.YBasicSocialgroups.fid='"+dynamic1.getYBasicSocialgroups().getFid()+"'");
						Set<String> tels = new HashSet<String>(telsList);
						if(tels!=null&&tels.size()>0){
								
							Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(tels);
							/**
							 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
							 */
							PushThread pushThread = new PushThread(new Integer(4), deviceIdMap, dynamic.getFheadline(), dynamic.getFid(), dynamic.getYBasicSocialgroups().getFid(), dynmicandInfoPushService);
							pushThread.start();
						}else{
							logger.info("没有登录过的设备信息....");
						}
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
				flag = true;
			}else{
				logger.info("数据不存在!");
			}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "生效、失效团体动态")
	public Boolean effectDynamic(YBasicSocialgroupsdynamic dynamic) throws NumberFormatException, Exception {
		boolean flag = false;
			YBasicSocialgroupsdynamic dynamic1 = (YBasicSocialgroupsdynamic) cOC_HibernateDAO.get(YBasicSocialgroupsdynamic.class, dynamic.getFid());
			if(null != dynamic1){
				dynamic1.setFbillState(dynamic.getFbillState());//更改需求信息的生效状态  生效  失效
				cOC_HibernateDAO.saveOrUpdate(dynamic1);//更新到持久层
				/**
				 * 如果点击生效进行推送
				 */
				try {
					
				
				if(dynamic.getFbillState()==Status.EFFECT.value()){
						List<String> telsList = (List<String>) cOC_HibernateDAO.findAllByHQL("select  ymember.fmobilePhone from  YBasicMember as ymember where ymember.YBasicSocialgroups.fid='"+dynamic1.getYBasicSocialgroups().getFid()+"'");
						//正式环境
						Set<String> tels = new HashSet<String>(telsList);
						//测试环境
//						Set<String> tels = new HashSet<String>();
//						tels.add("59444947424947494145444243");
//						tels.add("59484946444141494a42464243");
//						tels.add("5944414642424a434944494243");
						if(tels!=null&&tels.size()>0){
							
							Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(tels);
							/**
							 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
							 */
							PushThread pushThread = new PushThread(new Integer(4), deviceIdMap, dynamic1.getFheadline(), dynamic1.getFid(), dynamic1.getYBasicSocialgroups().getFid(), dynmicandInfoPushService);
							pushThread.start();
						
						}else{
							logger.info("没有登录过的设备信息....");
						}
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
				flag = true;
			}else{
				logger.info("数据不存在!");
			}
		return flag;
	}
	@Override
	@SystemServiceLog(description = "修改团体动态")
	public Boolean updateDynamic(YBasicSocialgroupsdynamic dynamic) {
		boolean flag = true;
		YBasicSocialgroupsdynamic dynamic1 = (YBasicSocialgroupsdynamic) cOC_HibernateDAO.get(YBasicSocialgroupsdynamic.class, dynamic.getFid());
		if(null != dynamic1){
			dynamic1.setFbillState(dynamic.getFbillState());
			dynamic1.setFcomment(dynamic.getFcomment());
			dynamic1.setFheadline(dynamic.getFheadline());
			dynamic1.setFmessage(dynamic.getFmessage());
			dynamic1.setFnumber(dynamic.getFnumber());//编号需要自己去获取
			dynamic1.setFlogoImage(dynamic.getFlogoImage());//前段返回已经存好的照片的uri路径
			dynamic1.setFdetailAddress(dynamic1.getFdetailAddress());
			dynamic1.setFpublishTime(new java.sql.Date(new java.util.Date().getTime()));
			/**
			 * 下面还要更新表中外键关系
			 */
			dynamic1.setYBasicType(dynamic.getYBasicType());//类型表
			dynamic1.setYBasicSocialgroups(dynamic.getYBasicSocialgroups());//前台传入YBasicSocialgroups的主键
			cOC_HibernateDAO.update(dynamic1);//更新到持久层
			
			flag = true;
		}else{
			logger.info("数据不存在!");
		}
		return flag;
	}


	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部，或根据条件查询团体动态")
	public PagingList<YBasicSocialgroupsdynamic> getAllDynamicPagingList(Integer page, Integer pageSize,String groupId,String fheadline) {
		PagingList<YBasicSocialgroupsdynamic> pagingList = new PagingList<YBasicSocialgroupsdynamic>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsdynamic dynamic"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsdynamic dynamic"); 
		/**
		 * 根据不同的团体来查询
		 */
		if(null!=groupId&&!"".equals(groupId)){
			hqlList.append(" where dynamic.YBasicSocialgroups.fid=?");
			hqlCount.append(" where dynamic.YBasicSocialgroups.fid=?");
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
			hqlList.append("dynamic.fheadline like ?");
			hqlCount.append("dynamic.fheadline like ?");
			values.add("%"+fheadline+"%");
		}
		
		hqlList.append(" order by dynamic.fpublishTime desc");
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicSocialgroupsdynamic>) cOC_HibernateDAO.find(hqlList.toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
/**
 * 接口方法
 * 
 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据用户id查询团体动态，并按条数返回")
	public Map<String,Object> getDynamicById(String fid,String tel,String businessId){
		Map<String,Object> resultObject = new HashMap<String,Object>();
		List<YComment> ycommentList = null; 
		List<YPointlike> ypointlikeList = null;
		List<YPointlike> mineypointlikeList = null;
		YBasicSocialgroupsdynamic dynamic = (YBasicSocialgroupsdynamic)cOC_HibernateDAO.findById(YBasicSocialgroupsdynamic.class, fid);
		if(dynamic!=null){ //如果这条数据存在 获取他的类型   通过它的类型和fid来查询它是否点赞 点赞数  评论数
			String hql =null;
			hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='3001'";
			ypointlikeList =  (List<YPointlike>)cOC_HibernateDAO.findAllByHQL(hql);
			
			if(ypointlikeList!=null&&ypointlikeList.size()>0){ //只要根据  类型和动态的id查找到数据 说明已经点赞
				resultObject.put("supportNum",ypointlikeList.size());//点赞数
			}else{
				resultObject.put("supportNum", 0);
			}
			if(null!=tel&&!"".equals(tel)&&null!=businessId&&!"".equals(businessId)){
				List<YBasicMember> memberList = (List<YBasicMember>)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'");
				if(null!=memberList&&memberList.size()>0){
					YBasicMember ymember =memberList.get(0);
					hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='3001' and ypointlike.YBasicMember.fid='"+ymember.getFid()+"'";
					mineypointlikeList =  (List<YPointlike>)cOC_HibernateDAO.findAllByHQL(hql);
					if(mineypointlikeList!=null&&mineypointlikeList.size()>0){//根据当前用户来查询
						resultObject.put("isSupport", 0);//当前用户已点赞
					}else{
						resultObject.put("isSupport", 1);//当前用户未点赞
					}
				}else{
					resultObject.put("isSupport", 1);//当前用户未点赞
				}
				resultObject.put("businessId", dynamic.getYBasicSocialgroups().getFid());//businessId	nsstring	商会ID(当该用户为游客的时候返回)
				resultObject.put("businessName", dynamic.getYBasicSocialgroups().getFname());//businessName	nsstring	商会名称(当该用户为游客的时候返回)
			}else{
				resultObject.put("isSupport", 1);//当前用户未点赞
				resultObject.put("businessId", dynamic.getYBasicSocialgroups().getFid());//businessId	nsstring	商会ID(当该用户为游客的时候返回)
				resultObject.put("businessName", dynamic.getYBasicSocialgroups().getFname());//businessName	nsstring	商会名称(当该用户为游客的时候返回)
			}
			
			hql = "from YComment ycomment where ycomment.fforeignId='"+fid+"' and ycomment.YBasicType.fid='3001'";
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
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "通过dynamicID或者当前用户id查询pageSize或者当前")
	public List<Map<String, Object>> getDynamicList(String fid, int pageSize,String businessId,String tel,HttpServletRequest request) {
		Map<String,Object> resultObject =null;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<YBasicSocialgroupsdynamic> dynamicList=null;
		String hql = null;
		if(fid==null||fid.equals("")){
			if(tel!=null){
				if(businessId!=null&&!"".equals(businessId)){
					hql ="from YBasicSocialgroupsdynamic dynamic where dynamic.YBasicSocialgroups.fid='"+businessId+"' and dynamic.fbillState=5 order by dynamic.flag desc";
				}else{
					hql ="from YBasicSocialgroupsdynamic dynamic where  dynamic.fbillState=5 order by dynamic.flag desc";
				}
			}else{
				if(businessId!=null&&!"".equals(businessId)){
					hql="from YBasicSocialgroupsdynamic dynamic where dynamic.YBasicSocialgroups.fid='"+businessId+"' and  dynamic.fbillState=5 order by dynamic.flag desc";
				}else{
					hql="from YBasicSocialgroupsdynamic dynamic where  dynamic.fbillState=5 order by dynamic.flag desc";
				}	
			}
				 dynamicList =  (List<YBasicSocialgroupsdynamic>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}else{
			//根据当前的来分页
			YBasicSocialgroupsdynamic dynamic = (YBasicSocialgroupsdynamic) cOC_HibernateDAO.findById(YBasicSocialgroupsdynamic.class, fid);
			if(tel!=null){
				if(businessId!=null&&!"".equals(businessId)){
					hql="from YBasicSocialgroupsdynamic ybasicdynamic where ybasicdynamic.YBasicSocialgroups.fid='"+businessId+"' and ybasicdynamic.flag< "+dynamic.getFlag()+" and ybasicdynamic.fbillState=5 order by ybasicdynamic.flag desc";
				}else{
					hql="from YBasicSocialgroupsdynamic ybasicdynamic where  ybasicdynamic.flag< "+dynamic.getFlag()+" and ybasicdynamic.fbillState=5 order by ybasicdynamic.flag desc";
				}
			}else{
				if(businessId!=null&&!"".equals(businessId)){
					hql="from YBasicSocialgroupsdynamic ybasicdynamic where ybasicdynamic.YBasicSocialgroups.fid='"+businessId+"' and  ybasicdynamic.fbillState=5 order by ybasicdynamic.flag desc";
				}else{
					hql="from YBasicSocialgroupsdynamic ybasicdynamic where  ybasicdynamic.fbillState=5 order by ybasicdynamic.flag desc";
				}	
			}
			dynamicList = (List<YBasicSocialgroupsdynamic>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}
		if(dynamicList!=null&&dynamicList.size()>0){
			for(YBasicSocialgroupsdynamic dynamic :dynamicList){
				resultObject = new HashMap<String,Object>();
				String logo=dynamic.getFlogoImage();
				String url=null;
				if(null!=logo){
					url = logo.replace(",", "");
				}
				resultObject.put("dynamicImageurl", url);//通知图片地址
				resultObject.put("dynamicId", dynamic.getFid());//通知Id
				resultObject.put("dynamicTitle", dynamic.getFheadline());//通知标题
				resultObject.put("dynamicDescription", FilterHtmlUtil.filterHtml(dynamic.getFmessage()));//通知内容
				Date d=dynamic.getFpublishTime();
				String publishTime=null;
				if(null!=d&&!"".equals(d)){
					publishTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d) ;
				}
				
				resultObject.put("dynamicDate", publishTime);//通知时间
				resultObject.put("dynamicDetailUrl", "webApp/dynamicDetail.html?id="+dynamic.getFid());//通知详情地址
				
				if(tel!=null){
					boolean flag = true;
					try {
						 flag = dynmicandInfoPushService.findRead(dynamic.getFid(),tel);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(flag ==true){
						resultObject.put("isRead", 0);
					}else{
						resultObject.put("isRead", 1);
					}
				}else{
					resultObject.put("isRead", 0);
				}
				resultList.add(resultObject);
			}
		}
		return resultList;
	}
	
	/**
	 * 点赞    点取消如果不存在就新增，如果存在就更改状态
	 * @param fid  dynamicId 
	 * @param userId 当前用户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "点赞    点取消如果不存在就新增，如果存在就更改状态")
	public boolean pointLike(String fid,String tel,String businessId,int type) {
		boolean flag = true;
		List<YPointlike> mineypointlikeList = null;

		String hql = "";
		try{
			YBasicSocialgroupsdynamic dynamic = (YBasicSocialgroupsdynamic)cOC_HibernateDAO.findById(YBasicSocialgroupsdynamic.class, fid);
			YBasicMember ymember =(YBasicMember)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'").get(0);
			hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='3001' and ypointlike.YBasicMember.fid='"+ymember.getFid()+"'";
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
				YBasicMember member =  new YBasicMember();
				member.setFid(ymember.getFid());
				ypointlike.setYBasicMember(member);//操作用户
				ypointlike.setYBasicType(new YBasicType("3001"));//模块类型
				ypointlike.setFpointLikeType(type);//点赞或者取消
				ypointlike.setFmanShowId(fid);
				ypointlike.setFpointLikeTime(new java.sql.Date(new java.util.Date().getTime()));
				cOC_HibernateDAO.save(ypointlike);
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}


	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取动态评论的回复列表（分页）")
	public List<Map<String, Object>> getDynamicReplyList(String dynamicId,String reviewId,int pageSize) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultObject = null;
		String hql = "";
		List<YComment> ycommentList=null;
		//首先获取需要查询的评论pagesize信息
		if(reviewId==null||reviewId.equals("")){
			//如果评论id为空就查询动态最新的pagesize条评论的回复
			 hql ="from YComment ycomment where ycomment.YBasicType.fid='3001' and ycomment.fforeignId='"+dynamicId+"' order by ycomment.flag desc";
			 ycommentList = (List<YComment>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}else{
			//如果评论id不为空，那么就查询id前通过flag排序的pageSize条评论的回复
			//根据当前的来分页
			YComment ycomment = (YComment) cOC_HibernateDAO.findById(YComment.class, reviewId);
			hql="from YComment ycomment where ycomment.YBasicType.fid='3001' and ycomment.fforeignId='"+dynamicId+"' and ycomment.flag< "+ycomment.getFlag()+"  order by ycomment.flag desc";
			ycommentList = (List<YComment>) cOC_HibernateDAO.find(hql, 0, pageSize, null);
		}
		//获取所有的评论的所有的回复
		for(YComment ycommet:ycommentList){
			YBasicMember basicMember = ycommet.getYBasicMember();
				resultObject = new HashMap<String,Object>();
				resultObject.put("reviewId", ycommet.getFid());//评论id
				String url = basicMember.getFheadImage().replace(",", "");
				resultObject.put("userImageUrl", url);//回复人头像
				resultObject.put("userNickeName", basicMember.getFname());//回复人昵称
				resultObject.put("reviewDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ycommet.getFtime()) );//回复时间
				resultObject.put("reviewContent", ycommet.getFcontents());//回复内容
				resultObject.put("mid", basicMember.getFmobilePhone());//用户id
				resultList.add(resultObject);
		}
		return resultList;
	}


	@Override
	@SystemServiceLog(description = "删除动态的评论 按照id")
	public boolean delDynamicReview(String reviewId) {
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
	@SystemServiceLog(description = "添加评论")
	public void addDynamicReview(String tel,String businessId, String dynamicId,
			String reviewContent) {
		YBasicMember ymember =(YBasicMember)cOC_HibernateDAO.findAllByHQL("from YBasicMember ymember where ymember.fmobilePhone='"+tel+"' and ymember.YBasicSocialgroups.fid='"+businessId+"'").get(0);
		YComment comment = new YComment();
		comment.setFcontents(reviewContent);//评论类容
		comment.setFforeignId(dynamicId);//设置关联外键
		comment.setFtime(new java.sql.Date(new java.util.Date().getTime()));
		comment.setYBasicMember(ymember);
		comment.setYBasicType(new YBasicType("3001"));//设置模块类型
		cOC_HibernateDAO.saveOrUpdate(comment);
	}


	@Override
	@SystemServiceLog(description = "根据id查询团体动态")
	public YBasicSocialgroupsdynamic getDynamic(String fid) {
		return (YBasicSocialgroupsdynamic)cOC_HibernateDAO.findById(YBasicSocialgroupsdynamic.class, fid);
	}


	@Override
	@SystemServiceLog(description = "根据id查询团体动态")
	public YBasicSocialgroupsdynamic getById(String fid) {
		return (YBasicSocialgroupsdynamic)cOC_HibernateDAO.findById(YBasicSocialgroupsdynamic.class, fid);
	}


	@Override
	@SystemServiceLog(description = "修改团体动态")
	public void update(YBasicSocialgroupsdynamic dynamic) {
		cOC_HibernateDAO.update(dynamic);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagingList<YBasicSocialgroupsdynamic> getAllDynamicPagingList(Integer start, Integer limit, Integer type) {
		PagingList<YBasicSocialgroupsdynamic> pagingList = new PagingList<YBasicSocialgroupsdynamic>();
		List<Object> values = new ArrayList<Object>();
		/**
		 * type ：0 点赞数  1排序数
		 */
		if(type==0){
			String sql = "select  dynamic.fid,dynamic.fnumber,dynamic.fheadline,dynamic.flogoImage,dynamic.fmessage,dynamic.fbillState,dynamic.fcomment,dynamic.flag,dynamic.fdetailAddress,dynamic.fpublishTime from  y_basic_socialgroupsdynamic dynamic  left join (select pointlike.FManShowID,count(pointlike.FManShowID) as totalcount from  y_pointlike pointlike group by FManShowID) tmp on dynamic.fid=tmp.FManShowID ORDER BY  totalcount desc  limit "+start+","+limit+"";
			pagingList.setList((List<YBasicSocialgroupsdynamic>) cOC_HibernateDAO.getListBySqlVO(sql, YBasicSocialgroupsdynamic.class));
		}else if(type==1){
			String sql = "select dynamic.fid,dynamic.fnumber,dynamic.fheadline,dynamic.flogoImage,dynamic.fmessage,dynamic.fbillState,dynamic.fcomment,dynamic.flag,dynamic.fdetailAddress,dynamic.fpublishTime from  y_basic_socialgroupsdynamic dynamic  left join (select coment.FForeignID,count(coment.FForeignID) as totalcount from  y_comment coment group by FForeignID) tmp on dynamic.fid=tmp.FForeignID ORDER BY  totalcount desc  limit "+start+","+limit+"";
			pagingList.setList((List<YBasicSocialgroupsdynamic>) cOC_HibernateDAO.getListBySqlVO(sql, YBasicSocialgroupsdynamic.class));
		}else{
			StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsdynamic dynamic"); 
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<YBasicSocialgroupsdynamic>) cOC_HibernateDAO.find(hqlList.toString(), start, limit, values.toArray()));
		}
		
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsdynamic dynamic"); 
		
		/**
		 * 获得总条数
		 */
		pagingList.setCount(cOC_HibernateDAO.getTotalCountByCondition(hqlCount.toString(), start, limit, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}
}

