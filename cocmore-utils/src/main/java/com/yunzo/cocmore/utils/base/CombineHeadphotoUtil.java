package com.yunzo.cocmore.utils.base;
/**
 * 专门通过4个群组成员获得拼成的群头像
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class CombineHeadphotoUtil {
	public static int width = Math.max(44, 1);
	public static int height = Math.max(44, 1);
	public static int combineImgWidth = Math.max(88, 1);
	public static int combineImgHeight = Math.max(88, 1);
	
	/**
	 * 传入4个用户头像
	 * @param files
	 * @throws Exception
	 */
	public static String combineHeadPhotoImgs(List<InputStream> streams,String type) throws Exception{
			
			List<int[]> imgArrayList = getImageArray(streams);

			// 生成新图片
			BufferedImage ImageNew = new BufferedImage(combineImgWidth, combineImgHeight,BufferedImage.TYPE_INT_RGB);
			// 获取Graphics2D

			ImageNew.setRGB(0, 0, width, height, imgArrayList.get(0), 0, width);// 设置上半部分的RGB
			ImageNew.setRGB(width,0,width,height,imgArrayList.get(1),0,width);//设置上半部分的RGB
//			ImageNew.setRGB(width*2,0,width,height,imgArrayList.get(2),0,width);//设置上半部分的RGB
			
//			ImageNew.setRGB(0,height,width,height,imgArrayList.get(3),0,width);//设置中半部分的RGB
//			ImageNew.setRGB(width,height,width,height,imgArrayList.get(4),0,width);//设置中半部分的RGB
//			ImageNew.setRGB(width*2,height,width,height,imgArrayList.get(5),0,width);//设置中半部分的RGB
			
//			ImageNew.setRGB(0,height*2,width,height,imgArrayList.get(6),0,width);//设置下半部分的RGB
			ImageNew.setRGB(0,height,width,height,imgArrayList.get(2),0,width);//设置下半部分的RGB
			ImageNew.setRGB(width,height,width,height,imgArrayList.get(3),0,width);//设置下半部分的RGB
//			File outFile = new File("d:/test3.jpg");
//			ImageIO.write(ImageNew, "jpg", outFile);// 写图片

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(ImageNew, "jpg", output);
			byte[] buff = output.toByteArray();
			InputStream is = new java.io.ByteArrayInputStream(buff); 
			
			Map<String,Object> map= AliyunOSSUtils.upload2ImgToAliyun(is, ".jpg",type);
			String url =(String)map.get("imgsrc");
			return url;
	}
	
	/**
	 * 获取9张头像的int数组
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static List<int[]> getImageArray(List<InputStream> streams) throws IOException{
		Graphics graphics = null;
		List<int[]> imageArrayList = new ArrayList<int[]>();
		for(InputStream inputStream :streams){
			/**
			 * 需要的拼接的图片
			 */
			BufferedImage ImageOne = ImageIO.read(inputStream);

			BufferedImage imageOne = new BufferedImage(width, height,  
					BufferedImage.TYPE_INT_RGB);  
			graphics = imageOne.getGraphics();  

			graphics.drawImage(ImageOne, 0, 0, width, height, null); // 绘制缩小后的图  
			// 画边框,在drawImage后面，下面代码给图片加上两个像素的白边     
			graphics.setColor(Color.WHITE);     
			graphics.drawRect(0, 0, width - 1, height - 1);  
			graphics.drawRect(1, 1, width - 1, height - 1);  
			graphics.drawRect(0, 0, width - 2, height - 2);  
			
			// 从图片中读取RGB
			//根据缩略图的固定比例获取
			int[] ImageArrayOne = new int[width * height];
			ImageArrayOne = imageOne.getRGB(0, 0, width, height,ImageArrayOne, 0, width);
			imageArrayList.add(ImageArrayOne);
		}
		return imageArrayList;
	}
	public static void main(String[] args) throws Exception {
		List<InputStream> stream = new ArrayList<InputStream>();
		for(int i=0;i<4;i++){
			if(i/3==0){
				stream.add(new FileInputStream(new  File("D:/test1.jpg")));
			}else{
				stream.add(new FileInputStream(new  File("D:/test2.jpg")));
			}
		}
		combineHeadPhotoImgs(stream,IMGSize.X88.value());
	}
}
