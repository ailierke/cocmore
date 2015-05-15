package com.yunzo.cocmore.utils.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.HttpMethod;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;

/**
 * 阿里云服务接口
 * 
 * @author star
 *
 */
public class AliyunOSSUtils {
	// 阿里云OSS ID对
	private static final String ACCESSKEYID = "u3NBdp48iFi6Eosz";
	private static final String ACCESSKEYSECRET = "joSCkKojahx88oDoDTP6ABp5xq9Y3N";

	private static OSSClient client = new OSSClient(ACCESSKEYID, ACCESSKEYSECRET);
	private static final String BUCKET ="yunzo"; 
	private static final String FILEDIR ="cocmore_product/";
	public static final String UPLOADEXCEPTION = "上传图片异常";//缩放比例
	public static final String NOTIMG = "非图片文件";//缩放比例


	public static boolean checkImgFile(ByteArrayOutputStream baos) throws IOException{
		
		InputStream stream = new ByteArrayInputStream(baos.toByteArray());
		ImageInputStream iis = ImageIO.createImageInputStream(stream);//resFile为需被
		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
		if (!iter.hasNext()) {//文件不是图片
			return false;
		} else{
			return true;
		}
	}
	/**
	 *图片2张图片（原图和缩略图）上传阿里云
	 *返回map包括下面key
	 * IMGSRC       原图片上传后网络url
	 * IMGSRC_SLT   缩略图上传后网路地址
	 * SUCCESS      是否成功标志boolean
	 * MSG          返回中文提示信息  
	*@filename 文件名
	*@type 类型，根据类型来确定缩放的width和height
	 * @return 原图上传后得到的网络uri地址
	 * @throws IOException 
	 * @throws Exception
	 */
	public static Map<String,Object> uploadImgToAliyun(InputStream stream,String filename) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len;  
		while ((len = stream.read(buffer)) > -1 ) {  
		    baos.write(buffer, 0, len);  
		}  
		baos.flush(); 
		stream.close();
		String IMGSRC ="";//返回的上传的原图片在网络中的路径
		String MSG = "上传成功";
		boolean SUCCESS=true;
		String random="";
		String postfix ="";
		Map<String,Object> srcMap = new HashMap<String,Object>();
		try{
			System.out.println(checkImgFile(baos));
			if(checkImgFile(baos)){
				random = UUID.randomUUID().toString();
				postfix = filename.substring(filename.lastIndexOf("."));
				// 上传原图
				String uploadPath = FILEDIR+random +postfix;
				IMGSRC = uploadFile(client, BUCKET, uploadPath, null,baos);
			}else{
				SUCCESS =false;
				MSG = NOTIMG;
			}
			}catch(Exception e){
			e.printStackTrace();
			SUCCESS =false;
			MSG = UPLOADEXCEPTION;
	}
		srcMap.put("msg", MSG);
		srcMap.put("success", SUCCESS);
		srcMap.put("imgsrc", IMGSRC);
		return srcMap;
	}
	/**
	 *图片2张图片（原图和缩略图）上传阿里云
	 *返回map包括下面key
	 * IMGSRC       原图片上传后网络url
	 * IMGSRC_SLT   缩略图上传后网路地址
	 * SUCCESS      是否成功标志boolean
	 * MSG          返回中文提示信息  
	*@filename 文件名
	*@type 类型，根据类型来确定缩放的width和height
	 * @return 原图上传后得到的网络uri地址
	 * @throws IOException 
	 * @throws Exception
	 */
	public static Map<String,Object> upload2ImgToAliyun(InputStream stream,String filename,String type) throws IOException{
		/**
		 * 由于流不能重复读取，所以先转化到内存
		 */
		
		int HEIGHT = 0;//缩放高度
		int WIDTH = 0;//缩放宽度
		String STANDARA = type;//缩放比例
		if(type.equals(IMGSize.X88.value())){
			HEIGHT = 88;WIDTH =88;
		}else if(type.equals(IMGSize.X640.value())){
			HEIGHT = 640;WIDTH =240;
		}else if(type.equals(IMGSize.X300.value())){
			HEIGHT = 300;WIDTH =120;
		}else if(type.equals(IMGSize.X200.value())){
			HEIGHT = 200;WIDTH =200;
		}else if(type.equals("test")){
			HEIGHT = 300;WIDTH =300;
		}else{
			throw new RuntimeException("传入尺寸没有合适的模块对应....");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len;  
		while ((len = stream.read(buffer)) > -1 ) {  
		    baos.write(buffer, 0, len);  
		}  
		baos.flush(); 
		stream.close();
		String IMGSRC ="";//返回的上传的原图片在网络中的路径
		String IMGSRC_SLT ="";//缩略图图片src
		String MSG = "上传成功";
		boolean SUCCESS=true;
		String random="";
		String postfix ="";
		Map<String,Object> srcMap = new HashMap<String,Object>();
		try{
			System.out.println(checkImgFile(baos));
			if(checkImgFile(baos)){
				random = UUID.randomUUID().toString();
				postfix = filename.substring(filename.lastIndexOf("."));
				//			System.out.println("图片后缀名"+postfix);
				// 上传原图
				String uploadPath = FILEDIR+random +postfix;
				IMGSRC = uploadFile(client, BUCKET, uploadPath, null,baos);
				//上传缩略图
				String uploadPathSLT = FILEDIR+random+STANDARA+postfix;
				/*
				 * 生成缩略图
				 */
				InputStream streamSLT = new ByteArrayInputStream(baos.toByteArray());
				Map<String, Object> map = ImageUtils.resize(streamSLT,WIDTH, HEIGHT);
				streamSLT.close();
				/*
				 * 获取处理后的缩略图inputStream
				 */
				InputStream is = (ByteArrayInputStream) map.get("input");
				IMGSRC_SLT = uploadFile(client, BUCKET, uploadPathSLT,is,null);
			}else{
				SUCCESS =false;
				MSG = NOTIMG;
			}
			}catch(Exception e){
			e.printStackTrace();
			SUCCESS =false;
			MSG = UPLOADEXCEPTION;
	}
		srcMap.put("msg", MSG);
		srcMap.put("success", SUCCESS);
		srcMap.put("imgsrc", IMGSRC);
		srcMap.put("imgsrc_slt", IMGSRC_SLT);
		return srcMap;
	}




	/**
	 *  图片上传文件
	 * @param client
	 * @param bucketName 
	 * @param key   文件上传到服务器上的路径+名
	 * @param filename 本地上传文件的src
	 * @throws OSSException
	 * @throws ClientException
	 * @throws IOException 
	 */
	private static String uploadFile(OSSClient client,String bucketName,  String uploadPath,InputStream input,ByteArrayOutputStream baos)throws OSSException, ClientException, IOException {
		if(null!=baos){
			input = new ByteArrayInputStream(baos.toByteArray());
		}
		long length = 0L;
		length = input.available();
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		// 可以在metadata中标记文件类型
		objectMeta.setContentType("image/jpeg");
		System.out.println(length);
		//上传
		PutObjectResult  result = client.putObject(bucketName, uploadPath, input, objectMeta);
		// 获取文件路径
		String eTag = result.getETag();
		String urlstr = "";
		if (eTag != null) {
			URL url = client.generatePresignedUrl(BUCKET,
					uploadPath, new Date(), HttpMethod.GET);
			urlstr = url.toString();
			urlstr = urlstr.substring(0, urlstr.indexOf("?"));
		}
		System.out.println("######"+urlstr);
		return urlstr;
	}

	/**
	 * 下载文件
	 * @param client
	 * @param bucketName
	 * @param key  文件在服务器上的文件名
	 * @param filename 下载到本地
	 * @throws OSSException
	 * @throws ClientException
	 */
	private static void downloadFile(OSSClient client, String bucketName, String downloadPath, File imgFile)throws OSSException, ClientException {

		client.getObject(new GetObjectRequest(bucketName, downloadPath), imgFile);
	}
	private static InputStream downloadFile(OSSClient client, String bucketName, String downloadPath)throws OSSException, ClientException {

		OSSObject oosobject =  client.getObject(new GetObjectRequest(bucketName, downloadPath));
		return oosobject.getObjectContent();
	}
	public static List<InputStream> downloadFile( ArrayList<String> downloadPath)throws OSSException, ClientException {
		List<InputStream> list = new ArrayList<InputStream>();
		for(String path:downloadPath){
			try {
				list.add(downloadFile(client, BUCKET, FILEDIR+path));
			} catch (Exception e) {
				e.printStackTrace();
				list.add(downloadFile(client, BUCKET, FILEDIR+"addressbook_avatar.png"));
			}
			
		}
		return list;
	}
	public static void main(String[] args) throws Exception {
		
//		downloadFile(client,"yunzo","ailierke_test/e02472f2-e762-46a3-98d8-48564e17e9cb100x100.jpg",new File("D:/qq.jpg"));
		//		上传原图和缩略图
//		File imgFile = new File("D:/test.txt");
//				upload2ImgToAliyun(new FileInputStream(imgFile),"test.txt");
//		downloadFile(client, BUCKET, "ailierke_test/eb61a08d-1563-481e-b4a7-44c6033d2daftest.jpg", imgFile);

//测试是否图片文件1
//		File file=new File("d:/test.txt");
//		ImageInputStream iis = ImageIO.createImageInputStream(imgFile);//resFile为需被
//
//		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
//		if (!iter.hasNext()) {//文件不是图片
//		    System.out.println("此文件不为图片文件");
//		} else{
//			System.out.println("此文件为图片文件");
//		}
		//测试是否图片文件2
		
// 	BufferedImage bi = ImageIO.read(new FileInputStream(file));
//	if(bi == null){
//	   System.out.println("此文件不为图片文件");
//	}else{
//		System.out.println("此文件为图片文件");
//	}
		File file=new File("d:/3.jpg");
		
		upload2ImgToAliyun(new FileInputStream(file), "3.jpg", "test");
	}

}