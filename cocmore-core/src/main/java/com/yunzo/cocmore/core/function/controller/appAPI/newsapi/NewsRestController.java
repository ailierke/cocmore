package com.yunzo.cocmore.core.function.controller.appAPI.newsapi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzo.cocmore.core.baseinit.COC_APPResponseResult;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.TNewsCollect;
import com.yunzo.cocmore.core.function.model.mysql.TNewsHeadline;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicType;
import com.yunzo.cocmore.core.function.model.mysql.YComment;
import com.yunzo.cocmore.core.function.service.CollectService;
import com.yunzo.cocmore.core.function.service.CommentService;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.TNewsHeadlineService;
import com.yunzo.cocmore.core.function.util.ResponseCode;

/**
 * @author：jackpeng
 * @date：2014年12月12日上午10:43:18 新闻
 */
@RestController
@RequestMapping("/mobileapi/newinfo")
public class NewsRestController {

	ObjectMapper objectMapper = new ObjectMapper();

	@Resource(name = "newsHeadlineService")
	private TNewsHeadlineService newsHeadlineService;
	// 评论
	@Resource(name = "commentService")
	private CommentService commentService;
	// 新闻收藏
	@Resource(name = "collectService")
	private CollectService collectService;
	//会员
	@Resource(name = "memberService")
	private MemberServiceI memberService;

	// 此处的参数也可以是ServletRequestDataBinder类型
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
		// 注册自定义的属性编辑器 (日期)
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(df, true);
		// 表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	/**
	 * 新闻轮播图
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getNewsCarouselList")
	@ResponseBody
	@SystemControllerLog(description = "APP 新闻轮播图")
	public void getNewsCarouselList(String infoMap, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Map<String, Object> maps = new HashMap<String, Object>();
		infoMap = URLDecoder.decode((infoMap != null ? infoMap : ""), "UTF-8");
		// 验证请求是否有效
		List<Map<String, Object>> listMap = null;
		try {
			listMap = newsHeadlineService.getHeadlineMap();
			if (listMap != null) {
				maps.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				maps.put(ResponseCode.MSGM.msg(), ResponseCode.SUCCESS.msg());
				maps.put(ResponseCode.MSGR.msg(), listMap);
			} else {
				maps.put(ResponseCode.MSGC.msg(),ResponseCode.EXCEPTION.value());
				maps.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
				maps.put(ResponseCode.MSGR.msg(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			maps.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			maps.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
			maps.put(ResponseCode.MSGR.msg(), null);
		}
		COC_APPResponseResult.responseToGJson(maps, response);
	}

	/**
	 * 新闻详情其他属性
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getNewsOther")
	@ResponseBody
	@SystemControllerLog(description = "APP 新闻详情其他属性")
	public void getNewsOther(String infoMap, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		Map<String, String> mapNews = new HashMap<String, String>();
		TNewsHeadline news = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			String newsId = (String) jsonObj.get("tId");
			String userPhone = (String) jsonObj.get("userName");
			if (null == userPhone || "".equals(userPhone)
					|| (null == newsId || "".equals(newsId))) {
				result = "tId或userName不能为空！";
			} else {
				news = newsHeadlineService.getById(newsId);
				if (news != null) {
					mapNews.put("tId", news.getFtid());
					mapNews.put("title", news.getFtitle());
					mapNews.put("describe", news.getFdescribe());
					mapNews.put("detailsUrl", news.getFdetailsUrl());
					mapNews.put("commentNumber",commentService.getCountNum(newsId));
					mapNews.put("isCollect",collectService.getCollectYN(newsId, userPhone));
					result += ResponseCode.SUCCESS.msg();
					flag = true;
				} else {
					result += ResponseCode.EXCEPTION.msg();
					flag = false;
				}
			}
			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
				map.put(ResponseCode.MSGR.msg(), mapNews);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
				map.put(ResponseCode.MSGR.msg(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 删除新闻评论
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/deleteNewsCommentById")
	@ResponseBody
	@SystemControllerLog(description = "APP 删除新闻评论")
	public void deleteNewsCommentById(String infoMap,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		YComment comment = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			String commentId = (String) jsonObj.get("tId");// 获取评论id
			if (null == commentId || "".equals(commentId)) {
				result += "tId 为空！";
			} else {
				comment = commentService.getById(commentId);
				if (comment != null) {
					commentService.delete(comment);
					result += ResponseCode.SUCCESS.msg();
					flag = true;
				} else {
					result += ResponseCode.EXCEPTION.msg();
					flag = false;
				}
			}
			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 添加新闻评论
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/putNewsCommentById")
	@ResponseBody
	@SystemControllerLog(description = "APP 添加新闻评论")
	public void putNewsCommentById(String infoMap, HttpServletResponse response)
			throws UnsupportedEncodingException {
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		Boolean flag = false;
		YComment comment = new YComment();
		String hqlMember = null;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			String tId = (String) jsonObj.get("tId");// 获取新闻id
			String commentContent = (String) jsonObj.get("commentContent");// 获取评论内容
			String mid = (String) jsonObj.get("mid");
			String businessId = (String)jsonObj.get("businessId");
			//获取会员id
			hqlMember = "from YBasicMember y where y.fhomePhone = '"+mid+"' and y.YBasicSocialgroups.fid = '"+businessId+"'";
			List<YBasicMember> listMember = memberService.findByHql(hqlMember);
			YBasicMember mem = listMember.get(0);

			if ((null == tId || "".equals(tId)) || (null == commentContent || "".equals(commentContent)) || (null == mid || "".equals(mid))) {
				result += "tId，mid，commentContent 不能为空！";
				flag = false;
			} else {
				comment.setFforeignId(tId);
				comment.setFcontents(commentContent);
				YBasicMember member = new YBasicMember();
				member.setFid(mem.getFid());
				comment.setYBasicMember(member);
				YBasicType type = new YBasicType();
				type.setFid("3101");
				comment.setYBasicType(type);
				comment.setFtime(new Date());
				commentService.save(comment);
				flag = true;
			}

			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 添加新闻收藏
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/putNewsCollectById")
	@ResponseBody
	@SystemControllerLog(description = "APP 添加新闻收藏")
	public void putNewsCollectById(String infoMap, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		List<TNewsCollect> list = null;
		TNewsCollect newsCollect = null;
		String hql = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			String tId = (String) jsonObj.get("tId");
			String userPhone = (String) jsonObj.get("userName");// 获取用户电话
			if ((null == tId || "".equals(tId)) || (null == userPhone || "".equals(userPhone))) {
				result += "tId，userName 不能为空！";
				flag = false;
			} else {
				newsCollect = new TNewsCollect();
				hql = "from TNewsCollect t where t.TNewsHeadline.ftid = '"+ tId + "' and t.ftel = '" + userPhone + "'";
				list = collectService.getByHql(hql);
				if (list.size() > 0) {
					result += ResponseCode.EXCEPTION.msg() + "[已收藏该新闻]";
					flag = false;
				} else {
					TNewsHeadline news = new TNewsHeadline();
					news.setFtid(tId);
					newsCollect.setTNewsHeadline(news);
					newsCollect.setFtel(userPhone);
					newsCollect.setFcreateTime(new Date());
					collectService.save(newsCollect);
					result += ResponseCode.SUCCESS.msg();
					flag = true;
				}
			}
			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 取消新闻收藏
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/deleteNewsCollectById")
	@ResponseBody
	@SystemControllerLog(description = "APP 取消新闻收藏")
	public void deleteNewsCollectById(String infoMap,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		List<TNewsCollect> list = null;
		TNewsCollect newsCollect = null;
		String hql = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			String tId = (String) jsonObj.get("tId");// 新闻id
			String userPhone = (String) jsonObj.get("userName");// 获取用户电话
			if ((null == userPhone || "".equals(userPhone)) || (null == tId || "".equals(tId))) {
				result += "tId，userName 不能为空！";
				flag = false;
			} else {
				hql = "from TNewsCollect y where y.TNewsHeadline.ftid = '"+ tId + "' and y.ftel = '" + userPhone + "'";
				list = collectService.getByHql(hql);
				if (list.size() > 0) {
					newsCollect = list.get(0);
					collectService.delete(newsCollect);
					result += ResponseCode.SUCCESS.msg();
					flag = true;
				} else {
					result += ResponseCode.EXCEPTION.msg();
					flag = false;
				}
			}
			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 获取收藏列表
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getNewsCollectListV2")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取收藏列表")
	public void getNewsCollectListV2(String infoMap,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		Map<String, Object> listStr = null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		String hql = null;
		List<TNewsCollect> list = null;
		TNewsCollect coll = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			Integer rowNum = (Integer) jsonObj.get("rowNum");
			String tId = (String) jsonObj.get("tId");
			String userName = (String) jsonObj.get("userName");

			if ((null == userName || "".equals(userName)) || (null == rowNum || "".equals(rowNum))) {
				result += "rowNum，userName 不能为空！";
				flag = false;
			} else {
				if (tId != null && !"".equals(tId)) {
					String sql = "from TNewsCollect y where y.TNewsHeadline.ftid = '" + tId + "'";
					List<TNewsCollect> colList = collectService.getByHql(sql);
					coll = colList.get(0);
					int lag = coll.getFlag();
					hql = "from TNewsCollect y where y.ftel = '" + userName + "' and y.flag < " + lag + " order by y.flag desc";
				} else {
					hql = "from TNewsCollect y where y.ftel = '" + userName + "' order by y.flag desc";
				}
				list = collectService.findByHql(hql, rowNum);
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						listStr = new HashMap<String, Object>();
						TNewsCollect collect = list.get(i);
						listStr.put("tId", collect.getTNewsHeadline().getFtid());
						listStr.put("title", collect.getTNewsHeadline().getFtitle());
						listStr.put("describe", collect.getTNewsHeadline().getFdescribe());
						listStr.put("tImageUrl", collect.getTNewsHeadline().getFtImageUrl());
						listStr.put("source", collect.getTNewsHeadline().getFsource());
						listStr.put("releaseTime", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(collect.getTNewsHeadline().getFreleaseTime()));
						listStr.put("commentNumber",commentService.getCountNum(tId));
						listStr.put("detailsUrl", collect.getTNewsHeadline().getFdetailsUrl());
						listStr.put("createTime", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(collect.getFcreateTime()));
						listMap.add(listStr);
					}
				}
				result += ResponseCode.SUCCESS.msg();
				flag = true;
			}

			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
				map.put(ResponseCode.MSGR.msg(), listMap);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 新闻列表
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getNewsListV2")
	@ResponseBody
	@SystemControllerLog(description = "APP 新闻列表")
	public void getNewsListV2(String infoMap, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		Map<String, Object> listStr = null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<TNewsHeadline> list = null;
		String hql = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			Integer rowNum = (Integer) jsonObj.get("rowNum");
			String tId = (String) jsonObj.get("tId");

			if (null == rowNum || "".equals(rowNum)) {
				result += "rowNum 不能为空！";
				flag = false;
			} else {
				if (tId != null && !tId.equals("")) {
					TNewsHeadline headli = newsHeadlineService.getById(tId);
					int lag = headli.getFlag();
					hql = "from TNewsHeadline y where y.flag < " + lag + " order by y.flag desc";
				} else {
					hql = "from TNewsHeadline y order by y.flag desc";
				}
				list = newsHeadlineService.findByHql(hql, rowNum);
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						listStr = new HashMap<String, Object>();
						TNewsHeadline news = list.get(i);
						listStr.put("tId", news.getFtid());
						listStr.put("title", news.getFtitle());
						listStr.put("tImageUrl", news.getFtImageUrl());
						listStr.put("source", news.getFsource());
						listStr.put("classification", news.getFclassification());
						listStr.put("describe", news.getFdescribe());
						listStr.put("releaseTime", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(news.getFreleaseTime()));
						listStr.put("commentNumber",commentService.getCountNum(tId));
						listStr.put("detailsUrl", news.getFdetailsUrl());
						listStr.put("createTime", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(news.getFcreateTime()));
						listMap.add(listStr);
					}
				}
				result += ResponseCode.SUCCESS.msg();
				flag = true;
			}

			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
				map.put(ResponseCode.MSGR.msg(), listMap);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

	/**
	 * 获取评论列表
	 * 
	 * @param infoMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("restriction")
	@RequestMapping("/getNewsCommentListByIdV2")
	@ResponseBody
	@SystemControllerLog(description = "APP 获取评论列表")
	public void getNewsCommentListByIdV2(String infoMap,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		infoMap = URLDecoder.decode(infoMap, "UTF-8");
		Map<String, Object> listStr = null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<YComment> list = null;
		String hql = null;
		String result = "";
		Boolean flag = false;
		try {
			Map<String, Object> jsonObj = objectMapper.readValue(infoMap,Map.class);
			String tId = (String) jsonObj.get("tId");// 新闻id
			Integer rowNum = (Integer) jsonObj.get("rowNum");
			String fId = (String) jsonObj.get("fId");// 评论id

			if ((null == rowNum || "".equals(rowNum)) || (null == tId || "".equals(tId))) {
				result += "tId,rowNum 不能为空！";
				flag = false;
			} else {
				if (fId != null && !"".equals(fId)) {
					YComment comm = commentService.getById(fId);
					int lag = comm.getFlag();
					hql = "from YComment y where y.flag < " + lag + " and y.fforeignId = '" + tId + "' order by y.flag desc";
				} else {
					hql = "from YComment y where y.fforeignId = '" + tId + "' order by y.flag desc";
				}
				list = commentService.findByHql(hql, rowNum);
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						listStr = new HashMap<String, Object>();
						YComment comment = list.get(i);
						listStr.put("fId", comment.getFid());
						listStr.put("mid", comment.getYBasicMember().getFhomePhone());
						listStr.put("imageUrl", comment.getYBasicMember().getFheadImage());
						listStr.put("realName", comment.getYBasicMember().getFname());
						listStr.put("commentContent", comment.getFcontents());
						listStr.put("releaseTime", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(comment.getFtime()));
						listMap.add(listStr);
					}
				}
				result += ResponseCode.SUCCESS.msg();
				flag = true;
			}
			if (flag) {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.SUCCESS.value());
				map.put(ResponseCode.MSGM.msg(), result);
				map.put(ResponseCode.MSGR.msg(), listMap);
			} else {
				map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
				map.put(ResponseCode.MSGM.msg(), result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put(ResponseCode.MSGC.msg(), ResponseCode.EXCEPTION.value());
			map.put(ResponseCode.MSGM.msg(), ResponseCode.EXCEPTION.msg());
		}
		COC_APPResponseResult.responseToGJson(map, response);
	}

}
