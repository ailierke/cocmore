package com.yunzo.cocmore.core.function.controller.appAPI;


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.model.mysql.YImage;
import com.yunzo.cocmore.core.function.model.mysql.YManshowinformation;
import com.yunzo.cocmore.core.function.model.mysql.YPointlike;
import com.yunzo.cocmore.core.function.model.mysql.YReplytocomment;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.ManshowinformationService;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.ResponseCode;
import com.yunzo.cocmore.utils.base.IMGSize;
import com.yunzo.cocmore.utils.base.YunzoCocSignCode;

/**
 * 个人秀app接口
 * @author yunzo
 *
 */
@RestController
@RequestMapping("/mobileapi/mysolo")
public class ManShowAppController {
	
	@Resource
	ManshowinformationService ManshowinformationService;
	
	@Resource
	CommentService commentService;                 //评论接口
	
	@Resource
	MemberServiceI memberService;                  //会员接口
	
	
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 发布个人秀
	 * @param infoMap
	 * @param request
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/addMySoloInfo")
	@SystemControllerLog(description="APP 发布个人秀")
	public void doNotNeedSessionAndSecurity_addMySoloInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
	
		String images=null;
		MultipartFile file = null;
		Map<String, Object> ImgInfoMap = null;
		//logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
		try {
			
			request.setCharacterEncoding("utf-8");
			String infoMap = request.getParameter("infoMap");
			if (request instanceof DefaultMultipartHttpServletRequest) {
				DefaultMultipartHttpServletRequest rest = (DefaultMultipartHttpServletRequest) request;
				MultipartHttpServletRequest r=(MultipartHttpServletRequest)request;
				ImgInfoMap = ImgUploadUtil.imgUpload(r,IMGSize.X200.value());
				if (ImgInfoMap != null)
					if ((boolean) ImgInfoMap.get("success") == true) {
						if ((ImgInfoMap.get("imgsrc")) != null) {
							images = (ImgInfoMap.get("imgsrc")).toString();
						}
					}
			}
			
			infoMap = URLDecoder.decode(infoMap, "UTF-8");
			JsonNode node = objectMapper.readTree(infoMap);

			String mid=(node.get("mid")==null?null:node.get("mid").textValue());
			String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
			
			String textContent=(node.get("textContent")==null?null:node.get("textContent").textValue());
			int isAllOpen=(node.get("isAllOpen")==null?4:node.get("isAllOpen").intValue());
			
			if(null==mid||"".equals(mid)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "mid 为空！");
				 COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==businessId||"".equals(businessId)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
				 COC_APPResponseResult.responseToGJson(map, response);
			}
			if(null==textContent||"".equals(textContent)){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "textContent 为空！");
				 COC_APPResponseResult.responseToGJson(map, response);
			}
			if(4==isAllOpen){
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "isAllOpen 为空！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			YBasicMember member=new YBasicMember();
			List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
			
			if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
				member=memberlist.get(0);
				YManshowinformation manshow=new YManshowinformation();
				manshow.setFid(UUID.randomUUID().toString());
				manshow.setFcontents(textContent);
				manshow.setFisOpen(isAllOpen);
				manshow.setFpublished(new Date());
				manshow.setYBasicMember(member);
				String img[]= images.split(",");
				if(img.length>0&&img.length<=10){
					ManshowinformationService.save(manshow);
					for(String imageurl:img){
						YImage image=new YImage();
						image.setFaddress(imageurl);
						image.setYManshowinformation(manshow);
						ManshowinformationService.saveImage(image);
					}
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				}
			}else{
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
				COC_APPResponseResult.responseToGJson(map, response);
			}
			
		} catch (Exception e) {
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			e.printStackTrace();
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 获取个人秀集合
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getMySoloInfoALLUsersList")
	@SystemControllerLog(description="APP 获取个人秀集合")
	public  void doNotNeedSessionAndSecurity_getMySoloInfoALLUsersList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
	
			List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String fid=(node.get("fId")==null?null:node.get("fId").textValue());
				Integer pageSize=(node.get("pageSize")==null?99:node.get("pageSize").intValue());
				String mid=(node.get("mid")==null?null:node.get("mid").textValue());
				String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
				
				if(null==businessId||"".equals(businessId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
					 COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==mid||"".equals(mid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "mid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(99==pageSize){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				YBasicMember member=new YBasicMember();
				List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
				if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
					member=memberlist.get(0);
					resultMap=ManshowinformationService.findManList(member.getFid(), pageSize, fid);
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					map.put(ResponseCode.MSGR.msg(), resultMap);
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
					COC_APPResponseResult.responseToGJson(map, response);
				}	
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
	/**
	 * 删除个人秀
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/delMySoloInfo")
	@SystemControllerLog(description="APP  删除个人秀")
	public  void doNotNeedSessionAndSecurity_delMySoloInfo(String infoMap,HttpServletResponse response){
		
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
	
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String manshowId=(node.get("fId")==null?null:node.get("fId").textValue());
				if(null==manshowId||"".equals(manshowId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "manshowId 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(manshowId!=null){
					YManshowinformation manshow=ManshowinformationService.getById(manshowId);
					ManshowinformationService.delete(manshow);
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.FORMWRONG.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.FORMWRONG.msg());
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
		
	}
	/**
	 * 删除个人秀评论回复
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/delMySoloInfoReview")
	@SystemControllerLog(description="APP  删除个人秀评论回复")
	public  void doNotNeedSessionAndSecurity_delMySoloInfoReview(String infoMap,HttpServletResponse response){
		
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
	
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String reviewId=(node.get("reviewId")==null?null:node.get("reviewId").textValue());
				if(null==reviewId||"".equals(reviewId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "reviewId 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				YComment comment=new YComment();
				comment=commentService.getById(reviewId);
				commentService.delete(comment);
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
		
	}
	/**
	 * 发布评论
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/addMySoloInfoReview")
	@SystemControllerLog(description="APP 发布评论")
	public  void doNotNeedSessionAndSecurity_addMySoloInfoReview(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
		
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String mid=(node.get("mid")==null?null:node.get("mid").textValue());
				String content=(node.get("content")==null?null:node.get("content").textValue());
				String fid=(node.get("fId")==null?null:node.get("fId").textValue());
				String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
				
				if(null==businessId||"".equals(businessId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
					 COC_APPResponseResult.responseToGJson(map, response);
				}
				
				if(null==mid||"".equals(mid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "mid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==content||"".equals(content)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "content 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==fid||"".equals(fid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "fId 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				YBasicMember member=new YBasicMember();
				List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
				if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
					member=memberlist.get(0);
					YComment comment=new YComment();
					YBasicType type=new YBasicType();
					type.setFid("0123456789");
					
					comment.setYBasicType(type);
					comment.setYBasicMember(member);
					comment.setFcontents(content);   //评论内容
					comment.setFtime(new Date());
					comment.setFforeignId(fid);     //关联个人秀id
					commentService.save(comment);
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
	
	}
	/**
	 * 获取我的个人秀列表
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/addMySoloInfoList")
	@SystemControllerLog(description="APP 获取我的个人秀列表")
	public  void doNotNeedSessionAndSecurity_addMySoloInfoList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
	
			List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String fid=(node.get("fId")==null?null:node.get("fId").textValue());
				Integer pageSize=(node.get("pageSize")==null?748:node.get("pageSize").intValue());
				String mid=(node.get("mid")==null?null:node.get("mid").textValue());
				String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
				
				if(null==businessId||"".equals(businessId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
					 COC_APPResponseResult.responseToGJson(map, response);
				}
				if(748==pageSize){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==mid||"".equals(mid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "mid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				YBasicMember member=new YBasicMember();
				List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
				if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
					member=memberlist.get(0);
					
					resultMap=ManshowinformationService.findMyManList(member.getFid(), pageSize, fid);
					map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
					map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
					map.put(ResponseCode.MSGR.msg(), resultMap);
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
		
	}
	/**
	 * 个人秀点赞 
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/support")
	@SystemControllerLog(description="APP 个人秀点赞 ")
	public  void doNotNeedSessionAndSecurity_support(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String fid=(node.get("fId")==null?null:node.get("mid").textValue());//fId	String	是	个人秀Id
				Integer isSupport=(node.get("isSupport")==null?748:node.get("mid").intValue());//isSupport	Int	是	点赞(0点赞，1取消点赞)
				String mid=(node.get("mid")==null?null:node.get("mid").textValue());
				String businessId=(node.get("businessId")==null?null:node.get("businessId").textValue());
				
				if(null==businessId||"".equals(businessId)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "businessId 为空！");
					 COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==mid||"".equals(mid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "mid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(null==fid||"".equals(fid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "fid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(748==isSupport){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "isSupport 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				
				YBasicMember member=new YBasicMember();
				List<YBasicMember> memberlist=memberService.findByHql("from YBasicMember as y  where y.fmobilePhone='"+mid+"'  and  y.YBasicSocialgroups.fid='"+businessId+"'");
				if(memberlist!=null&&!"".equals(memberlist)&&memberlist.size()>0){
					member=memberlist.get(0);
	
					YBasicType type=new YBasicType();
					type.setFid("0123456789");
					List<YPointlike> list=new ArrayList<YPointlike>();
					list=ManshowinformationService.findByPoint(member.getFid(), fid);
					if(isSupport==0){
						if(list.size()==0){
							YPointlike point=new YPointlike();
							point.setFmanShowId(fid);
							point.setYBasicMember(member);
							point.setYBasicType(type);
							point.setFpointLikeTime(new Date());
							point.setFarePointLike(0);           //isSupport		Int	是否点赞(0已点，1未点)
							point.setFpointLikeType(0);          // 0表示点赞   1表示  踩
							ManshowinformationService.savePointlike(point);
							map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
							map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
						}else if(list.size()==1){
							YPointlike point=new YPointlike();
							point=list.get(0);
							if(point.getFarePointLike()==1){
								point.setFarePointLike(0);
								ManshowinformationService.updatePointlike(point);
							}else{
								map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());   //已经点赞  不能再点赞
								map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
								COC_APPResponseResult.responseToGJson(map, response);
							}
						}else{
							map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());   //数据出错
							map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
							COC_APPResponseResult.responseToGJson(map, response);
						}
					}else{
						if(list.size()==0){
							map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());   //没有点赞 取消点赞
							map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
							COC_APPResponseResult.responseToGJson(map, response);
						}else if(list.size()==1){
							YPointlike point=new YPointlike();
							point=list.get(0);
							if(point.getFarePointLike()==0){
								point.setFarePointLike(1);
								ManshowinformationService.updatePointlike(point);
								map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
								map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
								COC_APPResponseResult.responseToGJson(map, response);
							}else{
								map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());   //已经取消点赞  不能再取消点赞
								map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
								COC_APPResponseResult.responseToGJson(map, response);
							}
						}else{
							map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());   //数据出错
							map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
							COC_APPResponseResult.responseToGJson(map, response);
						}
					}
				}else{
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "数据异常 ！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
		
	}
	
	/**
	 * 获取个人秀评论列表
	 * @param infoMap
	 * @return
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getSupplyAndDemandInfoCommentList")
	@SystemControllerLog(description="APP 获取个人秀评论列表")
	public  void doNotNeedSessionAndSecurity_getSupplyAndDemandInfoCommentList(String infoMap,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		//验证请求是否有效
	
			List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			try {
				infoMap = URLDecoder.decode(infoMap, "UTF-8");
				JsonNode node = objectMapper.readTree(infoMap);
				String reviewID=(node.get("reviewID")==null?null:node.get("reviewID").textValue());//reviewID	String	是	评论ID(如果为空字符串则表示获取最新的数据)
				String fid=(node.get("fid")==null?null:node.get("fid").textValue());//fid	String	是	个人秀ID
				Integer pageSize=(node.get("pageSize")==null?999:node.get("pageSize").intValue());//pageSize	Int	是	分页大小
				//String memberId=node.get("mid").textValue();
				
				if(null==fid||"".equals(fid)){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "fid 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				if(999==pageSize){
					map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
					map.put(ResponseCode.MSGM.msg(), "pageSize 为空！");
					COC_APPResponseResult.responseToGJson(map, response);
				}
				
				resultMap=ManshowinformationService.findByComment(reviewID,fid,pageSize);
				
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				map.put(ResponseCode.MSGR.msg(), resultMap);
			} catch (Exception e) {
				e.printStackTrace();
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			}
			COC_APPResponseResult.responseToGJson(map, response);
	}
}
