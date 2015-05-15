package com.yunzo.cocmore.core.function.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicJoinActivity;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YWallactivity;
import com.yunzo.cocmore.core.function.service.GetNumberService;
import com.yunzo.cocmore.core.function.service.GroupsAboutService;
import com.yunzo.cocmore.core.function.service.GroupsContactService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.util.ImgUploadUtil;
import com.yunzo.cocmore.core.function.util.PagingList;
import com.yunzo.cocmore.core.function.util.Tree1;
import com.yunzo.cocmore.core.function.vo.ImportGroupsVo;
import com.yunzo.cocmore.utils.base.IMGSize;
/**
 * 社会团体controller
 * @author jiangxing
 *
 */
@Controller
@RequestMapping("/groups")
public class GroupsController {
	
	private static final Logger logger = Logger.getLogger(GroupsController.class);
	@Resource
	GetNumberService getNumberService;
	@Resource(name="groupsService")
	GroupsService groupsService;
	
	@Resource
	GroupsAboutService groupsAboutService;          //关于社会团体接口
	
	@Resource
	GroupsContactService groupsContactService;      //社会团体联系方式接口
	
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {  
	    //注册自定义的属性编辑器 (日期)  
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
	    //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
	    binder.registerCustomEditor(Date.class, dateEditor);     
	}
	
	/**
	 * 根据发布人查询
	 * @param id
	 * @return
	 */
	@RequestMapping("/findGroupList")
	@ResponseBody
	@SystemControllerLog(description = "根据发布人查询社会团体")
	public Object[] findGroupList(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		List<YBasicSocialgroups> list = null;
		try {
			list = groupsService.getGroupList(id);
			if(list != null){
				map.put("obj", list);
				map.put("msg", "查询成功！");
				map.put("success", true);
			}else{
				map.put("obj", null);
				map.put("msg", "查询失败！");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "查询失败！");
			map.put("success", false);
		}
		return list.toArray();
	}
	
	/**
	 * 自主注册团体
	 * beck
	 * @param group
	 * @return
	 */
	@RequestMapping("/addRegisterGroup")
	@ResponseBody
	@SystemControllerLog(description = "自主注册团体")
	public void addRegisterGroup(YBasicSocialgroups group,MultipartHttpServletRequest request){
		logger.info("addRegisterGroup start....");
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X200.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				group.setLogo(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
			try{
				if(group.getFnumber()==null||group.getFnumber().equals("")){
					Map<String, Object> map1 = getNumberService.checkExist("XX-SHTTXX");
					group.setFnumber((String)map1.get("serialNumber"));
				}
				groupsService.addRegisterGroup(group);
				getNumberService.addSerialNumber("XX-SHTTXX");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 注册团体
	 * 20150108 duxi
	 * @param group
	 * @return
	 */
	@RequestMapping("/registGroup")
	@ResponseBody
	@SystemControllerLog(description = "注册团体")
	public Map<String, Object> registGroup(YBasicSocialgroups group){
		logger.info("registGroup start....");
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		try{
			flag = groupsService.addGroups(group);
			getNumberService.addSerialNumber("XX-SHTTXX");
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	/**
	 * 新增
	 * @param group
	 * @return
	 */
	@RequestMapping("/addGroup")
	@ResponseBody
	@SystemControllerLog(description = "新增团体")
	public void addGroup(YBasicSocialgroups group,MultipartHttpServletRequest request){
		logger.info("addGroup start....");
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X200.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				group.setLogo(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
			try{
				groupsService.addGroups(group);
				getNumberService.addSerialNumber("XX-SHTTXX");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询列表
	 * @param inform
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllGroupPagingList")
	@ResponseBody
	@SystemControllerLog(description = "查询社会团体列表，并分页")
	public Map<String,Object> findAllGroup(Integer start,Integer limit,String groupId,String orgId,String groupName){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroups> pageList = groupsService.getAllGroupsPagingList(start, limit, groupId, orgId, groupName);
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
		if((null!=orgId&&!"".equals(orgId))||(null!=groupName&&!"".equals(groupName))){
//			System.out.println(orgId);
			map.put("list", pageList.getList());//分页类容
		}else{
			if(null!= pageList.getList()&& pageList.getList().size()>0){
				map.put("list", pageList.getList().get(0));//分页类容
			}else{
				map.put("list", "");
			}
		}
		
		return map;
	}
	

	/**
	 * 查询列表
	 * @param inform
	 * @param start 查询第几条
	 * @param limit 每页显示条数
	 * @return
	 */
	@RequestMapping("/findAllGroupPagingList1")
	@ResponseBody
	@SystemControllerLog(description = "查询社会团体列表，并分页")
	public Map<String,Object> findAllGroup1(Integer start,Integer limit,String groupId,String orgId,String groupName){
		Map<String,Object> map = new HashMap<String,Object>();
		/**
		 * 查询页面显示和总条数
		 */
		PagingList<YBasicSocialgroups> pageList = groupsService.getAllGroupsPagingList(start, limit, groupId, orgId, groupName);
		map.put("success", true);
		map.put("msg","成功");
		map.put("count",pageList.getCount());//总条数
			map.put("list", pageList.getList());//分页类容
		return map;
	}
	/**
	 * 修改
	 * @param group
	 * @return
	 */
	@RequestMapping("/updateGroup")
	@ResponseBody
	@SystemControllerLog(description = "修改社会团体")
	public Map<String,Object> updateGroup(YBasicSocialgroups group,MultipartHttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroup start....");
		Map<String,Object> ImgInfoMap = ImgUploadUtil.imgUpload(request,IMGSize.X200.value());
		if((boolean)ImgInfoMap.get("success")==true){
			if(((StringBuffer)ImgInfoMap.get("imgsrc"))!=null&&!"".equals(((StringBuffer)ImgInfoMap.get("imgsrc")).toString())){
				group.setLogo(((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
				logger.info("保存图片url:"+((StringBuffer)ImgInfoMap.get("imgsrc")).toString());
			}
		}
		boolean flag = groupsService.update(group);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	
	/**
	 * 修改
	 * @param group
	 * @return
	 */
	@RequestMapping("/updateGroupStatus")
	@ResponseBody
	@SystemControllerLog(description = "修改社会团体")
	public Map<String,Object> updateGroupStatus(YBasicSocialgroups group,String status){
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("addGroup start....");
		boolean flag = true;
		try{
			map = groupsService.updateStatus(group,status);
		}catch(Exception e){
			map.put("success", false);
			map.put("msg","失败");
		}
		
		return map;
	}
	
	/**
	 * 查看单个
	 * @param group
	 * @return
	 */
	@RequestMapping("/getGroup")
	@ResponseBody
	@SystemControllerLog(description = "查看单个社会团体")
	public Map<String,Object> getGroup(YBasicSocialgroups group){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = groupsService.findGroup(group);
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	
	
	/**
	 * 生成树的方法
	 * 可以通过组织id和groupName来查询
	 * 新增判断是否为团体页面的typeId
	 */
	@RequestMapping(value = "/getGroupTree")
	@ResponseBody
	@SystemControllerLog(description = "生成树的方法")
	public List<Tree1> getGroupTree(String orgId,String groupName,String type,HttpSession session,String typeId){
		logger.info("YBasicSocialGroup getGroupTree");
		List<Tree1> treelist = new ArrayList<Tree1>();
		
		String userId=(String)session.getAttribute("userId");
		Map<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap = (Map<String, Object>)session.getAttribute("map");
		
		HashSet<String> groupSet=new HashSet<String>();
		if(!userId.equals("1")){
			groupSet=(HashSet<String>)sessionMap.get("groupSet");
		}
		
		
		try {
			if(null!=orgId&&!"".equals(orgId)){

				groupsService.createTree1(treelist,orgId,groupName,type,userId,groupSet,typeId);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(treelist);
		return treelist;
	}
	

	/**
	 * 查询商会下面是否下面子节点
	 */
	@RequestMapping(value = "/checkChildrenNode")
	@ResponseBody
	@SystemControllerLog(description = "查询商会下面是否下面子节点")
	public Map<String,Object> checkChildrenNode(String groupId){
		logger.info("YBasicSocialGroup checkChildrenNode");
		Map<String,Object> map = new HashMap<String,Object>();
		try {
				boolean flag = groupsService.checkChildrenNode(groupId);
				map.put("object", flag);
				map.put("success", true);
				map.put("msg","失败");
			
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg","失败");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据id查询社会团体对象
	 */
	@RequestMapping(value = "/getById")
	@ResponseBody
	@SystemControllerLog(description = "根据id查询社会团体对象")
	public Map<String,Object> getById(String groupId){
		logger.info("YBasicSocialGroup getById");
		Map<String,Object> map = new HashMap<String,Object>();
		try {
				YBasicSocialgroups group=new YBasicSocialgroups();
				group = groupsService.getById(groupId);
				map.put("object", group);
				map.put("success", true);
				map.put("msg","失败");
			
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg","失败");
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "/getByGroupId")
	@ResponseBody
	@SystemControllerLog(description = "根据团体id查询")
	public Map<String , Object> doNotNeedSessionAndSecurity_getByGroupId(String businessId){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> listmap = new HashMap<String,Object>();
		try {	
			map.putAll(groupsAboutService.getBygroupId(businessId));
			map.putAll(groupsContactService.getByGroupId(businessId));
			map.put("group", groupsService.getBygroupId(businessId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//需要模板页面
		return map;
	}
	
	@RequestMapping("/download")
	@ResponseBody
	@SystemControllerLog(description = "下载模板")
	public void download(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath("/function/template/socialgroupsTemplate.xlsx");
			File file = new File(filepath);
			long fileLength = file.length();  
			response.setContentType("multipart/form-data");  
			response.setHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));  
			response.setHeader("Content-Length", String.valueOf(fileLength));  

			bis = new BufferedInputStream(new FileInputStream(file));  
			bos = new BufferedOutputStream(response.getOutputStream());  
			byte[] buff = new byte[2048];  
			int bytesRead;  
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				bos.write(buff, 0, bytesRead);  
			}  
			bis.close();  
			bos.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 
	 * @param file
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/importOrUpdateGroups")
	@ResponseBody
	@SystemControllerLog(description = "导入社会团体")
	public Map<String, Object> importOrUpdateGroups(HttpServletRequest request,MultipartFile file){
		Map<String, Object> map = new HashMap<String, Object>();
//      1. 读取当前目录下文件 importStatus.lock.
//      2. 查看要处理的文件是否有相应同名的lock文件
//      3. 如果没有则生成lock文件，并进行写操作
//      4. 操作完毕，删除lock文件 
//      判断importStatus.lock是否存在，如果已经存在，说明已经有程序在进行导入导出，提示他...如果不存在就直接进行执行
		String filepath = request.getSession().getServletContext().getRealPath("/function/template/importStatus.lock");
		File lockFile = new File(filepath);
		System.out.println("******************************************");
		System.out.println(filepath);
		System.out.println("*******************************************");
		if(lockFile.exists()){
			map.put("msg", "已经有程序在进行导入,请稍后.....");
			map.put("success", false);
			return map;
		}else{
			try {
				lockFile.createNewFile();
			} catch (IOException e) {
				lockFile.delete();
				e.printStackTrace();
			}
		}
		groupsService.writeStatus(request, "*********批量导入商会Excel状态日志文件***************", lockFile,false);
		logger.info("importOrUpdateGroups groups");
		try {
//			File file = new File("D:\\joinActivityPeople.xlsx");
		    Workbook  wb = null;  
		    InputStream in= file.getInputStream();
		    if (!in.markSupported()) {
		    	in = new PushbackInputStream(in, 8);
		    	}
		    if (POIFSFileSystem.hasPOIFSHeader(in)) {
		    	wb= new HSSFWorkbook(in);
		    }else{
		    	if (POIXMLDocument.hasOOXMLHeader(in)) {
			    	wb= new XSSFWorkbook(OPCPackage.open(in));
			    }
		    }
         Cell cell = null; 
         ImportGroupsVo importGroupVO = null;
         for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {  
             Sheet st = wb.getSheetAt(sheetIndex);  
             for (int rowIndex = 1; rowIndex < st.getLastRowNum(); rowIndex++) {  
                 Row row = st.getRow(rowIndex);  
                short clumLength = row.getLastCellNum();
                importGroupVO = new ImportGroupsVo();
                for(short i=1;i<clumLength;i++){
                	String value ="";
                	cell = row.getCell(i);
                    if(cell!=null){
                    	if(i==12){
                    		 cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                    		 Date date = cell.getDateCellValue();
                    		 value = new SimpleDateFormat("yyyy/MM/dd").format(date);
                    	}else{
                    		 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        	 value = cell.getStringCellValue();
                    	}
                    	
                    }else{
                    	value="";
                    }
                    switch (i) {
					case 1:
						importGroupVO.setGroupsName(value);
						break;
					case 2:
						importGroupVO.setGroupJCName(value);
						break;
					case 3:
						importGroupVO.setLoginName(value);
						break;
					case 4:
						importGroupVO.setPeopleNum(value);
						break;
					case 5:
						importGroupVO.setPeopleName(value);
						break;
					case 6:
						importGroupVO.setType(value);
						break;
					case 7:
						importGroupVO.setContactName(value);
						break;
					case 8:
						importGroupVO.setTelphone(value);
						break;
					case 9:
						importGroupVO.setPhone(value);
						break;
					case 10:
						importGroupVO.setMail(value);
						break;
					case 11:
						importGroupVO.setIsSign(value);
						break;
					case 12:
						importGroupVO.setSignTime(value);
						break;
					case 13:
						importGroupVO.setInfoComeFrom(value);
						break;
					case 14:
						importGroupVO.setLevel(value);
						break;
					case 15:
						importGroupVO.setDistct(value);
						break;
					case 16:
						importGroupVO.setProvice(value);
						break;
					case 17:
						importGroupVO.setCity(value);
						break;
					case 18:
						importGroupVO.setContry(value);
						break;
					case 19:
						importGroupVO.setAddress(value);
						break;
					case 20:
						importGroupVO.setOganazation(value);
						break;
					default:
						break;
					}
                }
                //这里的的对每一条更新，不对其进行所有事物的回滚操作，有异常记录下来然后继续执行Excel的下一行
                try{
                	groupsService.importOrUpdateGroups(importGroupVO,rowIndex,request,lockFile);
                	groupsService.writeStatus(request, "<--成功--> 第 "+rowIndex +" 行 ："+importGroupVO.getGroupsName()+" 导入成功!!!!!", lockFile,true);
                }catch(Exception e){
                	lockFile.delete();
                }
             }  
         }  
			map.put("msg", "导入成功！");
			map.put("success", true);
			lockFile.delete();
		} catch (Exception e) {
			lockFile.delete();
			e.printStackTrace();
			map.put("msg", "导入失败！");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 
	 * @param file
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/downloadImportStatus")
	@ResponseBody
	public void downloadImportStatus(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;
		try {
			request.setCharacterEncoding("UTF-8");
			String filepath = request.getSession().getServletContext().getRealPath("/function/template/importStatus.txt");
			File file = new File(filepath);
			long fileLength = file.length();  
			response.setContentType("multipart/form-data");  
			response.setHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));  
			response.setHeader("Content-Length", String.valueOf(fileLength));  

			bis = new BufferedInputStream(new FileInputStream(file));  
			bos = new BufferedOutputStream(response.getOutputStream());  
			byte[] buff = new byte[2048];  
			int bytesRead;  
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
				bos.write(buff, 0, bytesRead);  
			}  
			bis.close();  
			bos.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有评论默认最新的在前面
	 * @return
	 */
	@RequestMapping("/getLeastYComent")
	@ResponseBody
	@SystemControllerLog(description = "获取所有评论默认最新的在前面")
	public Map<String, Object> getLeastYComent(Integer start,Integer limit){
		logger.info("getLeastYComent start....");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = true;
		try{
			map = groupsService.getLeastYComent(start,limit);
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
	
	/**
	 * 获取所有评论默认最新的在前面
	 * @return
	 */
	@RequestMapping("/delYComent")
	@ResponseBody
	@SystemControllerLog(description = "获取所有评论默认最新的在前面")
	public Map<String, Object> delYComent(String fid){
		logger.info("delYComent start....");
		boolean flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			groupsService.delYComent(fid);
		}catch(Exception e){
			e.printStackTrace();
			flag =false;
		}
		map.put("success", flag);
		map.put("msg",flag==false?"失败":"成功");
		return map;
	}
}
