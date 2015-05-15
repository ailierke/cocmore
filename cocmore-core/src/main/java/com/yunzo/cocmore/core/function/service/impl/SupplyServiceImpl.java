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
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.DemandAndSupply;
import com.yunzo.cocmore.core.function.model.mysql.YAppdevice;
import com.yunzo.cocmore.core.function.model.mysql.YBasicAssurancecontent;
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
import com.yunzo.cocmore.core.function.model.mysql.YSupplyGroup;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessage;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationMessagerecord;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.SupplyService;
import com.yunzo.cocmore.core.function.util.CheckBytes;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Status;
import com.yunzo.cocmore.core.function.vo.GuaranteeVo;
import com.yunzo.cocmore.utils.base.FilterHtmlUtil;
import com.yunzo.cocmore.utils.gexin.PushToListMessage;
import com.yunzo.cocmore.utils.gexin.TemplateType;

/**
 * @author：jackpeng
 * @date：2014年11月24日下午2:37:23
 * 供应Service实现类
 */
@Service("supplyService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class SupplyServiceImpl implements SupplyService {

	private static final Logger logger = Logger.getLogger(SupplyServiceImpl.class);

	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;
	@Resource(name="memberService")
	MemberServiceI memberService ;
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查看指定用户的供需列表")
	public List<Map<String, Object>> getMySupplyList(String fId,Integer pageSize, String userId) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapStr = null;
		List<YBasicSocialgroupssupply> list = null;
		YBasicSocialgroupssupply supplyflag = null;
		Integer lag = null;
		Integer billState = null;
		Integer isHide = null;
		String hql = null;
		String hqlMember = null;
		List<String > listImg = null;
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
					supplyflag = (YBasicSocialgroupssupply) dao.findById(YBasicSocialgroupssupply.class, fId);
					lag = supplyflag.getFlag();
					
					hql = "from YBasicSocialgroupssupply y where y.YBasicMember.fid in "+sb+" and y.flag < "+lag+" order by y.flag desc";
				}else{
					hql = "from YBasicSocialgroupssupply y where y.YBasicMember.fid in "+sb+" order by y.flag desc";
				}
				list = (List<YBasicSocialgroupssupply>) dao.find(hql, 0, pageSize, null);
				for(YBasicSocialgroupssupply supply: list){
					mapStr = new HashMap<String, Object>();
					listImg = new ArrayList<String>();
					String imgs = supply.getFimages();
					if(imgs != null && !imgs.equals("")){
						String[] img = imgs.split(",");
						for(String supplyImg:img){
							listImg.add(supplyImg);
						}
					}
					mapStr.put("fImageUrl", listImg);
					mapStr.put("fId", supply.getFid());
					mapStr.put("fTitle", supply.getFheadline());
					billState = supply.getFbillState();//获取状态
					isHide = supply.getFisHide();//获取是否隐藏(1：是，0：否)
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
					}else if(billState == 6){
						mapStr.put("fType", 3);
					}else if(billState == 11){
						mapStr.put("fType", 2);
					}
					mapStr.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(supply.getFpublisherTime()!=null?supply.getFpublisherTime():new Date()));
					mapStr.put("isWarrant", supply.getFareGuarantee());
					mapStr.put("fcontent", FilterHtmlUtil.filterHtml(supply.getFmessage()));
					mapStr.put("fRank", supply.getfRank());
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
	@SystemServiceLog(description = "根据条件查询供应，并分页")
	public List<Map<String, Object>> getSupplyList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,String provincialId,
			String cityId,int supplyDemandType,String mid) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		List<YBasicSocialgroupssupply> list = null;
		Map<String, Object> map = null;
		List<String> listImg = null;
		Integer lag = null;
		String sql = null;
		if(fId != null && !"".equals(fId)){
			YBasicSocialgroupssupply supply = (YBasicSocialgroupssupply) dao.findById(YBasicSocialgroupssupply.class, fId);
			lag = supply.getFlag();
		}
		
		StringBuffer hql = new StringBuffer("select y.* from y_basic_socialgroupssupply y where y.fbillState = 5 and y.FIsHide != 1 ");
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
		//商会认证and信用
		if(IsCredit == 0 && IsWarrant == 0){
			hql.append("and y.fareGuarantee = 1 order by y.flevel desc,y.flag desc ");
		}else{
			if(IsCredit == 0){
				hql.append("order by y.flevel desc,y.flag desc ");
			}
			if(IsWarrant == 0){
				hql.append("and y.fareGuarantee = 1 ");
			}
		}
		
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
		
		list = (List<YBasicSocialgroupssupply>) dao.getListBySql(sql,"y_basic_socialgroupssupply",YBasicSocialgroupssupply.class);
		
		if(list != null){
			for(int i = 0;i < list.size();i++){
				listImg = new ArrayList<String>();

				YBasicSocialgroupssupply sup = list.get(i);
				map = new HashMap<String, Object>();

				String imgs = sup.getFimages();
				if(imgs != null && !imgs.equals("")){
					String[] img = imgs.split(",");
					for (int j = 0; j < img.length; j++) {
						listImg.add(img[j]);
					}
				}
				map.put("fImageUrlArray", listImg);
				map.put("fId", sup.getFid());
				map.put("fType", supplyDemandType);
				map.put("fTitle", sup.getFheadline());
				map.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sup.getFpublisherTime()!=null?sup.getFpublisherTime():new Date()));
				Integer fRank = sup.getfRank();
				if(fRank==null || fRank.equals("")){
					map.put("fRank", 3);
				}else{
					map.put("fRank", fRank);
				}
				map.put("isWarrant", sup.getFareGuarantee());
				map.put("fcontent", FilterHtmlUtil.filterHtml(sup.getFmessage()));
				//获取供应推送记录
//				String dsHql = "from YBasicDemandsupplyPushinfo y where y.ftel = '"+mid+"' and y.fdemandsupplyId = '"+sup.getFid()+"' and y.ftype = 1";
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
	@SystemServiceLog(description = "同时获取供应和需求信息")
	public List<Map<String, Object>> getSupAndDemList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,
			String provincialId,String cityId,String mid){
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		List<DemandAndSupply> listds = null;
		List<String> listdsImg = null;
		Map<String, Object> mapds = null;
		DemandAndSupply dsTime = null;
		Date publisherTime = null;
		String sql = null;
		try {
			if(fId != null && !"".equals(fId)){
				dsTime = (DemandAndSupply) dao.findById(DemandAndSupply.class, fId);
				if(dsTime != null){
					//获取发布时间
					publisherTime = dsTime.getFpublisherTime();
				}
			}
			
			StringBuffer hql = new StringBuffer("select y.* from demandandsupply y where y.fbillState = 5 and y.FIsHide != 1 ");
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
			//商会认证and信用
			if(IsCredit == 0 && IsWarrant == 0){
				hql.append("and y.fareGuarantee = 1 order by y.flevel desc,y.fpublisherTime desc,y.flag desc ");
			}else{
				if(IsCredit == 0){
					hql.append("order by y.flevel desc,y.fpublisherTime desc,y.flag desc ");
				}
				if(IsWarrant == 0){
					hql.append("and y.fareGuarantee = 1 ");
				}
			}
			
			sql = "(" + hql.toString() + ") yy ";
			
			if(publisherTime == null){
				if(IsCredit == 0){
					sql = "select yy.* from " + sql + " limit 0," + pageSize;
				}else{
					sql = "select yy.* from " + sql + " order by yy.fpublisherTime desc,yy.flag desc limit 0," + pageSize;
				}
			}else{
				if(IsCredit == 0){
					sql = "select yy.* from " + sql + "where yy.fpublisherTime < '" + publisherTime + "' limit 0," + pageSize;
				}else{
					sql = "select yy.* from " + sql + "where yy.fpublisherTime < '" + publisherTime + "' order by yy.fpublisherTime desc,yy.flag desc limit 0," + pageSize;
				}
			}
			
			listds = (List<DemandAndSupply>) dao.getListBySql(sql, "demandandsupply", DemandAndSupply.class);
			
			for(DemandAndSupply dands: listds){
				listdsImg = new ArrayList<String>();
				mapds = new HashMap<String, Object>();
				String imgs = dands.getFimages();
				if(imgs != null && !imgs.equals("")){
					String[] img = imgs.split(",");
					for (int j = 0; j < img.length; j++) {
						listdsImg.add(img[j]);
					}
				}
				mapds.put("fImageUrlArray", listdsImg);
				mapds.put("fId", dands.getFid());
				mapds.put("fType", dands.getFtype());
				mapds.put("fTitle", dands.getFheadline());
				mapds.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dands.getFpublisherTime()!=null?dands.getFpublisherTime():new Date()));
				Integer fRank = dands.getfRank();
				if(fRank==null || fRank.equals("")){
					mapds.put("fRank", 0);
				}else{
					mapds.put("fRank", fRank);
				}
				mapds.put("isWarrant", dands.getFareGuarantee());
				mapds.put("fcontent", FilterHtmlUtil.filterHtml(dands.getFmessage()));
				//获取供应推送记录
//				String dsHql = "from YBasicDemandsupplyPushinfo y where y.ftel = '"+mid+"' and y.fdemandsupplyId = '"+dands.getFid()+"'";
//				List<YBasicDemandsupplyPushinfo> spList = (List<YBasicDemandsupplyPushinfo>) dao.findAllByHQL(dsHql);
//				if(spList != null && spList.size() > 0){	//判断是否存在推送记录
//					YBasicDemandsupplyPushinfo sp = spList.get(0);
//					if(sp.getFstatu() == 22){	//判断是已读还是未读，返回（0：已读，1：未读）
//						mapds.put("isRead", 1);
//					}else{
//						mapds.put("isRead", 0);
//					}
//				}else{
//					mapds.put("isRead", 0);
//				}
				listMap.add(mapds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "推荐供应")
	public List<Map<String, Object>> getRecommendationList(String fId,int IsWarrant,int IsCredit,int IsTrade,int IsRegion,int pageSize,String tradeId,
			String provincialId,String cityId,String mid){
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		List<DemandAndSupply> listds = null;
		List<String> listdsImg = null;
		Map<String, Object> mapds = null;
		DemandAndSupply dsTime = null;
		Date publisherTime = null;
		String sql = null;
		try {
			if(fId != null && !"".equals(fId)){
				dsTime = (DemandAndSupply) dao.findById(DemandAndSupply.class, fId);
				if(dsTime != null){
					//获取发布时间
					publisherTime = dsTime.getFpublisherTime();
				}
			}
			
			StringBuffer hql = new StringBuffer("select y.* from demandandsupply y where y.fareGuarantee = 1 and y.flevel >= 3 and y.fbillState = 5 and y.FIsHide != 1 ");
			//行业
			if(IsTrade == 0){
				hql.append("and y.FTradeID = '" + tradeId + "' ");
			}
			
			sql = "(" + hql.toString() + ") yy ";
			
			if(publisherTime == null){
				sql = "select yy.* from " + sql + " order by yy.flevel desc,yy.fpublisherTime desc,yy.flag desc limit 0," + pageSize;
			}else{
				sql = "select yy.* from " + sql + "where yy.fpublisherTime < '" + publisherTime + "' order by yy.flevel desc,yy.fpublisherTime desc,yy.flag desc limit 0," + pageSize;
			}
			
			listds = (List<DemandAndSupply>) dao.getListBySql(sql, "demandandsupply", DemandAndSupply.class);
			
			for(DemandAndSupply dands: listds){
				listdsImg = new ArrayList<String>();
				mapds = new HashMap<String, Object>();
				String imgs = dands.getFimages();
				if(imgs != null && !imgs.equals("")){
					String[] img = imgs.split(",");
					for (int j = 0; j < img.length; j++) {
						listdsImg.add(img[j]);
					}
				}
				mapds.put("fImageUrlArray", listdsImg);
				mapds.put("fId", dands.getFid());
				mapds.put("fType", dands.getFtype());
				mapds.put("fTitle", dands.getFheadline());
				mapds.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dands.getFpublisherTime()!=null?dands.getFpublisherTime():new Date()));
				Integer fRank = dands.getfRank();
				if(fRank==null || fRank.equals("")){
					mapds.put("fRank", 0);
				}else{
					mapds.put("fRank", fRank);
				}
				mapds.put("isWarrant", dands.getFareGuarantee());
				mapds.put("fcontent", FilterHtmlUtil.filterHtml(dands.getFmessage()));
				//获取供应推送记录
//				String dsHql = "from YBasicDemandsupplyPushinfo y where y.ftel = '"+mid+"' and y.fdemandsupplyId = '"+dands.getFid()+"'";
//				List<YBasicDemandsupplyPushinfo> spList = (List<YBasicDemandsupplyPushinfo>) dao.findAllByHQL(dsHql);
//				if(spList != null && spList.size() > 0){	//判断是否存在推送记录
//					YBasicDemandsupplyPushinfo sp = spList.get(0);
//					if(sp.getFstatu() == 22){	//判断是已读还是未读，返回（0：已读，1：未读）
//						mapds.put("isRead", 1);
//					}else{
//						mapds.put("isRead", 0);
//					}
//				}else{
//					mapds.put("isRead", 0);
//				}
				listMap.add(mapds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "获取推荐的供应列表")
	public List<Map<String, Object>> findSupplyAndDemandList(){
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		List<String> listImg = null;
		List<YBasicSocialgroupssupply> list = null;
		Map<String, Object> mapStr = null;
		String hql = null;
		try {
			hql = "from YBasicSocialgroupssupply y where y.fimages != '' and y.flevel >= 3 and y.fareGuarantee = 1 and y.fisHide != 1 order by y.fpublisherTime desc";
			list = (List<YBasicSocialgroupssupply>) dao.findAllByHQL(hql);
			if(list.size() <= 0){
				hql = "from YBasicSocialgroupssupply y where y.fimages != '' and y.fareGuarantee = 1 and y.fisHide != 1 order by y.fpublisherTime desc";
				list = (List<YBasicSocialgroupssupply>) dao.findAllByHQL(hql);
				if(list.size() <= 0){
					hql = "from YBasicSocialgroupssupply y where y.fimages != '' and y.flevel >= 3 and y.fisHide != 1 order by y.fpublisherTime desc";
					list = (List<YBasicSocialgroupssupply>) dao.findAllByHQL(hql);
				}
			}
			for(int i = 0;i < list.size();i++){
				listImg = new ArrayList<String>();
				mapStr = new HashMap<String, Object>();

				YBasicSocialgroupssupply supply = list.get(i);
				String imgs = supply.getFimages();
				if(imgs != null && !imgs.equals("")){
					String[] img = imgs.split(",");
					for(String imgOne: img){
						listImg.add(imgOne);
					}
				}
				mapStr.put("fImageUrlArray", listImg);
				mapStr.put("fId", supply.getFid());
				mapStr.put("fType", "0");
				mapStr.put("fTitle", supply.getFheadline());
				mapStr.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(supply.getFpublisherTime()!=null?supply.getFpublisherTime():new Date()));
				Integer level = supply.getFlevel();
				if(level==null || level.equals("")){
					mapStr.put("fRank", 0);
				}else{
					mapStr.put("fRank", supply.getFlevel());
				}
				mapStr.put("isWarrant", supply.getFareGuarantee());
				mapStr.put("fcontent", FilterHtmlUtil.filterHtml(supply.getFmessage()));
				listMap.add(mapStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部供应")
	public List<YBasicSocialgroupssupply> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<YBasicSocialgroupssupply> findAll()");
		return (List<YBasicSocialgroupssupply>) dao.findAll(YBasicSocialgroupssupply.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询全部供应，并分页")
	public PagingList<YBasicSocialgroupssupply> getAllSupplyPagingList(Integer page, Integer pageSize,String groupId,String headline) {
		logger.info("查询信息列表，并分页....");
		PagingList<YBasicSocialgroupssupply> pagingList = new PagingList<YBasicSocialgroupssupply>();
		List<Object> values = new ArrayList<Object>();
		StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupssupply y where 1=1"); 
		StringBuffer hqlCount = new StringBuffer("select count(0) from YBasicSocialgroupssupply y where 1=1"); 
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
		pagingList.setList((List<YBasicSocialgroupssupply>) dao.find(hqlList.toString(), page, pageSize, values.toArray()));
		/**
		 * 获得总条数
		 */
		pagingList.setCount(dao.getTotalCountByCondition(hqlCount.toString(), page, pageSize, values.toArray()));
		logger.info("总条数："+pagingList.getCount());
		logger.info("当前集合："+Arrays.toString(pagingList.getList().toArray()));
		return pagingList;
	}

	@SuppressWarnings("unchecked")
	@SystemServiceLog(description = "根据供应id查询供应担保分配信息")
	public List<YSupplyGroup> findSASupplyId(String id) {
		// TODO Auto-generated method stub
		List<YSupplyGroup> list = null;
		String hql = "from YSupplyGroup y where y.YBasicSocialgroupssupply.fid = '"+id+"'";
		list = (List<YSupplyGroup>) dao.findAllByHQL(hql);
		return list;
	}

	@Override
	@SystemServiceLog(description = "根据id查询供应")
	public YBasicSocialgroupssupply getById(String id) {
		// TODO Auto-generated method stub
		logger.info("YBasicSocialgroupssupply getById(String fid) || fid" == id);
		return (YBasicSocialgroupssupply) dao.findById(YBasicSocialgroupssupply.class, id);
	}

	@Override
	@SystemServiceLog(description = "新增供应")
	public void save(YBasicSocialgroupssupply supply) {
		// TODO Auto-generated method stub
		dao.save(supply);
	}

	@Override
	@SystemServiceLog(description = "新增供应和担保信息")
	public void saveSupply(YBasicSocialgroupssupply supply) {
		// TODO Auto-generated method stub
		Set<YSupplyGroup> supCon = null;
		String[] str = supply.getJsonStr().split("}");
		supply.setFid(UUID.randomUUID().toString());
		if(!"".equals(supply.getJsonStr())){
			try {
				while(supply.getJsonStr().indexOf("undefined") != -1){
					String newJsonStr = supply.getJsonStr().replaceFirst("undefined", UUID.randomUUID().toString());
					supply.setJsonStr(newJsonStr);
				}
				List<YSupplyGroup> list = JSON.parseArray(supply.getJsonStr(), YSupplyGroup.class);
				supCon = new HashSet<YSupplyGroup>();
				for(int i = 0;i < list.size();i++){
					YSupplyGroup ySupplyGroup = list.get(i);
					String[] array = str[i].split(",");
					for(String ycon: array){
						String[] field = ycon.split(":");
						if(field[0].indexOf("YBasicAssurancecontent.fid") != -1){
							YBasicAssurancecontent content = (YBasicAssurancecontent)dao.get(YBasicAssurancecontent.class, field[1].replaceAll("\"", ""));
							ySupplyGroup.setYBasicAssurancecontent(content);
							//update by ailierke
							ySupplyGroup.setGroupid(content.getYBasicSocialgroups().getFid());//加入需要担保的商会id
						}
					}
					ySupplyGroup.setYBasicSocialgroupssupply(supply);
					//update by ailierke
					ySupplyGroup.setIspass(Status.PENDING.value()+"");//15 待审核 加入状态
					ySupplyGroup.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));//加入更新时间

					supCon.add(ySupplyGroup);
				}
				supply.setySupplyGroups(supCon);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dao.save(supply);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "修改供应和担保信息")
	public void updateSupply(YBasicSocialgroupssupply supply) {
		// TODO Auto-generated method stub
		Set<YSupplyGroup> supCon = null;
		List<YSupplyGroup> listSupCon = findSASupplyId(supply.getFid());
		String[] str = supply.getJsonStr().split("}");
		if(!"".equals(supply.getJsonStr())){
			try {
				while(supply.getJsonStr().indexOf("undefined") != -1){
					String newJsonStr = supply.getJsonStr().replaceFirst("undefined", UUID.randomUUID().toString());
					supply.setJsonStr(newJsonStr);
				}
				List<YSupplyGroup> list = JSON.parseArray(supply.getJsonStr(), YSupplyGroup.class);
				supCon = new HashSet<YSupplyGroup>();
				for(int i = 0;i < list.size();i++){
					YSupplyGroup ySupplyGroup = list.get(i);
					String[] array = StringUtils.split(str[i],",");
					for(String ycon: array){
						String[] field = StringUtils.split(ycon,":");
						if(field[0].indexOf("YBasicAssurancecontent.fid") != -1){
							YBasicAssurancecontent content = (YBasicAssurancecontent)dao.get(YBasicAssurancecontent.class, field[1].replaceAll("\"", ""));
							ySupplyGroup.setYBasicAssurancecontent(content);
							//update by ailierke 
							ySupplyGroup.setGroupid(content.getYBasicSocialgroups().getFid());//加入需要担保的商会id
						}
					}
					//update by ailierke
					ySupplyGroup.setIspass(Status.PENDING.value()+"");//15待审核  加入状态
					ySupplyGroup.setUpdatetime(new java.sql.Date(new java.util.Date().getTime()));//加入更新时间

					ySupplyGroup.setYBasicSocialgroupssupply(supply);
					supCon.add(ySupplyGroup);
				}
				//删除前端删除的分录信息
				for(YSupplyGroup cons : listSupCon){
					boolean b = true;
					for(YSupplyGroup con : list){
						if(cons.getFid().equals(con.getFid())){
							b = false;
							break;
						}
					}
					if(b){
						dao.updateBySQL("delete from y_supply_group where fid = '"+cons.getFid()+"'");
					}
				}
				supply.setySupplyGroups(supCon);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dao.update(supply);
	}

	@Override
	@SystemServiceLog(description = "删除供应")
	public void delete(YBasicSocialgroupssupply supply) {
		// TODO Auto-generated method stub
		dao.delete(supply);
	}

	@Override
	@SystemServiceLog(description = "修改供应")
	public void update(YBasicSocialgroupssupply supply) {
		// TODO Auto-generated method stub
		dao.update(supply);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "条件查询供应")
	public List<YBasicSocialgroupssupply> getByHql(String hql) {
		// TODO Auto-generated method stub
		logger.info("List<YBasicSocialgroupssupply> findByHql(String hql)");
		return (List<YBasicSocialgroupssupply>) dao.findAllByHQL(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询我的供应")
	public List<Map<String, Object>> findByMyList(List<YBasicMember> memberlist,
			Integer Size, String fid) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<YBasicSocialgroupssupply> ssupply=new ArrayList<YBasicSocialgroupssupply>();
		try {
			
			StringBuffer hqlbuffer = new StringBuffer("from YBasicSocialgroupssupply as y where ");
			
			
			if(null==fid||"".equals(fid)){
				hqlbuffer.append("  y.fbillState=5  and (");
				//hql="from YBasicSocialgroupssupply as y where y.YBasicMember.fid='"+member.getFid()+"' and y.fbillState=5  order by y.flag desc";
			}else{
				YBasicSocialgroupssupply supply =(YBasicSocialgroupssupply)dao.findById(YBasicSocialgroupssupply.class, fid);
				hqlbuffer.append("  y.fbillState=5  and y.flag<"+supply.getFlag()+" and (");
				//hql="from YBasicSocialgroupssupply as y where y.YBasicMember.fid='"+member.getFid()+"' and y.fbillState=5  and y.flag<"+supply.getFlag()+"  order by y.flag desc";
			}
			for(int i=0;i<memberlist.size();i++){
				if(i!=0){
					hqlbuffer.append(" or ");
				}
				hqlbuffer.append("y.YBasicMember.fid='"+memberlist.get(i).getFid()+"'");
			}
			hqlbuffer.append(")  order by y.flag desc");
			ssupply=(List<YBasicSocialgroupssupply>)dao.find(hqlbuffer.toString(), 0, Size, null);
			System.out.println(hqlbuffer.toString());
			
			for(YBasicSocialgroupssupply s:ssupply){
				Map<String,Object> resultObject = new HashMap<String, Object>();
				if(s.getFimages()!=null){
					String[] images=s.getFimages().split(",");
					resultObject.put("fImageUrl", images);//				fImageUrl	String	商会动态图片地址
				}
				
				resultObject.put("fId", s.getFid());//				fId	String	供需ID
				resultObject.put("fTitle", s.getFheadline());//				fTitle	String	供需标题
				if(s.getFareGuarantee()==1){
					String grouphql="from YSupplyGroup as y where y.YBasicSocialgroupssupply.fid='"+s.getFid()+"'";
					List<YSupplyGroup> grouplist=new ArrayList<YSupplyGroup>();
					grouplist=(List<YSupplyGroup>)dao.findAllByHQL(grouphql);
					if(grouplist.size()>0){
						List<String> cg=new ArrayList<String>();//担保审核成功
						List<String> sb=new ArrayList<String>();//担保审核失败
						List<String> ds=new ArrayList<String>();//担保正在审核
						for(YSupplyGroup g:grouplist){
							if(g.getIspass().equals("16")){
								cg.add("16");
							}else if(g.getIspass().equals("17")){
								sb.add("17");
							}else{
								ds.add("15");
							}
						}
						if(cg.size()>0){
							if(s.getFisHide()==null||s.getFisHide()==0){
								resultObject.put("fType", 0);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
							}else{
								resultObject.put("fType", 1);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
							}
						}else{
							if(ds.size()>0){
								resultObject.put("fType", 2);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
							}else{
								resultObject.put("fType", 3);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
							}
						}
						
					}else{
						resultObject.put("fType", 0);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
					}
				}else{
					if(s.getFisHide()==null||s.getFisHide()==0){
						resultObject.put("fType", 0);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
					}else{
						resultObject.put("fType", 1);//				fType	Int	供需状态(0发布成功状态,1影藏该条供需，2供需认证正在被商会审核，3商会审核供需失败)
					}
					
				}
				resultObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(s.getFpublisherTime()));//				fDate	String	动态发布日期(yyyy-MM-dd hh:mm:ss)
				
				resultObject.put("isWarrant", s.getFareGuarantee());//				isWarrant	Int	是否是商会担保(0担保，1未担保)
				resultObject.put("fcontent", s.getFmessage());//fcontent	String	供需文本内容
				resultObject.put("fRank", s.getFlevel());//				fRank Int	供需等级（1-5）

				resultList.add(resultObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查看供应详情")
	public Map<String, Object> getSupplyInfo(String fid, String userId,String businessId) {
		boolean flag =memberService.isMember(userId);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		YBasicSocialgroupssupply supply= (YBasicSocialgroupssupply) dao.findById(YBasicSocialgroupssupply.class, fid);
		List<YComment> ycommentList = null; 
		List<YComplaint> ycomplaintList = null; 
		List<YPointlike> ypointlikeList = null;
		List<YPointlike> mineypointlikeList = null;
		List<YComplaint> ycomplaintListYN = null;
		Map<String,Object> warrantBusinessObject = null;
		List<Map<String,String>> warrantTypeList = new ArrayList<Map<String,String>>();
		Map<String,String>  warrantTypeObject = null;
		String hqlMember = null;
		if(null!=supply){
			resultMap.put("fId", supply.getFid());//供需id
			String headImage=supply.getYBasicMember().getFheadImage();
			String photoImg=null;
			if(null!=headImage){
				photoImg = headImage.replace(",", "");
				resultMap.put("userImageUrl",photoImg);//发布人头像
			}
			if(supply.getYBasicProvince()!=null){
				resultMap.put("provincialId", supply.getYBasicProvince().getFid());//省份id
			}
			if(supply.getYBasicCity()!=null){
				resultMap.put("cityId",supply.getYBasicCity().getFid());//城市id
			}
			if(supply.getYBasicCounty()!=null){
				resultMap.put("countryId",supply.getYBasicCounty().getFid());//县Id
			}
			if(supply.getYBasicTrade()!=null){
				resultMap.put("tradeId",supply.getYBasicTrade().getFid());//行业id
			}
			resultMap.put("contactsPhone",supply.getFtel());//联系号码
			Date expDateStop=supply.getFexpireTime();//结算时间
			Date expDateStart=supply.getFauditTime();//开始时间
			String stop=null;
			String start=null;
			if(null!=expDateStop){
				stop=new SimpleDateFormat("YYYY-MM-dd").format(expDateStop);
			}
			if(null!=expDateStart){
				start=new SimpleDateFormat("YYYY-MM-dd").format(expDateStart);
			}
			resultMap.put("expDateStop",stop);//停止日期
			resultMap.put("expDateStart",start);//开始日期
			resultMap.put("contactsPerson",supply.getFcontacts());//联系人
			resultMap.put("fTitle",supply.getFheadline());//供需title
			resultMap.put("fContent",FilterHtmlUtil.filterHtml(supply.getFmessage()==null?"":supply.getFmessage()));//供需类容
			resultMap.put("tradeName",supply.getYBasicTrade()==null?"":supply.getYBasicTrade().getFname());
			if(supply.getFimages()==null||supply.getFimages().equals("")){
				resultMap.put("fImageUrlArray",new String[0]);//需求图片
			}else{
				resultMap.put("fImageUrlArray",supply.getFimages().split(","));
			}
			resultMap.put("mid",supply.getYBasicMember().getFmobilePhone());//发布者Id
			resultMap.put("isWarrant",supply.getFareGuarantee()==1?0:1);//是否是商会担保
			String hql =null;
			hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='"+3003+"' and fpointLikeType=0";
			ypointlikeList =  (List<YPointlike>)dao.findAllByHQL(hql);
			if(ypointlikeList!=null&&ypointlikeList.size()>0){ //只要根据  类型和动态的id查找到数据 说明已经点赞
				resultMap.put("supportNum",ypointlikeList.size());//点赞数
			}else{
				resultMap.put("supportNum", 0);
			}
			//获取会员id
			if(flag == false){//是注册游客
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"'";
			}else{
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
			}
			List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
			if(listMember!=null&&listMember.size()>0){
				YBasicMember mem = listMember.get(0);
				hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='"+3003+"' and ypointlike.YBasicMember.fid='"+mem.getFid()+"'";
				mineypointlikeList =  (List<YPointlike>)dao.findAllByHQL(hql);
				if(mineypointlikeList!=null&&mineypointlikeList.size()>0&&(mineypointlikeList.get(0)).getFpointLikeType()==0){//根据当前用户来查询
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
			

			hql = "from YComment ycomment where ycomment.fforeignId='"+fid+"'";
			ycommentList =  (List<YComment>)dao.findAllByHQL(hql);
			if(ycommentList!=null&&ycommentList.size()>0){
				resultMap.put("reviewNum", ycommentList.size());//获取评论 总数
			}else{
				resultMap.put("reviewNum", 0);//如果不存在，评论数就是0
			}
			hql = "from YComplaint ycomplaint where ycomplaint.ftype=0 and ycomplaint.fsupplyDemandId='"+fid+"'";//查询需求的所有投诉信息
			ycomplaintList =  (List<YComplaint>)dao.findAllByHQL(hql);
			if(ycomplaintList!=null&&ycomplaintList.size()>0){
				resultMap.put("complainNum", ycomplaintList.size());//获取投诉总数
			}else{
				resultMap.put("complainNum", 0);//如果不存在，评论数就是0
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
			Map<String,Object> tempMap = new HashMap<String,Object>();//object是存的最大的warrantBusinessObject
			if(supply.getFareGuarantee()==1){//如果有担保就查询他的担保信息
				Set<YSupplyGroup> ysupplyGroupSet =null;
				ysupplyGroupSet = supply.getySupplyGroups();// 获取供应和担保的关系表
				if(ysupplyGroupSet!=null&&ysupplyGroupSet.size()>0){
					for(YSupplyGroup ysupplygroup:ysupplyGroupSet){
						YBasicAssurancecontent yAssurancecontent =ysupplygroup.getYBasicAssurancecontent();
						if(tempMap.containsKey(yAssurancecontent.getYBasicSocialgroups().getFid())){//如果这个商会已存在
							warrantTypeObject = new HashMap<String,String>();
							warrantTypeObject.put("warrantTypeVal", yAssurancecontent.getFid());
							warrantTypeObject.put("warrantTypeName", yAssurancecontent.getFcontent());
							Map<String,Object> map =(Map<String, Object>) tempMap.get(yAssurancecontent.getYBasicSocialgroups().getFid());
							List<Map<String,String>> list =(List<Map<String, String>>) map.get("warrantType");
							list.add(warrantTypeObject);
							warrantBusinessObject.put("warrantType", warrantTypeList);
						}else{//不存在就加进去
							warrantBusinessObject =new HashMap<String,Object>();
							warrantBusinessObject.put("businessId", yAssurancecontent.getYBasicSocialgroups().getFid());
							warrantBusinessObject.put("businessName", yAssurancecontent.getYBasicSocialgroups().getFname());
							warrantTypeObject = new HashMap<String,String>();
							warrantTypeObject.put("warrantTypeVal", yAssurancecontent.getFid());
							warrantTypeObject.put("warrantTypeName", yAssurancecontent.getFcontent());
							warrantTypeList.add(warrantTypeObject);
							warrantBusinessObject.put("warrantType", warrantTypeList);
							tempMap.put(yAssurancecontent.getYBasicSocialgroups().getFid(), warrantBusinessObject);
						}
					}
				}else{
					//   do nothing
				}
			}else{
				//do nothing   不去查询担保信息
			}

			resultMap.put("warrantBusiness", tempMap.values());
		}

		return resultMap;

	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "供应点赞")
	public void saveSupplyForSupport(String fid, String userId,int type,String businessId) {
		boolean flag = memberService.isMember(userId);//如果是注册游客就是false
		List<YPointlike> mineypointlikeList = null;
		String hql = null;
		String hqlMember = null;
		//获取会员id
		if(flag==false){//注册游客
			hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"'";
		}else{
			hqlMember = "from YBasicMember y where y.fmobilePhone = '"+userId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
		}
		List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
		YBasicMember mem =null;
		if(listMember!=null&&listMember.size()>0){
			mem = listMember.get(0);
		}
		hql= "from YPointlike ypointlike where ypointlike.fmanShowId='"+fid+"' and ypointlike.YBasicType.fid='"+3003+"' and ypointlike.YBasicMember.fid='"+mem.getFid()+"'";
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
			ypointlike.setYBasicType(new YBasicType("3003"));//模块类型
			ypointlike.setFpointLikeType(type);//点赞或者取消
			ypointlike.setFmanShowId(fid);
			ypointlike.setFpointLikeTime(new java.sql.Date(new java.util.Date().getTime()));
			dao.save(ypointlike);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "投诉供应")
	public void saveComplaint(String fId, String memberId,String businessId) {
		boolean flag = memberService.isMember(memberId);//如果是注册游客就是false
		String hqlMember = null;
		try {
			//获取会员id
			if(flag==false){
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+memberId+"'";
			}else{
				hqlMember = "from YBasicMember y where y.fmobilePhone = '"+memberId+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
			}
			List<YBasicMember> listMember = (List<YBasicMember>) dao.findAllByHQL(hqlMember);
			YBasicMember mem = null;
			if(listMember!=null&&listMember.size()>0){
				mem = listMember.get(0);
			}
			YComplaint complaint=new YComplaint();
			if(null==fId||"".equals(fId)){
				String hql="from YBasicSocialgroupssupply as y  Order by y.flag desc";
				List<YBasicSocialgroupssupply> supplys=(List<YBasicSocialgroupssupply>)dao.find(hql, 0, 1, null);
				YBasicSocialgroupssupply supply=supplys.get(0);
				complaint.setFsupplyDemandId(supply.getFid());
			}else{
				complaint.setFsupplyDemandId(fId);
			}
			complaint.setFtype(0);
			complaint.setComplaintId(mem.getFid());
			dao.save(complaint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly=true)
	@SystemServiceLog(description = "通过用户获取自己的商会和担保信息")
	public List<Map<String, Object>> getBusinessListAndEnsureType(String telphone) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultObject = null;
		List<Map<String,String>> warrantList = null;
		Map<String,String> warrantObject = null;

		String hql = "";

		hql="from YBasicMember ymember where ymember.fmobilePhone ='"+telphone+"'";
		List<YBasicMember> members = (List<YBasicMember>) dao.findAllByHQL(hql);
		if(members!=null&&members.size()>0){
			for(YBasicMember member:members){
				String logo  = null;
				 if(member.getYBasicSocialgroups()!=null){
					 logo = member.getYBasicSocialgroups().getLogo();
					 if(logo!=null&&!logo.equals("")){
						 logo = logo.replace(",", "");
					 }
				 }
				
				resultObject = new HashMap<String,Object>();
				warrantObject = new HashMap<String,String>();
				warrantList = new ArrayList<Map<String,String>>();
				if(logo!=null&&!logo.equals("")){
					logo=logo.replace(",", "");
				}else{
					logo = null;
				}
				resultObject.put("businessLogo",logo);
				if(member.getYBasicSocialgroups()!=null){
					resultObject.put("businessId",member.getYBasicSocialgroups().getFid());
					resultObject.put("businessName", member.getYBasicSocialgroups().getFname());
					Set<YBasicAssurancecontent> yBasicAssurancecontents = member.getYBasicSocialgroups().getYBasicAssurancecontents();
					if(yBasicAssurancecontents!=null&&yBasicAssurancecontents.size()>0){
						for(YBasicAssurancecontent yassurance:yBasicAssurancecontents){
							warrantObject.put("warrantId", yassurance.getFid());
							warrantObject.put("warrantName", yassurance.getFcontent());
							warrantList.add(warrantObject);
						}
					}
					resultObject.put("warrant", warrantList);
					resultList.add(resultObject);
				}
			}
		}
		return resultList;
	}
	/**
	 * update by ailierke
	 */
	@Override
	@SystemServiceLog(description = "根据id查询供应")
	public YBasicAssurancecontent getYBasicAssurancecontentById(String id) {

		return (YBasicAssurancecontent) dao.findById(YBasicAssurancecontent.class, id);
	}
	/**
	 * update by ailierke
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "查询并分页")
	public List<GuaranteeVo> getYBasicAssurancecontentVo(String start,String limit,String groupId,String ispass) {
		List<GuaranteeVo> guaranteeVoList =new ArrayList<GuaranteeVo>();
		GuaranteeVo guaranteeVo =null;
		StringBuffer hql =new StringBuffer("from YSupplyGroup supplygroup where supplygroup.groupid='"+groupId+"' ");
		if(ispass!=null&&!ispass.equals("")){
			hql.append(" and supplygroup.ispass='"+ispass+"'");
		}
		List<YSupplyGroup> ySupplyGroupList = (List<YSupplyGroup>) dao.find(hql.toString(), new Integer(start), new Integer(limit), null);
		if(ySupplyGroupList!=null&&ySupplyGroupList.size()>0){
			for(YSupplyGroup ysupplyGroup:ySupplyGroupList){
				guaranteeVo = new GuaranteeVo();
				guaranteeVo.setFid(ysupplyGroup.getFid());
				guaranteeVo.setIspass(ysupplyGroup.getIspass());
				guaranteeVo.setGroupid(ysupplyGroup.getGroupid());
				guaranteeVo.setYBasicCity(ysupplyGroup.getYBasicSocialgroupssupply().getYBasicCity());
				guaranteeVo.setYBasicCounty(ysupplyGroup.getYBasicSocialgroupssupply().getYBasicCounty());
				guaranteeVo.setYBasicTrade(ysupplyGroup.getYBasicSocialgroupssupply().getYBasicTrade());
				guaranteeVo.setYBasicProvince(ysupplyGroup.getYBasicSocialgroupssupply().getYBasicProvince());
				guaranteeVo.setFnumber(ysupplyGroup.getYBasicSocialgroupssupply().getFnumber());
				guaranteeVo.setFheadline(ysupplyGroup.getYBasicSocialgroupssupply().getFheadline());
				guaranteeVo.setFimages(ysupplyGroup.getYBasicSocialgroupssupply().getFimages());
				guaranteeVo.setFmessage(ysupplyGroup.getYBasicSocialgroupssupply().getFmessage());
				guaranteeVo.setFtel(ysupplyGroup.getYBasicSocialgroupssupply().getFtel());
				guaranteeVo.setFnationalCertification(ysupplyGroup.getYBasicSocialgroupssupply().getFnationalCertification());
				guaranteeVo.setFareGuarantee(ysupplyGroup.getYBasicSocialgroupssupply().getFareGuarantee());
				guaranteeVo.setFcontacts(ysupplyGroup.getYBasicSocialgroupssupply().getFcontacts());
				guaranteeVo.setFexpireTime(new SimpleDateFormat("yyyy-MM-dd").format(ysupplyGroup.getYBasicSocialgroupssupply().getFexpireTime())); 
				guaranteeVo.setFauditTime(new SimpleDateFormat("yyyy-MM-dd").format(ysupplyGroup.getYBasicSocialgroupssupply().getFauditTime()));
				guaranteeVo.setFpublisherTime(new SimpleDateFormat("yyyy-MM-dd").format(ysupplyGroup.getYBasicSocialgroupssupply().getFpublisherTime()));
				guaranteeVo.setFauditIdea(ysupplyGroup.getYBasicSocialgroupssupply().getFauditIdea());
				guaranteeVo.setFbillState(ysupplyGroup.getYBasicSocialgroupssupply().getFbillState());
				guaranteeVo.setFcomment(ysupplyGroup.getYBasicSocialgroupssupply().getFcomment());
				guaranteeVo.setFlag(ysupplyGroup.getYBasicSocialgroupssupply().getFlag());
				guaranteeVo.setFisHide(ysupplyGroup.getYBasicSocialgroupssupply().getFisHide()); 
				guaranteeVo.setYBasicMember(ysupplyGroup.getYBasicSocialgroupssupply().getYBasicMember()); 
				guaranteeVo.setFlevel(ysupplyGroup.getYBasicSocialgroupssupply().getFlevel());
				guaranteeVo.setContent(ysupplyGroup.getYBasicAssurancecontent().getFcontent());
				guaranteeVoList.add(guaranteeVo);
			}
		}
		return guaranteeVoList;
	}
	
	@Override
	@SystemServiceLog(description = "根据团体id获取条数")
	public Integer getCount(String start,String limit,String groupId,String ispass) {
		Integer count = null;
		StringBuffer hql =new StringBuffer("select count(0) from YSupplyGroup supplygroup where supplygroup.groupid='"+groupId+"' ");
		if(ispass!=null&&!ispass.equals("")){
			hql.append(" and supplygroup.ispass='"+ispass+"'");
		}
		count = dao.getTotalCountByCondition(hql.toString(), new Integer(start), new Integer(limit), null);
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(description = "对自己需要做担保的进行通过或者拒绝")
	public void updateYBasicAssurancecontent(String supplygroupId,String ispass) throws NumberFormatException, Exception {
		YSupplyGroup ySupplyGroup = (YSupplyGroup) dao.findById(YSupplyGroup.class, supplygroupId);
		if(ySupplyGroup!=null){
			ySupplyGroup.setIspass(ispass);
			dao.update(ySupplyGroup);
			if(ispass.equals("17")){//认证被拒绝，推送给用户
				String username = ySupplyGroup.getYBasicSocialgroupssupply().getYBasicMember().getFmobilePhone();
				List<YAppdevice> memberAppDeviceList = (List<YAppdevice>) dao.findAllByHQL("from  YAppdevice device where device.fuserName ='"+username+"'");
				//如果存在此用户的clientId信息就推送
				if(memberAppDeviceList!=null&&memberAppDeviceList.size()>0){
					YBasicSocialgroups groups = (YBasicSocialgroups) dao.findById(YBasicSocialgroups.class, ySupplyGroup.getGroupid());
					String logo ="";
					String logoUrl="";
					String transmissionContent="";
					String headLine = ySupplyGroup.getYBasicSocialgroupssupply().getFheadline();
					//如果大于10个汉字就截取
					if(headLine.getBytes().length>20){
						headLine = CheckBytes.substring(headLine, 20)+"...";
					}
				
					String text = "供应【"+headLine+"】未通过【"+groups.getFname()+"】审核";
					int transmissionType =1;
					YSystemconfigurationMessage message = new YSystemconfigurationMessage();
					message.setFmessageType("3003");
					message.setFcontent(headLine);
					dao.saveOrUpdate(message);//推送消息表
					YSystemconfigurationMessagerecord messageRecord = null;
					List<YSystemconfigurationMessagerecord> messagerecords = new ArrayList<YSystemconfigurationMessagerecord>();
					
					for(YAppdevice yAppdevice:memberAppDeviceList){
						//使用激活应用模板
						PushToListMessage.sendDownLoadMessageToSingel(yAppdevice.getFdeviceId(), new Integer(yAppdevice.getFappChannelNo()), TemplateType.NOTIFY_TEMPLATE.toString(), "云筑", text, logoUrl, logo, transmissionContent, transmissionType, null, null, null, null, null, null, null);
						messageRecord = new YSystemconfigurationMessagerecord();
						messageRecord.setFuserId(yAppdevice.getFuserName());
						messageRecord.setYSystemconfigurationMessage(new YSystemconfigurationMessage(message.getFid()));
						messageRecord.setFdate(new java.sql.Date(new java.util.Date().getTime()));
						messagerecords.add(messageRecord);
					}
					dao.saveOrUpdateAll(messagerecords);//推送消息记录表
				}
			}
		}else{
			throw new RuntimeException("对象不存在");
		}
	}

	@Override
	public Map<String, Object> getNumById(String supplyDemandId) {
		Map<String,Object> map=new HashMap<String, Object>();
		Integer Praisenum=0;
		Integer Treadnum=0;
		try {
			List<YPointlike> list=new ArrayList<YPointlike>();
			list=(List<YPointlike>)dao.findAllByHQL("from YPointlike as y where y.fmanShowId='"+supplyDemandId+"' and y.farePointLike=0  and y.fpointLikeType=0");
			Praisenum=list.size();
			List<YPointlike> Treadlist=new ArrayList<YPointlike>();
			Treadlist=(List<YPointlike>)dao.findAllByHQL("from YPointlike as y where y.fmanShowId='"+supplyDemandId+"' and y.farePointLike=0  and y.fpointLikeType!=0");
			Treadnum=Treadlist.size();
			map.put("Praisenum", Praisenum);
			map.put("Treadnum", Treadnum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void commentScore(String fId,Integer supplyDemandType, Integer gradeNum, String userId) {
		String hql =null;
		Integer score =0;
		YBasicSocialgroupssupply supply = null;
		YBasicSocialgroupsdemand demand  =null;
		if(supplyDemandType==0){
			hql = "from YBasicSocialgroupssupply supply where supply.fid='"+fId+"'";
			List<YBasicSocialgroupssupply> supplyList =  (List<YBasicSocialgroupssupply>)dao.findAllByHQL(hql);
			if(null!=supplyList&&supplyList.size()>0){
			     supply = supplyList.get(0);
				score = supply.getFlevel();
			}else{
				throw new RuntimeException("系统传参异常....");
			}
			
		}else{
			hql = "from YBasicSocialgroupsdemand demand where demand.fid='"+fId+"'";
			List<YBasicSocialgroupsdemand> demandList =  (List<YBasicSocialgroupsdemand>)dao.findAllByHQL(hql);
			if(demandList!=null&&demandList.size()>0){
				demand =  demandList.get(0);
				score =demand.getFlevel();
			}else{
				throw new RuntimeException("系统传参异常....");
			}
		}
		if(score==null){
			score=3;//主要针对以前的老生产数据没有数据的情况
		}
		/**
		 * 看是此记录的评分条数
		 */
		hql = "select count(*) from YCommentScore ycommentscore where  ycommentscore.demandOrSupplyId='"+fId+"' and ycommentscore.type="+supplyDemandType;//所有评分的总条数
		Integer count =  (int)dao.getTotalCountByCondition(hql, null, null, null);
		
		count =count+2;//加上后台一个人评分+现在新增的这个人，总人数
		
		hql = "from YCommentScore ycommentscore where  ycommentscore.demandOrSupplyId='"+fId+"' and ycommentscore.type="+supplyDemandType;//所有评分
		List<YCommentScore> ycommentScoreList =(List<YCommentScore>)dao.find(hql, null, null, null);
		if(ycommentScoreList!=null&&ycommentScoreList.size()>0){
			for(YCommentScore commentScore :ycommentScoreList){
				if(commentScore.getScore()!=null){
					score = score+new Integer(commentScore.getScore());//计算已经评论总分数+后台评论
				}else{
					throw new RuntimeException("系统参数异常....");//打分传来数据库保存的是不是数字
				}
			}
		}
		
		/**
		 * 评分总分/总人数--->平均分
		 */
		score = (int) Math.ceil((double)(score+gradeNum)/count); 
		if(supplyDemandType==0){
			supply.setfRank(score);
			dao.saveOrUpdate(supply);
		}else{
			demand.setfRank(score);
			dao.saveOrUpdate(demand);
		}
		dao.flush();//同步到数据库
		/**
		 * 查看是否评分
		 */
		hql = "from YCommentScore ycommentscore where ycommentscore.tel='"+userId+"' and ycommentscore.demandOrSupplyId='"+fId+"' and ycommentscore.type="+supplyDemandType;//是否已经评分
		List<YCommentScore> commentScoreList =  (List<YCommentScore>)dao.findAllByHQL(hql);
		YCommentScore ycommentScore =null;
		if(null!=commentScoreList&&commentScoreList.size()>0){
			/**
			 * 如果已经存在就修改
			 */
			ycommentScore = commentScoreList.get(0);
			ycommentScore.setScore(gradeNum.toString());
			ycommentScore.setUpdateTime(new java.sql.Date(new java.util.Date().getTime()));
		}else{
			/**
			 * 不存在就添加评分
			 */
			ycommentScore = new YCommentScore();
			ycommentScore.setDemandOrSupplyId(fId);
			ycommentScore.setScore(gradeNum.toString());
			ycommentScore.setTel(userId);
			ycommentScore.setType(supplyDemandType);
			ycommentScore.setUpdateTime(new java.sql.Date(new java.util.Date().getTime()));
		}
		dao.saveOrUpdate(ycommentScore);
		dao.flush();//同步到数据库
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getsupplyAnddemandByType(String userId, Integer pageSize,String searchName, Integer supplyDemandType,String fid) {
		String hql =null;
		Map<String, Object> mapObject= null ;
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		if(userId!=null){//查询某个人的商会供应需求
			returnList = getSupplyAndDemadByView(userId, searchName, pageSize,fid,supplyDemandType);
		}else if(userId==null&&supplyDemandType!=null){//根据供应需求类型来查
			String date = null;
			if(supplyDemandType==0){
				List<YBasicSocialgroupssupply> yBasicSocialgroupssupplyList = null;
				if(!fid.equals("")){
					hql = "from YBasicSocialgroupssupply supply where supply.fid = '"+fid+"'";
					yBasicSocialgroupssupplyList = (List<YBasicSocialgroupssupply>)dao.findAllByHQL(hql);
					if(yBasicSocialgroupssupplyList.size()>0){
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yBasicSocialgroupssupplyList.get(0).getFpublisherTime());
						hql = "from YBasicSocialgroupssupply supply where supply.fheadline like '%"+searchName+"%' and supply.fpublisherTime<'"+date+"' order by fpublisherTime desc";
					}else{
						throw new RuntimeException("系统参数异常....");
					}
				}else{
					hql = "from YBasicSocialgroupssupply supply where supply.fheadline like '%"+searchName+"%' order by fpublisherTime desc";
				}
				 yBasicSocialgroupssupplyList =  (List<YBasicSocialgroupssupply>)dao.find(hql, 0, pageSize, null);
				if(null!=yBasicSocialgroupssupplyList&&yBasicSocialgroupssupplyList.size()>0){
					String imgs = null;
					String[] imgSrray = null;
					for(YBasicSocialgroupssupply supply:yBasicSocialgroupssupplyList){
						mapObject = new HashMap<String, Object>();
						imgs = supply.getFimages();
						if(imgs!=null&&!"".equals(imgs)){
							imgSrray = imgs.split(",");
						}
						mapObject.put("fImageUrlArray",imgSrray);
						mapObject.put("fId", supply.getFid());
						mapObject.put("fTitle", supply.getFheadline());
						mapObject.put("fType", 0);
						if(supply.getFpublisherTime()!=null){
							mapObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(supply.getFpublisherTime()));
						}
						mapObject.put("fRank", supply.getFlevel());
						mapObject.put("isWarrant", supply.getFareGuarantee());
						mapObject.put("fcontent",  FilterHtmlUtil.filterHtml(supply.getFmessage()));
						returnList.add(mapObject);
					}
				}else{
					//returnList.add(new HashMap<String, Object>());//返回空的
				}
			}else if(supplyDemandType==1){
				List<YBasicSocialgroupsdemand> yBasicSocialgroupsdemandList = null;
				if(!fid.equals("")){
					hql = "from YBasicSocialgroupsdemand demand where demand.fid = '"+fid+"'";
					yBasicSocialgroupsdemandList = (List<YBasicSocialgroupsdemand>)dao.findAllByHQL(hql);
					if(yBasicSocialgroupsdemandList.size()>0){
						date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yBasicSocialgroupsdemandList.get(0).getFpublisherTime());
						hql = "from YBasicSocialgroupsdemand demand where demand.fheadline like '%"+searchName+"%' and demand.fpublisherTime<'"+date+"' order by fpublisherTime desc";
					}else{
						throw new RuntimeException("系统参数异常....");
					}
				}else{
					hql = "from YBasicSocialgroupsdemand demand where demand.fheadline like '%"+searchName+"%' order by fpublisherTime desc";
				}
				yBasicSocialgroupsdemandList =  (List<YBasicSocialgroupsdemand>)dao.find(hql, 0, pageSize, null);
				if(null!=yBasicSocialgroupsdemandList&&yBasicSocialgroupsdemandList.size()>0){
					String imgs = null;
					String[] imgSrray = null;
					for(YBasicSocialgroupsdemand demand:yBasicSocialgroupsdemandList){
						mapObject = new HashMap<String, Object>();
						imgs = demand.getFimages();
						if(imgs!=null&&!"".equals(imgs)){
							imgSrray = imgs.split(",");
						}
						mapObject.put("fImageUrlArray",imgSrray);
						mapObject.put("fId", demand.getFid());
						mapObject.put("fTitle", demand.getFheadline());
						mapObject.put("fType", 1);
						if(demand.getFpublisherTime()!=null){
							mapObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demand.getFpublisherTime()));
						}
						mapObject.put("fRank", demand.getFlevel());
						mapObject.put("isWarrant", 0);//未担保
						mapObject.put("fcontent",  FilterHtmlUtil.filterHtml(demand.getFmessage()));
						returnList.add(mapObject);
					}
				}else{
					//returnList.add(new HashMap<String, Object>());//返回空的
				}
			}else if(supplyDemandType==2){//查询试图
				returnList = getSupplyAndDemadByView(null, searchName, pageSize,fid,supplyDemandType);
			}
		}else if(userId==null&&supplyDemandType==null){//查询视图
			returnList = getSupplyAndDemadByView(null, searchName, pageSize,fid,supplyDemandType);
		}	
		return returnList;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> getSupplyAndDemadByView(String userId,String searchName,int pageSize,String fid,Integer supplyDemandType){
		String hql =null;
		Map<String, Object> mapObject= null ;
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		List<DemandAndSupply> supplyAnddemandList = null;
		String date =null;
		if(userId==null){//查询视图
			if(!fid.equals("")){
				hql = "from DemandAndSupply supplydemand where supplydemand.fid = '"+fid+"'";
				supplyAnddemandList = (List<DemandAndSupply>)dao.findAllByHQL(hql);
				if(supplyAnddemandList.size()>0){
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(supplyAnddemandList.get(0).getFpublisherTime());
						hql = "from DemandAndSupply demandAndsupply where  fheadline like '%"+searchName+"%' and fheadline like '%"+searchName+"%' and demandAndsupply.fpublisherTime<'"+date+"' order by fpublisherTime desc";
				}else{
					throw new RuntimeException("系统参数异常....");
				}
			}else{
				hql = "from DemandAndSupply demandAndsupply where  fheadline like '%"+searchName+"%' and fheadline like '%"+searchName+"%'  order by fpublisherTime desc";
			}
					List<DemandAndSupply> DemandAndSupplyList =  (List<DemandAndSupply>)dao.find(hql, 0, pageSize, null);
					if(null!=DemandAndSupplyList&&DemandAndSupplyList.size()>0){
						String imgs = null;
						String[] imgSrray = null;
						for(DemandAndSupply demandAndSupply:DemandAndSupplyList){
							mapObject = new HashMap<String, Object>();
							imgs = demandAndSupply.getFimages();
							if(imgs!=null&&!"".equals(imgs)){
								imgSrray = imgs.split(",");
							}
							mapObject.put("fImageUrlArray",imgSrray);
							mapObject.put("fId", demandAndSupply.getFid());
							mapObject.put("fTitle", demandAndSupply.getFheadline());
							mapObject.put("fType", demandAndSupply.getFtype());
							if(demandAndSupply.getFpublisherTime()!=null){
								mapObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demandAndSupply.getFpublisherTime()));
							}
							mapObject.put("fRank", demandAndSupply.getFlevel());
							mapObject.put("isWarrant", demandAndSupply.getFareGuarantee());
							mapObject.put("fcontent", FilterHtmlUtil.filterHtml(demandAndSupply.getFmessage()));//过滤html标签
							returnList.add(mapObject);
						}
				}else{
					//returnList.add(new HashMap<String, Object>());//返回空的
				}
			
		}else if(userId!=null){//查询个人供需
			if(supplyDemandType!=null){
				if(supplyDemandType==0){//查询用户供应
					List<YBasicSocialgroupssupply> yBasicSocialgroupssupplyList = null;
					if(!fid.equals("")){
						hql = "from YBasicSocialgroupssupply supply where supply.fid = '"+fid+"'";
						yBasicSocialgroupssupplyList = (List<YBasicSocialgroupssupply>)dao.findAllByHQL(hql);
						if(yBasicSocialgroupssupplyList.size()>0){
							date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yBasicSocialgroupssupplyList.get(0).getFpublisherTime());
							hql = "from YBasicSocialgroupssupply supply where supply.fheadline like '%"+searchName+"%' and supply.YBasicMember.fmobilePhone='"+userId+"' and supply.fpublisherTime<'"+date+"' order by fpublisherTime desc";
						}else{
							throw new RuntimeException("系统参数异常....");
						}
					}else{
						hql = "from YBasicSocialgroupssupply supply where supply.fheadline like '%"+searchName+"%' and supply.YBasicMember.fmobilePhone='"+userId+"' order by fpublisherTime desc";
					}
					 yBasicSocialgroupssupplyList =  (List<YBasicSocialgroupssupply>)dao.find(hql, 0, pageSize, null);
					if(null!=yBasicSocialgroupssupplyList&&yBasicSocialgroupssupplyList.size()>0){
						String imgs = null;
						String[] imgSrray = null;
						for(YBasicSocialgroupssupply supply:yBasicSocialgroupssupplyList){
							mapObject = new HashMap<String, Object>();
							imgs = supply.getFimages();
							if(imgs!=null&&!"".equals(imgs)){
								imgSrray = imgs.split(",");
							}
							mapObject.put("fImageUrlArray",imgSrray);
							mapObject.put("fId", supply.getFid());
							mapObject.put("fTitle", supply.getFheadline());
							mapObject.put("fType", 0);
							if(supply.getFpublisherTime()!=null){
								mapObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(supply.getFpublisherTime()));
							}
							mapObject.put("fRank", supply.getFlevel());
							mapObject.put("isWarrant", supply.getFareGuarantee());
							mapObject.put("fcontent",  FilterHtmlUtil.filterHtml(supply.getFmessage()));
							returnList.add(mapObject);
						}
					}else{
						//returnList.add(new HashMap<String, Object>());//返回空的
					}
				}else if(supplyDemandType==1){//查询用户需求
					List<YBasicSocialgroupsdemand> yBasicSocialgroupsdemandList = null;
					if(!fid.equals("")){
						hql = "from YBasicSocialgroupsdemand demand where demand.fid = '"+fid+"'";
						yBasicSocialgroupsdemandList = (List<YBasicSocialgroupsdemand>)dao.findAllByHQL(hql);
						if(yBasicSocialgroupsdemandList.size()>0){
							date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yBasicSocialgroupsdemandList.get(0).getFpublisherTime());
							hql = "from YBasicSocialgroupsdemand demand where demand.fheadline like '%"+searchName+"%'  and demand.YBasicMember.fmobilePhone='"+userId+"' and demand.fpublisherTime<'"+date+"' order by fpublisherTime desc";
						}else{
							throw new RuntimeException("系统参数异常....");
						}
					}else{
						hql = "from YBasicSocialgroupsdemand demand where demand.fheadline like '%"+searchName+"%'  and demand.YBasicMember.fmobilePhone='"+userId+"' order by fpublisherTime desc";
					}
					yBasicSocialgroupsdemandList =  (List<YBasicSocialgroupsdemand>)dao.find(hql, 0, pageSize, null);
					if(null!=yBasicSocialgroupsdemandList&&yBasicSocialgroupsdemandList.size()>0){
						String imgs = null;
						String[] imgSrray = null;
						for(YBasicSocialgroupsdemand demand:yBasicSocialgroupsdemandList){
							mapObject = new HashMap<String, Object>();
							imgs = demand.getFimages();
							if(imgs!=null&&!"".equals(imgs)){
								imgSrray = imgs.split(",");
							}
							mapObject.put("fImageUrlArray",imgSrray);
							mapObject.put("fId", demand.getFid());
							mapObject.put("fTitle", demand.getFheadline());
							mapObject.put("fType", 1);
							if(demand.getFpublisherTime()!=null){
								mapObject.put("fDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(demand.getFpublisherTime()));
							}
							mapObject.put("fRank", demand.getFlevel());
							mapObject.put("isWarrant", 0);//未担保
							mapObject.put("fcontent",  FilterHtmlUtil.filterHtml(demand.getFmessage()));
							returnList.add(mapObject);
						}
					}else{
						//returnList.add(new HashMap<String, Object>());//返回空的
					}
				}else{
					//donothing
				}
			}else{
				throw new RuntimeException("系统参数异常.....");
			}
		}
		return returnList;
  }
//	public static void main(String[] args) {
//		int score = (int) Math.ceil((double)(3+4)/2);
//		System.out.println(score);
//		
//	}

	@Override
	public PagingList<YBasicSocialgroupssupply> findSupplyByComment(
			Integer start, Integer limit) {
		PagingList<YBasicSocialgroupssupply> pagingList = new PagingList<YBasicSocialgroupssupply>();
		try {
			String sql="select y.FID as fid,y.FNumber as fnumber,y.FHeadline as fheadline,y.Fimages as fimages,y.FMessage as fmessage,"
			+ "y.FTel as ftel,y.FNationalCertification as fnationalCertification,y.FAreGuarantee as fareGuarantee,"
			+ "y.FContacts as fcontacts,y.FExpireTime as fexpireTime,y.FAuditTime as fauditTime,y.FPublisherTime as fpublisherTime,"
			+ "y.FAuditIdea as fauditIdea,y.FBillState as fbillState,y.FComment as fcomment,y.FLag as flag,y.FIsHide as fisHide,"
			+ "y.Flevel as flevel,y.jsonStr as jsonStr,y.FRank as fRank from y_basic_socialgroupssupply y left JOIN (select FForeignID, count(*) as num from y_comment  GROUP BY FForeignID) c on y.fid=c.FForeignID order by c.num desc";
			List<YBasicSocialgroupssupply> list=(List<YBasicSocialgroupssupply>)dao.getListBySqlVO(sql,YBasicSocialgroupssupply.class);
			pagingList.setCount(list.size());
			if(null!=start&&null!=limit){
				sql=sql+"  limit "+start+","+limit;
				list=(List<YBasicSocialgroupssupply>)dao.getListBySqlVO(sql,YBasicSocialgroupssupply.class);
			}
			
			pagingList.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagingList;
	}

	@Override
	public PagingList<YBasicSocialgroupssupply> findSupplyByPointLike(
			Integer start, Integer limit) {
		PagingList<YBasicSocialgroupssupply> pagingList = new PagingList<YBasicSocialgroupssupply>();
		try {
			String sql="select y.FID as fid,y.FNumber as fnumber,y.FHeadline as fheadline,y.Fimages as fimages,y.FMessage as fmessage,"
			+ "y.FTel as ftel,y.FNationalCertification as fnationalCertification,y.FAreGuarantee as fareGuarantee,"
			+ "y.FContacts as fcontacts,y.FExpireTime as fexpireTime,y.FAuditTime as fauditTime,y.FPublisherTime as fpublisherTime,"
			+ "y.FAuditIdea as fauditIdea,y.FBillState as fbillState,y.FComment as fcomment,y.FLag as flag,y.FIsHide as fisHide,"
			+ "y.Flevel as flevel,y.jsonStr as jsonStr,y.FRank as fRank from y_basic_socialgroupssupply y left JOIN (select FManShowID,count(*) as num from y_pointlike  where FPointLikeType='0' GROUP BY FManShowID) c on y.fid=c.FManShowID order by c.num desc";
			List<YBasicSocialgroupssupply> list=(List<YBasicSocialgroupssupply>)dao.getListBySqlVO(sql,YBasicSocialgroupssupply.class);
			pagingList.setCount(list.size());
			if(null!=start&&null!=limit){
				sql=sql+"  limit "+start+","+limit;
				list=(List<YBasicSocialgroupssupply>)dao.getListBySqlVO(sql,YBasicSocialgroupssupply.class);
			}
			
			pagingList.setList(list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagingList;
	}

	@Override
	public PagingList<YBasicSocialgroupssupply> findSupplyBytime(Integer start,
			Integer limit) {
		logger.info("查询信息列表，并分页....");
		PagingList<YBasicSocialgroupssupply> pagingList = new PagingList<YBasicSocialgroupssupply>();
		try {
			StringBuffer hqlList = new StringBuffer("from YBasicSocialgroupssupply y  order by y.fpublisherTime desc "); 
			StringBuffer hql = new StringBuffer("select count(*) from YBasicSocialgroupssupply y  order by y.fpublisherTime desc "); 
			/**
			 * 获得此页数据
			 */
			pagingList.setList((List<YBasicSocialgroupssupply>) dao.find(hqlList.toString(), start, limit, null));
			pagingList.setCount(dao.getTotalCountByCondition(hql.toString(), start, limit,null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pagingList;
	}
	/**
	 * 供需点赞
	 */
	@Override
	public void savepointlike(YPointlike pointlike) {
		try{
			List<YPointlike> list=(List<YPointlike>)dao.findAllByHQL("from YPointlike as y where y.YBasicMember.fid='"+pointlike.getYBasicMember().getFid()+"' and y.fmanShowId='"+pointlike.getFmanShowId()+"'");
			if(list.size()>0){
				for(YPointlike point:list){
					if(point.getFpointLikeType()==1){
						point.setFpointLikeType(1);
						dao.update(point);
					}
				}
			}else{
				dao.save(pointlike);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
