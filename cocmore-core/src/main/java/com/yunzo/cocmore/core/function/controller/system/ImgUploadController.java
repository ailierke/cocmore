package com.yunzo.cocmore.core.function.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.utils.base.AliyunOSSUtils;
import com.yunzo.cocmore.utils.base.IMGSize;

/** 
 *Description: <日志控制层>. <br>
 * @date:2014年11月27日 上午11:58:05
 * @author jiangxings
 * @version V1.0                             
 */
@Controller
@RequestMapping("/imgUpload")
public class ImgUploadController {
	private static final Logger logger = Logger.getLogger(ImgUploadController.class);
	
	
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST)
	@ResponseBody
	@SystemControllerLog(description = "上传图片日志")
	public Map<String,Object> imgUpload(MultipartHttpServletRequest request){
		logger.info("#######################图片上传方法日志....");
		List<MultipartFile> files = (List<MultipartFile>) request.getFiles("file");  
		Map<String,Object> map = new HashMap<String,Object>();
		ArrayList<String> srcList = new ArrayList<String>(); 
		for(MultipartFile imgFile:files){
			try{
			if(imgFile!=null&&imgFile.getInputStream().available()>0){
					map =  AliyunOSSUtils.upload2ImgToAliyun(imgFile.getInputStream(), imgFile.getOriginalFilename(),IMGSize.X88.value());
					if((boolean) map.get("success")==false){
						break;
					}else {
						srcList.add((String)map.get("imgsrc"));//原map中IMGSRC存单个上传后的图片src
					}
			}
				}catch(Exception e){
					e.printStackTrace();
				}   
			
		}
		if((boolean)map.get("success")==true){
			map.put("imgsrc",srcList);//如果所有都成功，将IMGSRC赋值成所有的 上传后的src
		}else {
			map.put("imgsrc",null);
		}
		map.put("msg", "测试");
		return map;
	}
	
}
