package com.yunzo.cocmore.core.function.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yunzo.cocmore.core.dao.hibernate.COC_HibernateDAO;
import com.yunzo.cocmore.core.function.aop.aspect.SystemServiceLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsdemand;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YImage;
import com.yunzo.cocmore.core.function.model.mysql.YManshowinformation;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.service.ManshowinformationService;
import com.yunzo.cocmore.core.function.util.PagingList;

/**
 * 个人秀接口实现类
 * @author yunzo
 *
 */
@Service("ManshowinformationService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class ManshowinformationServiceImpl implements ManshowinformationService {

private static final Logger logger = Logger.getLogger(ManshowinformationServiceImpl.class);
	
	@Resource(name = "hibernateDAO")
	private COC_HibernateDAO dao;

	@Override
	@SystemServiceLog(description = "新增个人秀")
	public void save(YManshowinformation manshow) {
		logger.info("ManshowinformationService SaveYManshowinformation");
		dao.save(manshow);
	}

	@Override
	@SystemServiceLog(description = "修改个人秀")
	public void update(YManshowinformation manshow) {
		logger.info("ManshowinformationService UpdateYManshowinformation");
		dao.update(manshow);
	}

	@Override
	@SystemServiceLog(description = "删除个人秀")
	public void delete(YManshowinformation manshow) {
		logger.info("ManshowinformationService DeleteYManshowinformation");
		dao.delete(manshow);
	}

	@Override
	@SystemServiceLog(description = "新增个人秀图片")
	public void saveImage(YImage image) {
		logger.info("ManshowinformationService saveYImage");
		dao.save(image);
	}

	@Override
	@SystemServiceLog(description = "修改个人秀图片")
	public void updateImage(YImage image) {
		logger.info("ManshowinformationService updateYImage");
		dao.update(image);
	}

	@Override
	@SystemServiceLog(description = "根据id查询个人秀")
	public YManshowinformation getById(String fid) {
		logger.info("ManshowinformationService getByIdYManshowinformation");
		return (YManshowinformation)dao.findById(YManshowinformation.class, fid);
	}
	
	@Override
	@SystemServiceLog(description = "根据个人秀id查询个人秀图片")
	public List<YImage> getByshowId(String showId){
		List<YImage> list=new ArrayList<YImage>();
		list=(List<YImage>)dao.findAllByHQL("from YImage as y where y.YManshowinformation.fid='"+showId+"'");
		return list;
	}

	@Override
	@SystemServiceLog(description = "获取我的个人秀列表")
	public List<Map<String, Object>> findMyManList(String memberId,
			Integer Size, String fid) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<YManshowinformation> manshows=new ArrayList<YManshowinformation>();
		
		try {
			String hql=null;
			YBasicMember member=new YBasicMember();
			member=(YBasicMember)dao.findById(YBasicMember.class, memberId);
			if(null==fid||"".equals(fid)){
				hql="from YManshowinformation as y where y.YBasicMember.fid='"+member.getFid()+"' order by y.flag desc";
				manshows=(List<YManshowinformation>)dao.find(hql, 0, Size, null);
			}else{
				YManshowinformation manshow=(YManshowinformation)dao.findById(YManshowinformation.class, fid);
				hql="from YManshowinformation as y where y.YBasicMember.fid='"+member.getFid()+"' and  y.flag<"+manshow.getFlag()+"  order by y.flag desc";
				manshows=(List<YManshowinformation>)dao.find(hql, 0, Size, null);
			}
			
			for(YManshowinformation show:manshows){
				Map<String,Object> resultObject = new HashMap<String, Object>();
				resultObject.put("fId", show.getFid());//				fId	        	String	个人秀ID
				resultObject.put("fContent", show.getFcontents());//			fContent		String	个人秀文本内容
				resultObject.put("userNiceName", show.getYBasicMember().getFnickName());//			userNiceName	String	用户昵称
				resultObject.put("userImageUrl", show.getYBasicMember().getFheadImage());//			userImageUrl	String	用户头像地址
				resultObject.put("mid",show.getFid());//			mid				String	用户ID
				Integer reviewNum=0;
				List<YComment> comments=(List<YComment>)dao.findAllByHQL("from YComment as c where c.fforeignId='"+show.getFid()+"' and c.YBasicType.fid='0123456789'");
				reviewNum=comments.size();
				
				resultObject.put("reviewNum", reviewNum);//			reviewNum		String	回复数 //评论数
				resultObject.put("dynamicDate",new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(show.getFpublished()));//			dynamicDate		Int	动态发布日期(yyyy-MM-dd hh:mm:ss)
				List<YPointlike> YPointLikes=(List<YPointlike>)dao.findAllByHQL("from YPointlike as p where p.YBasicType.fid='0123456789' and p.fmanShowId ='"+show.getFid()+"'");
				Integer supportNum=0;
				supportNum=YPointLikes.size(); 
				resultObject.put("supportNum", supportNum);//			supportNum		Int	点赞数
				for(YPointlike p:YPointLikes){
					Map<String ,Object> members=new HashMap<String, Object>();
					if(p.getYBasicMember().getFid().equals(member.getFid())){
						resultObject.put("isSupport", p.getFarePointLike());   //			isSupport		Int	是否点赞(0已点，1未点)
					}
					members.put("mid", p.getYBasicMember().getFid()); //			fSupport(json数组)	mid	String	用户ID
					members.put("userNiceName", p.getYBasicMember().getFnickName());//				userNiceName	String	用户昵称
					members.put("userImageUrl", p.getYBasicMember().getFheadImage());//				userImageUrl	String	用户头像地址
					
					resultObject.put("fSupport",members);//			fSupport(json数组)
				}
				
				List<YImage> images=new ArrayList();
				images=(List<YImage>)dao.findAllByHQL("from YImage as y where y.YManshowinformation.fid='"+show.getFid()+"'");
				for(YImage img:images){
					Map<String ,Object> imgs=new HashMap<String, Object>();
					imgs.put("thumbnailImageUrl", img.getFaddress()); //			fImages(json数组)	thumbnailImageUrl	String	缩略图地址
					imgs.put("imageUrl", img.getFaddress()); //							imageUrl	String	大图地址
					resultObject.put("fImages", imgs);
				}
				resultList.add(resultObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	public List<String> getByMemberId(String memberId){
		List<String> list=new ArrayList<String>();
		YBasicMember member=new YBasicMember();
		member=(YBasicMember)dao.findById(YBasicMember.class, memberId);
		List<YBasicMember> memberlist=new ArrayList<YBasicMember>();
		memberlist=(List<YBasicMember>)dao.findAllByHQL("from YBasicMember y where y.fmobilePhone='"+member.getFmobilePhone()+"'");
		for(YBasicMember m:memberlist){
			list.add(m.getYBasicSocialgroups().getFid());
		}
		return list;
	}
	@Override
	@SystemServiceLog(description = "获取个人秀列表")
	public List<Map<String, Object>> findManList(String memberId, Integer Size,
			String fid) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<YManshowinformation> manshows=new ArrayList<YManshowinformation>();
		List<YManshowinformation> shows=new ArrayList<YManshowinformation>();
		
		try {
			String hql=null;
			YBasicMember member=new YBasicMember();
			member=(YBasicMember)dao.findById(YBasicMember.class, memberId);
			List<String> mygroupId=getByMemberId(memberId);
			
			if(null==fid||"".equals(fid)){
				hql="from YManshowinformation as y  order by y.flag desc";
				manshows=(List<YManshowinformation>)dao.find(hql);
			}else{
				YManshowinformation manshow=(YManshowinformation)dao.findById(YManshowinformation.class, fid);
				hql="from YManshowinformation as y where y.flag<"+manshow.getFlag()+"  order by y.flag desc";
				manshows=(List<YManshowinformation>)dao.find(hql);
			}
			//获取符合条件的个人秀list
			for(YManshowinformation s:manshows){
				if(s.getFisOpen()==0){
					shows.add(s);
				}else{
					List<String> showgroupId=getByMemberId(s.getYBasicMember().getFid());
					a:for(String mg:mygroupId){
						b:for(String sg:showgroupId){
							if(mg.equals(sg)){//判断是否有相同的团体
								shows.add(s);
								break a;
							}
						}
					}	
				}
				if(shows.size()==Size){
					break;
				}
			}
			for(YManshowinformation show:shows){
				Map<String,Object> resultObject = new HashMap<String, Object>();
				resultObject.put("fId", show.getFid());//				fId	        	String	个人秀ID
				resultObject.put("fContent", show.getFcontents());//			fContent		String	个人秀文本内容
				resultObject.put("userNiceName", show.getYBasicMember().getFname());//			userNiceName	String	用户昵称
				String headimg[]= show.getYBasicMember().getFheadImage().split(",");
				resultObject.put("userImageUrl", headimg[0]);//			userImageUrl	String	用户头像地址
				resultObject.put("mid",show.getFid());//			mid				String	用户ID
				Integer reviewNum=0;
				List<YComment> comments=(List<YComment>)dao.findAllByHQL("from YComment as c where c.fforeignId='"+show.getFid()+"' and c.YBasicType.fid='0123456789' order by c.flag");
				reviewNum=comments.size();
				
				resultObject.put("reviewNum", reviewNum);//			reviewNum		String	回复数 //评论数
				resultObject.put("dynamicDate",new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(show.getFpublished()));//			dynamicDate		Int	动态发布日期(yyyy-MM-dd hh:mm:ss)
				List<YPointlike> YPointLikes=(List<YPointlike>)dao.findAllByHQL("from YPointlike as p where p.YBasicType.fid='0123456789' and p.fmanShowId ='"+show.getFid()+"'");
				Integer supportNum=0;
				supportNum=YPointLikes.size(); 
				resultObject.put("supportNum", supportNum);//			supportNum		Int	点赞数
				List<Map<String , Object>> memberlist=new ArrayList<Map<String,Object>>();
				if(YPointLikes.size()==0){
					resultObject.put("isSupport", 1);   //			isSupport		Int	是否点赞(0已点，1未点)
				}else{
					for(YPointlike p:YPointLikes){
						Map<String ,Object> members=new HashMap<String, Object>();
						if(p.getYBasicMember().getFid().equals(member.getFid())){
							resultObject.put("isSupport", p.getFarePointLike());   //			isSupport		Int	是否点赞(0已点，1未点)
						}
						members.put("mid", p.getYBasicMember().getFid()); //			fSupport(json数组)	mid	String	用户ID
						members.put("userNiceName", p.getYBasicMember().getFname());//				userNiceName	String	用户昵称
						String img[]= p.getYBasicMember().getFheadImage().split(",");
						members.put("userImageUrl", img[0]);//				userImageUrl	String	用户头像地址
						memberlist.add(members);
					}
				}
				
				resultObject.put("fSupport",memberlist);//			fSupport(json数组)
				
				List<YImage> images=new ArrayList();
				images=(List<YImage>)dao.findAllByHQL("from YImage as y where y.YManshowinformation.fid='"+show.getFid()+"'");
				List<Map<String , Object>> imglist=new ArrayList<Map<String,Object>>();
				for(YImage img:images){
					Map<String ,Object> imgs=new HashMap<String, Object>();
					imgs.put("thumbnailImageUrl", img.getFaddress()); //			fImages(json数组)	thumbnailImageUrl	String	缩略图地址
					imgs.put("imageUrl", img.getFaddress()); //							imageUrl	String	大图地址
					imglist.add(imgs);
				}
				
				
				List<Map<String,Object>> commentList=new ArrayList<Map<String,Object>>();//reviewArray（json数组，返回最新的5条评论）	
				for(YComment comment:comments){
					Map<String,Object> commap=new HashMap<String,Object>();
					commap.put("mid", comment.getYBasicMember().getFid());       //mid	             String	用户ID
					commap.put("nickname", comment.getYBasicMember().getFnickName());       //nickname	     String	用户昵称
					commap.put("reviewContent", comment.getFcontents());       //reviewContent	 String	评论内容
					commap.put("reviewDate", comment.getFtime());       //reviewDate	     String	评论时间
					String headImage=comment.getYBasicMember().getFheadImage();
					String[] headimgs=headImage.split(",");
					commap.put("imageUrl", headimgs[0]);       //imageUrl	     String	用户头像
					commentList.add(commap);
					if(commentList.size()==5){
						break;
					}
				}
				resultObject.put("fImages", imglist);
				resultList.add(resultObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}

	/**
	 * 根据会员和团体查询个人秀
	 */
	@Override
	@SystemServiceLog(description = "根据会员和团体查询个人秀")
	public List<YManshowinformation> getAllDynamicPagingList(
			Integer page, Integer pageSize, String groupId,String memberId) {
		List<Map<String,Object>> listmap=new ArrayList<Map<String,Object>>();
		List<YManshowinformation> paglist=new ArrayList<YManshowinformation>();
		try {
//			List<Object> values = new ArrayList<Object>();
			String hql="";
			String counthql="";
			int Begin=0;
			int End=0;
			
			Begin=page;
			End=pageSize+page;
			String sql="";
			List list=new ArrayList();
//			if(null!=memberId&&!"".equals(memberId)){
//				sql="select * from y_manshowinformation s where s.FMemberID='"+memberId+"' order by s.FPublished desc limit "+Begin+","+End;
//			}else{
//				sql="select * from y_manshowinformation s where s.FMemberID in (select m.fid from y_basic_member m "
//						+ "where m.FSocialGroupsID = '"+groupId+"') order by s.FPublished desc limit "+Begin+","+End;
//			}
//			list=(List)dao.getListBySql(sql);
			if(null!=memberId&&!"".equals(memberId)){
				hql="from YManshowinformation s where s.YBasicMember.fid='"+memberId+"' order by s.fpublished desc";
			}else{
				hql="from YManshowinformation s where s.YBasicMember.fid in (select m.fid from YBasicMember m where m.YBasicSocialgroups.fid='"+groupId+"') order by s.fpublished desc";
			}
			paglist=(List<YManshowinformation>)dao.find(hql, page, pageSize, null);
			
//			for(int i=0;i<list.size();i++){
//				YManshowinformation y=new YManshowinformation();
////				y.setFid(list.get(i).getFid());
////				y.setFcontents(list.get(i).getFcontents());
////				y.setFisOpen(list.get(i).getFisOpen());
////				y.setFpublished(list.get(i).getFpublished());
////				y.setFlag(list.get(i).getFlag());
////				y.setYBasicMember(list.get(i).getYBasicMember());
//				
//				paglist.add(y);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paglist;
	}

	@Override
	@SystemServiceLog(description = "根据个人秀id和会员id查询点赞记录")
	public List<YPointlike> findByPoint(String memberId, String showId) {
		List<YPointlike> list=new ArrayList<YPointlike>();
		list=(List<YPointlike>)dao.findAllByHQL("from YPointlike y where y.YBasicMember.fid='"+memberId+"' and y.fmanShowId='"+showId+"'");
		return list;
	}

	@Override
	@SystemServiceLog(description = "个人秀点赞")
	public void savePointlike(YPointlike pointlike) {
		dao.save(pointlike);
	}

	@Override
	@SystemServiceLog(description = "个人秀取消点赞")
	public void updatePointlike(YPointlike pointlike) {
		dao.update(pointlike);
	}

	@Override
	@SystemServiceLog(description = "获取个人秀评论列表")
	public List<Map<String, Object>> findByComment(String reviewID, String fid,
			Integer pageSize) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			String hql="";
			List<YComment> list=new ArrayList<YComment>();
			if(null!=reviewID&&!"".equals(reviewID)){
				YComment comment=new YComment();
				comment=(YComment)dao.findById(YComment.class, reviewID);
				hql="from YComment y where y.fforeignId='"+fid+"' and y.YBasicType.fid='0123456789' and y.flag <"+comment.getFlag()+"  order by y.flag desc";
			}else{
				hql="from YComment y where y.fforeignId='"+fid+"' and y.YBasicType.fid='0123456789'  order by y.flag desc";
			}
			
			list=(List<YComment>)dao.find(hql,0, pageSize, null);
			if(list.size()>0){
				for(YComment comment:list){
					Map<String ,Object> commentmap=new HashMap<String, Object>();
					String[] img=comment.getYBasicMember().getFheadImage().split(",");
					commentmap.put("reviewID",comment.getFid() );//reviewID	String	评论ID
					commentmap.put("fmemberID", comment.getYBasicMember().getFid());//fmemberID	String	会员ID
					commentmap.put("fmemberNickName", comment.getYBasicMember().getFname());//fmemberNickName	String	会员昵称
					commentmap.put("fmemberHeadImage", img[0]);//fmemberHeadImage	String	会员头像
					commentmap.put("fcontents", comment.getFcontents());//fcontents 	String	评论内容
					commentmap.put("ftime", comment.getFtime());//ftime	String	评论时间
					resultList.add(commentmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public Integer getNumByshowId(String showId) {
		Integer num=0;
		try {
			List<YPointlike> list=new ArrayList<YPointlike>();
			list=(List<YPointlike>)dao.findAllByHQL("from YPointlike as y where y.fmanShowId='"+showId+"' and y.farePointLike=0  and y.fpointLikeType=0");
			num=list.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	
}
