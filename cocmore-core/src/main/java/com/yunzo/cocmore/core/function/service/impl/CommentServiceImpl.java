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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplycmentPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.DemandSupplyComentPushService;
import com.yunzo.cocmore.core.function.service.GroupsInformService;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.vo.PushVo;
import com.yunzo.cocmore.core.thread.PushThread;

/**
 * 评论接口实现类
 * @author yunzo
 *
 */
@Service("commentService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class CommentServiceImpl implements CommentService
{

	private static final Logger logger = Logger.getLogger(CommentServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	@Resource
	GroupsInformService groupsInformService;
	@Resource(name="demandSupplyComentPushService")
	DemandSupplyComentPushService demandSupplyComentPushService;
	@Resource(name="memberService")
	MemberServiceI memberService ;
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据关联id查询评论条数")
	public String getCountNum(String id) {
		String hql = "from YComment y where y.fforeignId = '"+id+"'";
		List<YComment> list = (List<YComment>) dao.findAllByHQL(hql);
		int nums = list.size();
		String count = String.valueOf(nums);
		return count;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据关联id，hql查询评论，并按条数返回数据")
	public List<Map<String, Object>> getByHql(String commentID,String fId, int pageSize) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapValues = null;
		List<YComment> list = null;
		YComment comment = null;
		String hql = null;
		int lag = 0;
		if(commentID != null && !"".equals(commentID)){
			YComment comLag = (YComment) dao.findById(YComment.class, commentID);
			lag = comLag.getFlag();
			
			hql = "from YComment y where y.flag < "+lag+" and y.fforeignId = '"+fId+"' order by y.flag desc";
		}else{
			hql = "from YComment y where y.fforeignId = '"+fId+"' order by y.flag desc";
		}
		
		list = (List<YComment>) dao.find(hql, 0, pageSize, null);
		for(int i = 0;i < list.size();i++){
			mapValues = new HashMap<String, Object>();
			comment = list.get(i);
			mapValues.put("userImageUrl", comment.getYBasicMember().getFheadImage());
			mapValues.put("userNickeName", comment.getYBasicMember().getFname());
			mapValues.put("reviewDate", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(comment.getFtime()));
			mapValues.put("reviewContent", comment.getFcontents());
			mapValues.put("commentID", comment.getFid());
			mapValues.put("mid", comment.getYBasicMember().getFmobilePhone());
			listMap.add(mapValues);
		}
		return listMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询评论，并按条数返回数据")
	public List<YComment> findByHql(String hql, Integer rowNum) {
		// TODO Auto-generated method stub
		return (List<YComment>) dao.find(hql, 0, rowNum, null);
	}

	@Override
	@SystemServiceLog(description = "新增评论")
	public void save(YComment comment) {
		logger.info("CommentService save");
		dao.save(comment);
	}

	@Override
	@SystemServiceLog(description = "删除评论")
	public void delete(YComment comment) {
		logger.info("CommentService delete");
		dao.delete(comment);
		
	}

	@Override
	@SystemServiceLog(description = "修改评论")
	public void update(YComment comment) {
		logger.info("CommentService update");
		dao.update(comment);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部评论")
	public List<YComment> findAll() {
		logger.info("CommentService findAll");
		return (List<YComment>)dao.findAll(YComment.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "hql查询评论")
	public List<YComment> findByHql(String hql) {
		logger.info("CommentService findByHql");	
		return (List<YComment>)dao.findAllByHQL(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据新闻id查询，并分页")
	public PagingList<YComment> getAllDynamicPagingList(String ftid,
			Integer start, Integer limit) {
		logger.info("CommentService getAllDynamicPagingList");
		PagingList<YComment> pagingList = new PagingList<YComment>();
		//不分页就是查询全部
		if(start == null && limit == null){
			pagingList.setList((List<YComment>)dao.findAll(YComment.class));
			return pagingList;
		}
		
		String hql = "from YComment y where 1=1 ";
		//获取数据
		if(ftid != null){
			hql += "and y.fforeignId = '" + ftid + "'";
		}
		pagingList.setList((List<YComment>) dao.find(hql, start, limit, null));
		//获取总条数
		List<YComment> list = (List<YComment>)dao.find(hql);
		pagingList.setCount(list.size());
		
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询")
	public YComment getById(String id) {
		return (YComment)dao.findById(YComment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "评论供需")
	public Map<String, Object> savesupplyDemand(String memberId, String fContent,
			Integer supplyDemandType, String fId,String businessId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hqlMember = null;
		/**
		 * 推送所需的tel 供需发布人的id
		 */
		Set<String> tels = new HashSet<String> ();
		String groupId ="";//这条供需所属商会id
		boolean flag =memberService.isMember(memberId);
		try {
			//获取会员id
			if(flag==false){
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+memberId+"'";
			}else{
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+memberId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
			}
			List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
			YBasicMember mem = listMember.get(0);
			
			YComment comment=new YComment();
			String commentId = null;
			if(supplyDemandType==0){
				YBasicType type=new YBasicType();
				type.setFid("3103");
				if(null==fId||"".equals(fId)){
					List<YBasicSocialgroupssupply> supplys=new ArrayList<YBasicSocialgroupssupply>();
					String hql="from YBasicSocialgroupssupply  Order by flag desc";
					supplys=(List<YBasicSocialgroupssupply>)dao.find(hql,0,1,null);
					comment.setFforeignId(supplys.get(0).getFid());
					try {
						groupId = supplys.get(0).getYBasicSocialgroups().getFid();
						tels.add(supplys.get(0).getYBasicMember().getFmobilePhone());
					} catch (Exception e) {
						//出现异常就不加进去
					}
				}else{
					YBasicSocialgroupssupply supply=new YBasicSocialgroupssupply();
					supply = (YBasicSocialgroupssupply)dao.findById(YBasicSocialgroupssupply.class, fId);
					if(supply!=null){
						String mobilephone = supply.getYBasicMember().getFmobilePhone();
						groupId = supply.getYBasicSocialgroups().getFid();
						tels.add(mobilephone);
					}
					comment.setFforeignId(fId);
					
				}
				comment.setFtime(new Date());
				comment.setYBasicMember(mem);
				comment.setFcontents(fContent);
				comment.setYBasicType(type);
				dao.save(comment);
				commentId = comment.getFid();
			}else{
				YBasicType type=new YBasicType();
				type.setFid("3104");
				if(null==fId||"".equals(fId)){
					List<YBasicSocialgroupsdemand> demands=new ArrayList<YBasicSocialgroupsdemand>();
					String hql="from YBasicSocialgroupsdemand  Order by flag desc";
					demands=(List<YBasicSocialgroupsdemand>)dao.find(hql,0,1,null);
					comment.setFforeignId(demands.get(0).getFid());
					/**
					 * update by ailierke
					 */
					try {
						groupId = demands.get(0).getYBasicSocialgroups().getFid();
						tels.add((demands.get(0).getYBasicMember().getFmobilePhone()));
					} catch (Exception e) {
						//出现异常就不加进去
					}
				}else{
					/**
					 * update by ailierke
					 */
					YBasicSocialgroupsdemand demand=new YBasicSocialgroupsdemand();
					demand = (YBasicSocialgroupsdemand)dao.findById(YBasicSocialgroupsdemand.class, fId);
					if(demand!=null){
						String mobilephone = demand.getYBasicMember().getFmobilePhone();
						groupId = demand.getYBasicSocialgroups().getFid();
						tels.add(mobilephone);
					}
					comment.setFforeignId(fId);
				}
				comment.setFtime(new Date());
				comment.setYBasicMember(mem);
				comment.setFcontents(fContent);
				comment.setYBasicType(type);
				dao.save(comment);
				commentId = comment.getFid();
			}
			map.put("commentId", commentId);
			
			if(tels!=null&&tels.size()>0){
				Map<String,PushVo> deviceIdMap  =  groupsInformService.getOutRepeat(tels);
				/**
				 * 开启推送线程，进行推送行为的记录和系统日志的记录  app端的推送
				 */
				PushThread pushThread = new PushThread(supplyDemandType==0?new Integer(7):new Integer(8), deviceIdMap, supplyDemandType==0?"有人评论了你的供应":"有人评论了你的需求", fId, groupId, demandSupplyComentPushService);
				pushThread.start();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	@SystemServiceLog(description = "删除供需评论")
	public boolean delsupplyDemand(String commentID) {
		boolean bool=false;
		try {
			YComment comment=(YComment)dao.findById(YComment.class, commentID);
			dao.delete(comment);
			bool=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bool;
	}

}
