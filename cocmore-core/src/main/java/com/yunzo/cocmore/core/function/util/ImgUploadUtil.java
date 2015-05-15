package com.yunzo.cocmore.core.function.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yunzo.cocmore.utils.base.AliyunOSSUtils;

public class ImgUploadUtil {
	private static final Logger logger = Logger.getLogger(ImgUploadUtil.class);

	/**
	 * @param request
	 * @param type
	 *            缩放大小的类型
	 * @return
	 */
	public static Map<String, Object> imgUpload(MultipartHttpServletRequest request, String type) {
		logger.info("#######################图片上传方法日志....");
		List<MultipartFile> files = (List<MultipartFile>) request
				.getFiles("file");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);// 如果没有文件就直接true
		if (files != null && files.size() > 0) {
			StringBuffer imgUrls = new StringBuffer(); // 图片的url拼接
			for (MultipartFile imgFile : files) {
				try {
					if (imgFile != null
							&& imgFile.getInputStream().available() > 0) {
						map = AliyunOSSUtils.upload2ImgToAliyun(
								imgFile.getInputStream(),
								imgFile.getOriginalFilename(), type);
						if ((boolean) map.get("success") == false) {
							break;
						} else {
							imgUrls.append((String) map.get("imgsrc") + ",");// 原map中IMGSRC存单个上传后的图片src
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if ((boolean) map.get("success") == true) {
				map.put("imgsrc", imgUrls);// 如果所有都成功，将IMGSRC赋值成所有的 上传后的src
			} else {
				map.put("imgsrc", null);
			}
			map.put("msg", "测试");
		} else {
			logger.info("没有文件");
		}
		return map;
	}

	/**
	 * @param request
	 * @param type
	 *            缩放大小的类型
	 * @return
	 */
	public static Map<String, Object> imgUpload(MultipartFile file, String type) {
		logger.info("#######################图片上传方法日志....");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);// 如果没有文件就直接true
		if (file != null) {
			StringBuffer imgUrls = new StringBuffer(); // 图片的url拼接
			try {
				if (file != null && file.getInputStream().available() > 0) {
					map = AliyunOSSUtils.upload2ImgToAliyun(
							file.getInputStream(), file.getOriginalFilename(),
							type);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.info("没有文件");
		}
		return map;
	}
	/**
	 * @param MultipartFile
	 * @return
	 */
	public static Map<String, Object> imgUpload(MultipartFile file) {
		logger.info("#######################图片上传方法日志....");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);// 如果没有文件就直接true
		if (file != null) {
			try {
				if (file != null && file.getInputStream().available() > 0) {
					map = AliyunOSSUtils.uploadImgToAliyun(
							file.getInputStream(), file.getOriginalFilename());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.info("没有文件");
		}
		return map;
	}
	/**
	 * @param files
	 * @param type
	 * 			缩放大小的类型
	 * @return
	 */
	public static Map<String, Object> imgUpload(List<MultipartFile> files, String type) {
		logger.info("#######################图片上传方法日志....");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);// 如果没有文件就直接true
		if (files != null) {
			StringBuffer imgUrls = new StringBuffer(); // 图片的url拼接
			try {
				for(MultipartFile file:files){
					if (file != null && file.getInputStream().available() > 0) {
						map = AliyunOSSUtils.upload2ImgToAliyun(
								file.getInputStream(), file.getOriginalFilename(),
								type);
						if ((boolean) map.get("success") == false) {
							break;
						} else {
							imgUrls.append((String) map.get("imgsrc") + ",");// 原map中IMGSRC存单个上传后的图片src
						}
					}
				}
				if ((boolean) map.get("success") == true) {
					map.put("imgsrc", imgUrls);// 如果所有都成功，将IMGSRC赋值成所有的 上传后的src
				} else {
					map.put("imgsrc", null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.info("没有文件");
		}
		return map;
	}
}
