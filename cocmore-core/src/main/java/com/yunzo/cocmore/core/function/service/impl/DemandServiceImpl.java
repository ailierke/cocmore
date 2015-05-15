package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicCounty;
import com.yunzo.cocmore.core.function.model.mysql.YBasicDemandsupplyPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicProvince;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupssupply;
import com.yunzo.cocmore.core.function.model.mysql.YBasicTrade;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YCommentScore;
import com.yunzo.cocmore.core.function.model.mysql.YComplaint;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.service.DemandService;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.FilterHtmlUtil;

/**
 * @author：jackpeng
 * @date：2014年11月25日上午9:19:11
 * 需求service实现类
 */
@Service("demandService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class DemandServiceImpl implements DemandService {

	private static final Logger logger = Logger.getLogger(DemandServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	@Resource(name="memberService")
	MemberServiceI memberService ;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMyDemandList(String fId,Integer pageSize, String userId) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		List<YBasicSocialgroupsdemand> list = null;
		Map<String, Object> mapStr = null;
		YBasicSocialgroupsdemand demandflag = null;
		List<String> listImg = null;
		Integer billState = null;
		Integer isHide = null;
		Integer lag = null;
		String hql = null;
		String hqlMember = null;
		StringBuffer sb = null;
		try {
			//获取会员id
			hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"'";
			List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
			if(listMember != null && listMember.size() > 0){
				sb = new StringBuffer("(");
				for(YBasicMember mem:listMember){
					sb.append("'"+mem.getFid()+"',");
				}
				//去掉最后的","
				sb.replace(sb.lastIndexOf(","),sb.length(),"");
				sb.append(")");
				
				if(fId != null && !fId.equals("")){
					demandflag = (YBasicSocialgroupsdemand) dao.findById(YBasicSocialgroupssupply.class, fId);
					lag = demandflag.getFlag();
					
					hql = "from YBasicSocialgroupsdemand y where y.YBasicMember.fid = "+sb+" and y.flag < "+lag+" order by y.flag desc";
				}else{
					hql = "from YBasicSocialgroupsdemand y where y.YBasicMember.fid = "+sb+" order by y.flag desc";
				}
				list = (List<YBasicSocialgroupsdemand>) dao.find(hql, 0, pageSize, null);
				for(YBasicSocialgroupsdemand demand: list){
					mapStr = new HashMap<String, Object>();
					listImg = new ArrayList<String>();
					String imgs = demand.getFimages();
					if(imgs != null && !imgs.equals("")){
						String[] img = imgs.split(",");
						for(String demandImg:img){
							listImg.add(demandImg);
						}
					}
					mapStr.put("fImageUrl", listImg);
					mapStr.put("fId", demand.getFid());
					mapStr.put("fTitle", demand.getFheadline());
					billState = demand.getFbillState();//获取状态
					isHide = demand.getFisHide();//获取是否隐藏
					if(billState == 5){
						if(isHide != null){
							if(isHide == 1){
								mapStr.put("fType", 1);
							}else{
								mapStr.put("fType", 0);
							}
						}else{
							mapStr.put("fType", 0);
						}
					}
					mapStr.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demand.getFpublisherTime()!=null?demand.getFpublisherTime():new Date()));
//						mapStr.put("isWarrant", demand.getFareGuarantee());
					mapStr.put("fcontent", FilterHtmlUtil.filterHtml(demand.getFmessage()));
					mapStr.put("fRank", demand.getFlevel());
					listMap.add(mapStr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "根据条件查询需求")
	public List<Map<String, Object>> getDemandList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,String provincialId,
			String cityId,int supplyDemandType,String mid) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		List<YBasicSocialgroupsdemand> list = null;
		Map<String, Object> map = null;
		List<String> listImg = null;
		String sql = null;
		Integer lag = null;
		if(fId != null && !"".equals(fId)){
			YBasicSocialgroupsdemand demand = (YBasicSocialgroupsdemand) dao.findById(YBasicSocialgroupsdemand.class, fId);
			lag = demand.getFlag();
		}
		
		StringBuffer hql = new StringBuffer("select y.* from y_basic_socialgroupsdemand y where y.fbillState = 5 and y.FIsHide != 1 ");
		//行业
		if(IsTrade == 0){
			hql.append("and y.FTradeID = '" + tradeId + "' ");
		}
		//地域
		if(IsRegion == 0){
			hql.append("and y.FProvinceID = '" + provincialId + "' ");
			if(cityId != null && !cityId.equals("")){
				hql.append("and y.FCityID = '" + cityId + "' ");
			}
		}
		//信用
		if(IsCredit == 0){
			hql.append("order by y.flevel desc,y.flag desc ");
		}
//		if(IsCredit == 0 && IsWarrant == 0){
//			hql.append("and y.fareGuarantee = 1 order by y.flevel desc,y.flag desc ");
//		}else{
//			if(IsCredit == 0){
//				hql.append("order by y.flevel desc,y.flag desc ");
//			}
//			if(IsWarrant == 0){
//				hql.append("and y.fareGuarantee = 1 ");
//			}
//		}
		
		sql = "(" + hql.toString() + ") yy ";
		
		if(lag == null){
			if(IsCredit == 0){
				sql = "select yy.* from " + sql + " limit 0," + pageSize;
			}else{
				sql = "select yy.* from " + sql + " order by yy.flag desc limit 0," + pageSize;
			}
		}else{
			if(IsCredit == 0){
				sql = "select yy.* from " + sql + "where yy.flag < " + lag + " limit 0," + pageSize;
			}else{
				sql = "select yy.* from " + sql + "where yy.flag < " + lag + " order by yy.flag desc limit 0," + pageSize;
			}
		}
		
		list = (List<YBasicSocialgroupsdemand>) dao.getListBySql(sql, "y_basic_socialgroupsdemand", YBasicSocialgroupsdemand.class);
		
		if(list != null){
			for(int i = 0;i < list.size();i++){
				listImg = new ArrayList<String>();
				
				YBasicSocialgroupsdemand dem = list.get(i);
				map = new HashMap<String, Object>();
				
				String imgs = dem.getFimages();
				if(imgs != null && !imgs.equals("")){
					String[] img = imgs.split(",");
					for (int j = 0; j < img.length; j++) {
						listImg.add(img[j]);
					}
				}
				map.put("fImageUrlArray", listImg);
				map.put("fId", dem.getFid());
				map.put("fType", supplyDemandType);
				map.put("fTitle", dem.getFheadline());
				map.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dem.getFpublisherTime()!=null?dem.getFpublisherTime():new Date()));
				Integer fRank = dem.getfRank();
				if(fRank==null || fRank.equals("")){
					map.put("fRank", 3);
				}else{
					map.put("fRank", fRank);
				}
				map.put("fcontent", FilterHtmlUtil.filterHtml(dem.getFmessage()));
				//获取供应推送记录
//				String dsHql = "from YBasicDemandsupplyPushinfo y where y.ftel = '"+mid+"' and y.fdemandsupplyId = '"+dem.getFid()+"' and y.ftype = 2";
//				List<YBasicDemandsupplyPushinfo> spList = (List<YBasicDemandsupplyPushinfo>) dao.findAllByHQL(dsHql);
//				if(spList != null && spList.size() > 0){	//判断是否存在推送记录
//					YBasicDemandsupplyPushinfo sp = spList.get(0);
//					if(sp.getFstatu() == 22){	//判断是已读还是未读，返回（0：已读，1：未读）
//						map.put("isRead", 1);
//					}else{
//						map.put("isRead", 0);
//					}
//				}else{
//					map.put("isRead", 0);
//				}
				listMap.add(map);
			}
		}
		return listMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取推荐的需求列表（广告栏位置）")
	public List<Map<String, Object>> findSupplyAndDemandList() {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();;
		List<String> listImg = null;
		List<YBasicSocialgroupsdemand> list = null;
		Map<String, Object> mapStr = null;
		String hql = null;
		try {
			hql = "from YBasicSocialgroupsdemand y where y.fimages != '' and y.flevel >= 3 and y.fisHide != 1 order by y.fpublisherTime desc";
			list = (List<YBasicSocialgroupsdemand>) dao.findAllByHQL(hql);
			for(int i = 0;i < list.size();i++){
				listImg = new ArrayList<String>();
				mapStr = new HashMap<String, Object>();
				
				YBasicSocialgroupsdemand demand = list.get(i);
				String imgs = demand.getFimages();
				if(imgs != null && !imgs.equals("")){
					String[] img = imgs.split(",");
					for(String imgOne: img){
						listImg.add(imgOne);
					}
				}
				mapStr.put("fImageUrlArray", listImg);
				mapStr.put("fId", demand.getFid());
				mapStr.put("fType", "1");
				mapStr.put("fTitle", demand.getFheadline());
				mapStr.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demand.getFpublisherTime()!=null?demand.getFpublisherTime():new Date()));
				Integer level = demand.getFlevel();
				if(level==null || level.equals("")){
					mapStr.put("fRank", 0);
				}else{
					mapStr.put("fRank", demand.getFlevel());
				}
//				mapStr.put("isWarrant", supply.getFareGuarantee());
				mapStr.put("fcontent", FilterHtmlUtil.filterHtml(demand.getFmessage()));
				listMap.add(mapStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}

	@SuppressWarnings(value = "unchecked")
	@Override
	@SystemServiceLog(description = "查询全部需求")
	public List<YBasicSocialgroupsdemand> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YBasicSocialgroupsdemand> findAll()");
		return (List<YBasicSocialgroupsdemand>)dao.findAll(YBasicSocialgroupsdemand.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部需求，或根据条件查询需求，并分页")
	public PagingList<YBasicSocialgroupsdemand> getAllDemandPagingList(
			Integer page, Integer pageSize, String groupId, String headline) {
		logger.info("查询信息列表，并分页....");
		PagingList<YBasicSocialgroupsdemand> pagingList = new PagingList<YBasicSocialgroupsdemand>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsdemand y where 1=1"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupsdemand y where 1=1"); 
		/**
		 * 判断是否通过groupId或headline
		 */
		if(groupId != null){
			hqlList.append(" and y.YBasicSocialgroups.fid=?");
			hqlCount.append(" and y.YBasicSocialgroups.fid=?");
			values.add(groupId);
		}
		if(headline != null){
			hqlList.append(" and y.fheadline like '%"+headline+"%'");
			hqlCount.append(" and y.fheadline like '%"+headline+"%'");
		}
		/**
		 * 获得此页数据
		 */
		pagingList.setList((List<YBasicSocialgroupsdemand>) dao.find(hqlList.toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@Override
	@SystemServiceLog(description = "根据id查询需求")
	public YBasicSocialgroupsdemand getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YBasicSocialgroupsdemand getById(String id)");
		return (YBasicSocialgroupsdemand) dao.findById(YBasicSocialgroupsdemand.class, id);
	}

	@Override
	@SystemServiceLog(description = "新增需求")
	public void save(YBasicSocialgroupsdemand demand) {
		// TODO Auto-generated method stub
		dao.save(demand);
	}

	@Override
	@SystemServiceLog(description = "删除需求")
	public void delete(YBasicSocialgroupsdemand demand) {
		// TODO Auto-generated method stub
		dao.delete(demand);
	}

	@Override
	@SystemServiceLog(description = "修改需求")
	public void update(YBasicSocialgroupsdemand demand) {
		// TODO Auto-generated method stub
		dao.update(demand);
	}

	@SuppressWarnings(value = "unchecked")
	@Override
	@SystemServiceLog(description = "hql查询需求")
	public List<YBasicSocialgroupsdemand> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YBasicSocialgroupsdemand> findByHql(String hql)");
		return (List<YBasicSocialgroupsdemand>) dao.findAllByHQL(hql);
	}

	@Override
	@SystemServiceLog(description = "查询我的需求")
	public List<Map<String, Object>> findByMyList(List<YBasicMember> memberlist,
			Integer Size, String fid) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<YBasicSocialgroupsdemand> demands=new ArrayList<YBasicSocialgroupsdemand>();
		try {
			//String hql=null;
//			YBasicMember member=new YBasicMember();
//			member=(YBasicMember)dao.findById(YBasicMember.class, memberId);
			StringBuffer hqlbuffer = new StringBuffer("from YBasicSocialgroupsdemand as y where ");
			
			if(null==fid||"".equals(fid)){
				hqlbuffer.append("  y.fbillState=5  and (");
			}else{
				YBasicSocialgroupsdemand demand=(YBasicSocialgroupsdemand)dao.findById(YBasicSocialgroupsdemand.class, fid);
				hqlbuffer.append("  y.fbillState=5 and y.flag<"+demand.getFlag()+" and (");
				//hql="from YBasicSocialgroupsdemand as y where y.YBasicMember.fid='"+member.getFid()+"'  and y.fbillState=5 and y.flag<"+demand.getFlag()+"  order by f.flag desc";
				
			}
			for(int i=0;i<memberlist.size();i++){
				if(i!=0){
					hqlbuffer.append(" or ");
				}
				hqlbuffer.append("y.YBasicMember.fid='"+memberlist.get(i).getFid()+"'");
			}
			hqlbuffer.append(")  order by y.flag desc");
			demands=(List<YBasicSocialgroupsdemand>)dao.find(hqlbuffer.toString(), 0, Size, null);
			
			for(YBasicSocialgroupsdemand d:demands){
				Map<String,Object> resultObject = new HashMap<String, Object>();
				if( d.getFimages()!=null){
					String[] imgurl=d.getFimages().split(",");
					resultObject.put("fImageUrl", imgurl);//				fImageUrl	String	商会动态图片地址
				}
				resultObject.put("fId", d.getFid());//				fId	String	供需ID
				resultObject.put("fTitle", d.getFheadline());//				fTitle	String	供需标题
				if(d.getFisHide()!=null&&d.getFisHide()==1){
					resultObject.put("fType", 1);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
				}else{
					resultObject.put("fType", 0);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
				}
				
				
				resultObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d.getFstartTime()));//				fDate	String	动态发布日期(yyyy-MM-dd hh:mm:ss)
				resultObject.put("isWarrant", null);//				isWarrant	Int	是否是商会担保(0担保，1未担保)
				resultObject.put("fcontent", d.getFmessage());//fcontent	String	供需文本内容
				resultObject.put("fRank", d.getFlevel());//				fRank Int	供需等级（1-5）

				resultList.add(resultObject);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查看详情")
	public Map<String, Object> getDemandInfo(String fid,String userId,String businessId) {
		boolean flag =memberService.isMember(userId);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		YBasicSocialgroupsdemand demand= (YBasicSocialgroupsdemand) dao.findById(YBasicSocialgroupsdemand.class, fid);
		List<YComment> ycommentList = null; 
		List<YComplaint> ycomplaintList = null; 
		List<YPointlike> ypointlikeList = null;
		List<YPointlike> mineypointlikeList = null;
		List<YComplaint> ycomplaintListYN = null;
		List<Map<String,Object>> warrantTypeList = new ArrayList<Map<String,Object>>();
		String hqlMember = null;
		if(null!=demand){
			resultMap.put("fId", demand.getFid());//供需id
			if(demand.getYBasicMember().getFheadImage()!=null){
				String photoImg = demand.getYBasicMember().getFheadImage().replace("'", "");
				resultMap.put("userImageUrl",photoImg);//发布人头像
			}
			if(demand.getYBasicProvince()!=null){
				resultMap.put("provincialId", demand.getYBasicProvince().getFid());//省份id
			}
			if(demand.getYBasicCity()!=null){
				resultMap.put("cityId",demand.getYBasicCity().getFid());//城市id
			}
			if(demand.getYBasicCounty()!=null){
				resultMap.put("countryId",demand.getYBasicCounty().getFid());//县Id
			}
			if(demand.getYBasicTrade()!=null){
				resultMap.put("tradeId",demand.getYBasicTrade().getFid());//行业id
			}
			resultMap.put("contactsPhone",demand.getFtel());//联系号码
			resultMap.put("expDateStop",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demand.getFfinishTime()));//停止日期
			resultMap.put("expDateStart",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demand.getFstartTime()));//开始日期
			resultMap.put("contactsPerson",demand.getFcontacts());//联系人
			resultMap.put("fTitle",demand.getFheadline());//供需title
			resultMap.put("fContent",FilterHtmlUtil.filterHtml(demand.getFmessage()==null?"":demand.getFmessage()));//供需类容
			resultMap.put("fImageUrlArray",demand.getFimages()==null?new String[0]:demand.getFimages().split(","));//需求图片
			resultMap.put("mid",demand.getYBasicMember().getFmobilePhone());//发布者Id
			resultMap.put("tradeName", demand.getYBasicTrade()==null?"":demand.getYBasicTrade().getFname());
			resultMap.put("isWarrant","");//是否是商会担保
			String hql =null;
			hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='"+3004+"'";
			ypointlikeList =  (List<YPointlike>)dao.findAllByHQL(hql);
			if(ypointlikeList!=null&&ypointlikeList.size()>0){ //只要根据  类型和动态的id查找到数据 说明已经点赞
				resultMap.put("supportNum",ypointlikeList.size());//点赞数
			}else{
				resultMap.put("supportNum", 0);
			}
			//获取会员id
			if(flag==false){
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"'";
			}else{
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
			}
			List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
			if(listMember!=null&&listMember.size()>0){
				YBasicMember mem = listMember.get(0);
				hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='"+3004+"' and ypointlike.YBasicMember.fid='"+mem.getFid()+"'  and fpointLikeType=0";
				mineypointlikeList =  (List<YPointlike>)dao.findAllByHQL(hql);
				if(mineypointlikeList!=null&&mineypointlikeList.size()>0){//根据当前用户来查询
					resultMap.put("isSupport", 0);//当前用户已点赞
				}else{
					resultMap.put("isSupport", 1);//当前用户未点赞
				}
				hql = "from YComplaint ycomplaint where ycomplaint.complaintId='"+mem.getFid()+"' and ycomplaint.ftype=1 and ycomplaint.fsupplyDemandId='"+fid+"'";//查询自己对需求是否投诉
				ycomplaintListYN =  (List<YComplaint>)dao.findAllByHQL(hql);
				if(ycomplaintListYN!=null&&ycomplaintListYN.size()>0){
					resultMap.put("isComplain", 0);//自己已投诉	
				}else{
					resultMap.put("isComplain", 1);//为投诉
				}
			}else{
				resultMap.put("isSupport", 1);//当前用户未点赞
				resultMap.put("isComplain", 1);//为投诉
			}
			
			/**
			 * 看是否评分
			 */
			hql = "from YCommentScore ycommentscore where ycommentscore.tel='"+userId+"' and ycommentscore.demandOrSupplyId='"+fid+"'";//查询自己对需求是否投诉
			List<YCommentScore> commentScoreList =  (List<YCommentScore>)dao.findAllByHQL(hql);
			if(null!=commentScoreList&&commentScoreList.size()>0){
				resultMap.put("isGrade", 0);//已评分
				resultMap.put("gradeNum",new Integer(commentScoreList.get(0).getScore()));
			}else{
				resultMap.put("isGrade", 1);//未评分
				resultMap.put("gradeNum",3);
			}
			hql = "from YComment ycomment where ycomment.fforeignId='"+fid+"' and ycomment.YBasicType.fid='"+3004+"'";
			ycommentList =  (List<YComment>)dao.findAllByHQL(hql);
			if(ycommentList!=null&&ycommentList.size()>0){
				resultMap.put("reviewNum", ycommentList.size());//获取评论总数
			}else{
				resultMap.put("reviewNum", 0);//如果不存在，评论数就是0
			}
			hql = "from YComplaint ycomplaint where ycomplaint.ftype=1 and ycomplaint.fsupplyDemandId='"+fid+"'";//查询需求的所有投诉信息
			ycomplaintList =  (List<YComplaint>)dao.findAllByHQL(hql);
			if(ycomplaintList!=null&&ycomplaintList.size()>0){
				resultMap.put("complainNum", ycomplaintList.size());//获取评论总数
			}else{
				resultMap.put("complainNum", 0);//如果不存在，评论数就是0
			}
			resultMap.put("warrantBusiness", warrantTypeList);
		}
		
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "点赞")
	public void saveDemandForSupport(String fid, String userId,int type,String businessId) {
		List<YPointlike> mineypointlikeList = null;
		String hql = null;
		String hqlMember = null;
		//获取会员id
		boolean flag =memberService.isMember(userId);
		if(flag==false){
			hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"'";
		}else{
			hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
		}
		List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
		YBasicMember mem = listMember.get(0);
		hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='"+3004+"' and ypointlike.YBasicMember.fid='"+mem.getFid()+"'";
		mineypointlikeList =  (List<YPointlike>)dao.findAllByHQL(hql);
		YPointlike ypointlike = null;
		if(mineypointlikeList!=null&&mineypointlikeList.size()>0){
			//如果此用户、此模块、此通知id在点赞表中存在     就修改表态状态
			ypointlike = mineypointlikeList.get(0);
			ypointlike.setFpointLikeType(type);//更改表态为点赞或者取消
			ypointlike.setFpointLikeTime(new java.sql.Date(new java.util.Date().getTime()));
			dao.update(ypointlike);//更新数据库
		}else{
			//如果此条记录不存在就新增
			ypointlike = new YPointlike(); 
			YBasicMember member =  new YBasicMember();
			member.setFid(mem.getFid());
			ypointlike.setYBasicMember(member);//操作用户
			ypointlike.setYBasicType(new YBasicType("3004"));//模块类型
			ypointlike.setFpointLikeType(type);//点赞或者取消
			ypointlike.setFmanShowId(fid);
			ypointlike.setFpointLikeTime(new java.sql.Date(new java.util.Date().getTime()));
			dao.save(ypointlike);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "投诉需求")
	public void saveComplaint(String fId, String memberId,String businessId) {
		String hqlMember = null;
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
			
			YComplaint complaint=new YComplaint();
			if(null==fId||"".equals(fId)){
				String hql="from YBasicSocialgroupsdemand as y  Order by y.flag desc";
				List<YBasicSocialgroupsdemand> demands=(List<YBasicSocialgroupsdemand>)dao.find(hql, 0, 1, null);
				YBasicSocialgroupsdemand demand=demands.get(0);
				complaint.setFsupplyDemandId(demand.getFid());
			}else{
				complaint.setFsupplyDemandId(fId);
			}
			complaint.setFtype(1);
			complaint.setComplaintId(mem.getFid());
			dao.save(complaint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public PagingList<YBasicSocialgroupsdemand> findDemandByComment(
			Integer page, Integer pageSize) {
		PagingList<YBasicSocialgroupsdemand> pagingList = new PagingList<YBasicSocialgroupsdemand>();
		try {
			//String sql="select y.* from y_basic_socialgroupsdemand y left JOIN (select FForeignID, count(*) as num from y_comment  GROUP BY FForeignID) c on y.fid=c.FForeignID order by c.num desc";
//			String sql="select * from y_basic_socialgroupsdemand";
//			String sql="select y.FID as fid,y.FNumber as fnumber ,y.FHeadline as fheadline,y.FImages as fimages,"
//					+ "y.FMessage as fmessage,y.FSocialGroupsID as YBasicSocialgroups,y.FPublisherID as YBasicMember,"
//					+ "y.FContacts as fcontacts,y.FTel as ftel,y.FStartTime as fstartTime,y.FFinishTime as ffinishTime,"
//					+ "y.FBillState as fbillState,y.FComment as fcomment,y.FLag as flag,y.FIsHide as fisHide,"
//					+ "y.FProvinceID as YBasicProvince,y.FCityID as YBasicCity,y.FCountyID as YBasicCounty,y.FTradeID as YBasicTrade,"
//					+ "y.Flevel as flevel,y.FPublisherTime as fpublisherTime,y.FRank as fRank"
//					+ " "+ "from y_basic_socialgroupsdemand y left JOIN (select FForeignID, count(*) as num from y_comment  GROUP BY FForeignID) c on y.fid=c.FForeignID order by c.num desc";
			String sql="select y.FID as fid,y.FNumber as fnumber ,y.FHeadline as fheadline,y.FImages as fimages,"
					+ "y.FMessage as fmessage,"
					+ "y.FContacts as fcontacts,y.FTel as ftel,y.FStartTime as fstartTime,y.FFinishTime as ffinishTime,"
					+ "y.FBillState as fbillState,y.FComment as fcomment,y.FLag as flag,y.FIsHide as fisHide,"
					+ "y.Flevel as flevel,y.FPublisherTime as fpublisherTime,y.FRank as fRank"
					+ " "+ "from y_basic_socialgroupsdemand y left JOIN (select FForeignID, count(*) as num from y_comment  GROUP BY FForeignID) c on y.fid=c.FForeignID order by c.num desc";
			List<YBasicSocialgroupsdemand> list=(List<YBasicSocialgroupsdemand>)dao.getListBySqlVO(sql,YBasicSocialgroupsdemand.class);
			pagingList.setCount(list.size());
			if(null!=page&&null!=pageSize){
				sql=sql+"  limit "+page+","+pageSize;
				list=(List<YBasicSocialgroupsdemand>)dao.getListBySqlVO(sql,YBasicSocialgroupsdemand.class);
			}
			pagingList.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagingList;
	}

	@Override
	public PagingList<YBasicSocialgroupsdemand> findDemandByPointLike(
			Integer page, Integer pageSize) {
		PagingList<YBasicSocialgroupsdemand> pagingList = new PagingList<YBasicSocialgroupsdemand>();
		try {
			//String sql="select y.* from y_basic_socialgroupsdemand y left JOIN (select FForeignID, count(*) as num from y_comment  GROUP BY FForeignID) c on y.fid=c.FForeignID order by c.num desc";
//			String sql="select * from y_basic_socialgroupsdemand";
//			String sql="select y.FID as fid,y.FNumber as fnumber ,y.FHeadline as fheadline,y.FImages as fimages,"
//					+ "y.FMessage as fmessage,y.FSocialGroupsID as YBasicSocialgroups,y.FPublisherID as YBasicMember,"
//					+ "y.FContacts as fcontacts,y.FTel as ftel,y.FStartTime as fstartTime,y.FFinishTime as ffinishTime,"
//					+ "y.FBillState as fbillState,y.FComment as fcomment,y.FLag as flag,y.FIsHide as fisHide,"
//					+ "y.FProvinceID as YBasicProvince,y.FCityID as YBasicCity,y.FCountyID as YBasicCounty,y.FTradeID as YBasicTrade,"
//					+ "y.Flevel as flevel,y.FPublisherTime as fpublisherTime,y.FRank as fRank"
//					+ " "+ "from y_basic_socialgroupsdemand y left JOIN (select FForeignID, count(*) as num from y_comment  GROUP BY FForeignID) c on y.fid=c.FForeignID order by c.num desc";
			String sql="select y.FID as fid,y.FNumber as fnumber ,y.FHeadline as fheadline,y.FImages as fimages,"
					+ "y.FMessage as fmessage,y.FContacts as fcontacts,y.FTel as ftel,y.FStartTime as fstartTime,"
					+ "y.FFinishTime as ffinishTime,y.FBillState as fbillState,y.FComment as fcomment,y.FLag as flag,"
					+ "y.FIsHide as fisHide,y.Flevel as flevel,y.FPublisherTime as fpublisherTime,"
					+ "y.FRank as fRank from y_basic_socialgroupsdemand y left JOIN (select FManShowID,count(*) as num from y_pointlike  where FPointLikeType='0' GROUP BY FManShowID) c on y.fid=c.FManShowID order by c.num desc";
			List<YBasicSocialgroupsdemand> list=(List<YBasicSocialgroupsdemand>)dao.getListBySqlVO(sql,YBasicSocialgroupsdemand.class);
			pagingList.setCount(list.size());
			if(null!=page&&null!=pageSize){
				sql=sql+"  limit "+page+","+pageSize;
				list=(List<YBasicSocialgroupsdemand>)dao.getListBySqlVO(sql,YBasicSocialgroupsdemand.class);
			}
			
			
			pagingList.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagingList;
	}

	@Override
	public PagingList<YBasicSocialgroupsdemand> findDemandBytime(Integer page,
			Integer pageSize) {
		logger.info("查询信息列表，并分页....");
		PagingList<YBasicSocialgroupsdemand> pagingList = new PagingList<YBasicSocialgroupsdemand>();
		try {
			StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupsdemand y  order by y.fpublisherTime desc "); 
			StringBuffer hql = new StringBuffer("select count(*) from YBasicSocialgroupsdemand y  order by y.fpublisherTime desc "); 
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<YBasicSocialgroupsdemand>) dao.find(hqlList.toString(), page, pageSize, null));
			pagingList.setCount(dao.getTotalCountByCondition(hql.toString(), page, pageSize, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagingList;
	}
	
}
