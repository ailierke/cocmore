package com.yunzo.cocmore.core.function.controller.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesMemberdistribution;
import com.yunzo.cocmore.core.function.model.mysql.YCompanyproduct;
import com.yunzo.cocmore.core.function.service.MembercompanyServiceI;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.utils.base.AliyunOSSUtils;
import com.yunzo.cocmore.utils.base.IMGSize;

/** 
 *Description: <会员公司控制层>. <br>
 * @date:2014年11月26日 下午3:06:25
 * @author beck
 * @version V1.0                             
 */
@Controller
@RequestMapping("/com")
public class MembercompanyController {
	private static final Logger logger = Logger.getLogger(MembercompanyController.class);
	
	@Resource(name = "companyService")
	private MembercompanyServiceI companyService;
	
	@RequestMapping("/findCompanyPage")
	@ResponseBody
	@SystemControllerLog(description="查找公司")
	public Map<String, Object> findCompanyPage(Integer start,Integer limit,String groupId,String tradeId,String industryId){
		logger.info("find YBasicMembercompany");
		Map<String, Object> map = new HashMap<String, Object>();
		PagingList<YBasicMembercompany> pagingList = null;
		try {
			pagingList = companyService.getCompanyPagingList(start, limit,groupId, tradeId, industryId);
			map.put("list", pagingList.getList());
			map.put("count", pagingList.getCount());
			map.put("msg", "查询成功！");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findComById")
	@ResponseBody
	@SystemControllerLog(description="查找公司根据id")
	public Map<String, Object> doNotNeedSessionAndSecurity_findComById(@RequestParam("fid")String fid){
		logger.info("YBasicMembercompany findComById");
		Map<String, Object> map = new HashMap<String,Object>();
		YBasicMembercompany obj = null;
		try {
			obj = companyService.getById(fid);
			map.put("msg", "查询成功");
			map.put("obj", obj);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 根据ID查询主营产品信息
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findProductByComID")
	@ResponseBody
	@SystemControllerLog(description="根据ID查询主营产品信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_findProductByComID(@RequestParam("fid")String fid){
		logger.info("YBasicMembercompany findComById");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YCompanyproduct> list  = new ArrayList<YCompanyproduct>();
		try {
			list = companyService.findProductByComID(fid);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 根据行业、产业查询
	 * @param fid
	 * @return
	 */
	@RequestMapping("/findAllByIndAndTrade")
	@ResponseBody
	@SystemControllerLog(description="根据行业、产业查询")
	public Map<String, Object> doNotNeedSessionAndSecurity_findAllByIndAndTrade(String ifid,String tfid){
		logger.info("YBasicMembercompany  findAllByIndAndTrade");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String hql = "from YBasicMembercompany y where y.YBasicIndustry.fid = '" + ifid + "' or y.YBasicTrade.fid = '" + tfid + "'";
			List<YBasicMembercompany> list = companyService.findAllByIndAndTrade(hql);
			map.put("msg", "查询成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 新增
	 * @param fid
	 * @return
	 */
	@RequestMapping("/saveCom")
	@ResponseBody
	@SystemControllerLog(description="新增公司信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_saveCom(YBasicMembercompany form,String[] fname,String[] fdescription,
			MultipartHttpServletRequest request){
		logger.info("YBasicMembercompany  saveCom");
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String, Object> imgMap = new HashMap<String,Object>();
		Set<YCompanyproduct> pts = new HashSet<YCompanyproduct>();
		
		YCompanyproduct product = null;
		form.setFcid(UUID.randomUUID().toString());
		try {

					//创建公司产品对象
					for(int i = 0;i < fname.length; i++){
						product = new YCompanyproduct();
						product.setFid(UUID.randomUUID().toString());
						product.setYBasicMembercompany(form);
						product.setFname(fname[i]);
						if(fdescription[i].equals("null")){
							product.setFdescription("");
						}else{
							product.setFdescription(fdescription[i]);
						}
						MultipartFile file = request.getFile("file" + i);
						imgMap = AliyunOSSUtils.upload2ImgToAliyun(file.getInputStream(), file.getOriginalFilename(),IMGSize.X200.value());
						if(((String)imgMap.get("imgsrc")).equals("") == false){
							//System.out.println((String)imgMap.get("imgsrc"));
							product.setFlogoImage((String)imgMap.get("imgsrc"));
						}
						pts.add(product);
					}
					
			form.setCompanyproducts(pts);
			companyService.save(form);
			map.put("msg", "查询成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 用会员ID查询公司
	 * @param fid
	 * @return
	 */
	@RequestMapping("/getComByMemberID")
	@ResponseBody
	@SystemControllerLog(description=" 用会员ID查询公司")
	public Map<String, Object> doNotNeedSessionAndSecurity_getComByMemberID(String fid){
		logger.info("YBasicMembercompany  getComByMemberID");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YBasicMembercompany com = companyService.getByMemberId(fid);
			map.put("msg", "查询成功");
			map.put("success", true);
			map.put("obj", com);
			map.put("ptList", com == null ? null : com.getCompanyproducts());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
	
	
	
	/**
	 * 修改
	 * @param fid
	 * @return
	 */
	@RequestMapping("/updateCom")
	@ResponseBody
	@SystemControllerLog(description=" 修改公司信息")
	public Map<String, Object> doNotNeedSessionAndSecurity_updateCom(YBasicMembercompany form,String[] fid,String[] fname,String[] fdescription,String[] flogoImage,
			MultipartHttpServletRequest request){
		logger.info("YBasicMembercompany  updateCom");
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String, Object> imgMap = new HashMap<String,Object>();
		
		Set<YCompanyproduct> pts = new HashSet<YCompanyproduct>();
		YCompanyproduct product = null;
		try {
			
			//创建公司产品对象
			for(int i = 0;i < fname.length; i++){
				
				String id = fid[i];
				if(id == null || id.equals("")){
					product = new YCompanyproduct();
				}else{
					product = companyService.getProductById(id);
					//product.setFid(UUID.randomUUID().toString());
				}
				product.setYBasicMembercompany(form);
				product.setFname(fname[i]);
				
				if(fdescription[i].equals("null")){
					product.setFdescription("");
				}else{
					product.setFdescription(fdescription[i]);
				}
				
				MultipartFile file = request.getFile("file" + i);
				imgMap = AliyunOSSUtils.upload2ImgToAliyun(file.getInputStream(), file.getOriginalFilename(),IMGSize.X200.value());
				if(((String)imgMap.get("imgsrc")).equals("") == false){
					//判断是否存在路径
					product.setFlogoImage((String)imgMap.get("imgsrc"));
				}else{
					product.setFlogoImage(flogoImage.length == 0 ? null : flogoImage[i]);
				}
				pts.add(product);
			}

		form.setCompanyproducts(pts);
		companyService.update(form);
		map.put("msg", "修改成功");
		map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败");
			map.put("success", false);
		}
		return map;
	}
}
