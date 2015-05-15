package com.yunzo.cocmore.utils.base;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;


/**
 * 图片工具类
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 * @author beck
 * 
 */
public class ImageUtils {
	
	  /**
     * 几种常见的图片格式
     */
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop
    public static int WIDTH = 640;// Photoshop的专用格式Photoshop
    public static int WIDTH1 = 110;// Photoshop的专用格式Photoshop
    
	/**
	 * 图片转换byte[]
	 * @param file
	 * @param fileImageType
	 * @return
	 * @throws Exception
	 */
	public static byte[] imageToByteArray(File file,String fileImageType) throws Exception{
		BufferedImage img = ImageIO.read(file);  
	    ByteArrayOutputStream buf = new ByteArrayOutputStream();  
	    ImageIO.write(img, fileImageType, buf); 
		return buf.toByteArray();
	}
	
	/**
	 * byte[]转换图片
	 * @param b
	 * @param url
	 * @param imageType
	 * @throws Exception
	 */
	public static void byteArrayToImage(byte[] b,String url,String imageType) throws Exception{
		ByteArrayInputStream in = new ByteArrayInputStream(b);  
	    BufferedImage image = ImageIO.read(in);  
	    File newFile = new File(url);  
	    ImageIO.write(image, imageType, newFile);
	}
	/**
     * 缩放图像（按比例缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     */
    public final static void scale(String srcImageFile, String result,
            int scale, boolean flag) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag) {// 放大
                width = width * scale;
                height = height * scale;
            } else {// 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 缩放图像（按比例缩放）按宽320的比例缩放
     * @param input
     */
    public final static Map<String, Object> scale(InputStream input,int scaleWidth) {
    	try {
    		BufferedImage src = ImageIO.read(input); // 读入文件
    		float width = src.getWidth(); // 得到源图宽
    		float height = src.getHeight(); // 得到源图长
			if(width>scaleWidth){
				float percent=1-(width-scaleWidth)/width;
    			width = width * percent;
    			height = height * percent;
    			Image image = src.getScaledInstance((int)width, (int)height,
    					Image.SCALE_DEFAULT);
    			BufferedImage tag = new BufferedImage((int)width, (int)height,
    					BufferedImage.TYPE_INT_RGB);
    			Graphics g = tag.getGraphics();
    			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
    			g.dispose();
    			return getImageStream(tag);
			}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    /**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     * @throws FileNotFoundException 
     */
    public final static Map<String, Object> scaleFromInputStream(InputStream stream,int height, int width, boolean bb) throws FileNotFoundException {
    	try {
    		double ratio = 0.0; // 缩放比例
    		BufferedImage bi = ImageIO.read(stream);
    		Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
    		// 计算比例
    		if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
    			if (bi.getHeight() > bi.getWidth()) {
    				ratio = (new Integer(height)).doubleValue()
    						/ bi.getHeight();
    			} else {
    				ratio = (new Integer(width)).doubleValue() / bi.getWidth();
    			}
    			AffineTransformOp op = new AffineTransformOp(AffineTransform
    					.getScaleInstance(ratio, ratio), null);
    			itemp = op.filter(bi, null);
    		}
    		if (bb) {//补白
    			BufferedImage image = new BufferedImage(width, height,
    					BufferedImage.TYPE_INT_RGB);
    			Graphics2D g = image.createGraphics();
    			g.setColor(Color.white);
    			g.fillRect(0, 0, width, height);
    			if (width == itemp.getWidth(null))
    				g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
    						itemp.getWidth(null), itemp.getHeight(null),
    						Color.white, null);
    			else
    				g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
    						itemp.getWidth(null), itemp.getHeight(null),
    						Color.white, null);
    			g.dispose();
    			itemp = image;
    		}
    		//ImageIO.write((BufferedImage) itemp, "JPEG", new File("d:/temp.jpg"));
    		return getImageStream((BufferedImage) itemp);
    	} catch (IOException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 将BufferImage转换成InputStream
     * @param bufferedImage
     * @return
     */
    public static Map<String, Object> getImageStream(BufferedImage bi){ 
        InputStream is = null; 
        Map<String, Object> map=new HashMap<String, Object>();
        bi.flush(); 
        ByteArrayOutputStream bs = new ByteArrayOutputStream();  
         
        ImageOutputStream imOut; 
        try { 
            imOut = ImageIO.createImageOutputStream(bs); 
            ImageIO.write(bi, "png",imOut); 
            is= new ByteArrayInputStream(bs.toByteArray()); 
            map.put("input", is);
            map.put("length", bs.toByteArray().length);
            map.put("width", bi.getWidth());
            map.put("height", bi.getHeight());
        } catch (IOException e) { 
            e.printStackTrace(); 
        }  
        
        return map; 
    } 
   
    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public final static void cut(String srcImageFile, String result,
            int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public final static Map<String, Object> cutFromInputStream(InputStream input,
    		int x, int y, int width, int height) {
    	try {
    		// 读取源图像
    		BufferedImage bi = ImageIO.read(input);
    		int srcWidth = bi.getHeight(); // 源图宽度
    		int srcHeight = bi.getWidth(); // 源图高度
    		if (srcWidth > 0 && srcHeight > 0) {
    			Image image = bi.getScaledInstance(srcWidth, srcHeight,
    					Image.SCALE_DEFAULT);
    			// 四个参数分别为图像起点坐标和宽高
    			// 即: CropImageFilter(int x,int y,int width,int height)
    			ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
    			Image img = Toolkit.getDefaultToolkit().createImage(
    					new FilteredImageSource(image.getSource(),
    							cropFilter));
    			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    			Graphics g = tag.getGraphics();
    			g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
    			g.dispose();
    			// 输出为输入流
    			return getImageStream(tag);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * 获取原图片的宽、高
     * @param input
     * @return
     */
    public static Map<String,Integer> getSourseImgInfo(InputStream input){
    	Map<String,Integer> map=new HashMap<String,Integer>();
    	// 读取源图像
		BufferedImage bi;
		try {
			bi = ImageIO.read(input);
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				map.put("srcWidth", srcWidth);
				map.put("srcHeight", srcHeight);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
    }
    /**
     * 图像切割（指定切片的行数和列数）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public final static void cut2(String srcImageFile, String descDir,
            int rows, int cols) {
        try {
            if(rows<=0||rows>20) rows = 2; // 切片行数
            if(cols<=0||cols>20) cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的宽度和高度）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param destWidth 目标切片宽度。默认200
     * @param destHeight 目标切片高度。默认150
     */
    public final static void cut3(String srcImageFile, String descDir,
            int destWidth, int destHeight) {
        try {
            if(destWidth<=0) destWidth = 200; // 切片宽度
            if(destHeight<=0) destHeight = 150; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols = 0; // 切片横向数量
                int rows = 0; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * @param srcImageFile 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 彩色转为黑白
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     */
    public final static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(destImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加文字水印
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 水印的字体名称
     * @param fontStyle 水印的字体样式
     * @param color 水印的字体颜色
     * @param fontSize 水印的字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressText(String pressText,
            String srcImageFile, String destImageFile, String fontName,
            int fontStyle, Color color, int fontSize,int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加文字水印
     * @param pressText 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 字体名称
     * @param fontStyle 字体样式
     * @param color 字体颜色
     * @param fontSize 字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressText2(String pressText, String srcImageFile,String destImageFile,
            String fontName, int fontStyle, Color color, int fontSize, int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加图片水印
     * @param pressImg 水印图片
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param x 修正值。 默认在中间
     * @param y 修正值。 默认在中间
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void pressImage(String pressImg, String srcImageFile,String destImageFile,
            int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算text的长度（一个中文算两个字符）
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
	
    /**  
     * 实现图像的等比缩放  
     * @param source  
     * @param targetW  
     * @param targetH  
     * @return  
     * @throws IOException 
     */  
    public static Map<String,Object> resize(InputStream stream, int targetW, int targetH) throws IOException {   
    	BufferedImage source = ImageIO.read(stream);
        // targetW，targetH分别表示目标长和宽   
        int type = source.getType();   
        BufferedImage target = null;   
        double sx = (double) targetW / source.getWidth();   
        double sy = (double) targetH / source.getHeight();   
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放   
        // 则将下面的if else语句注释即可   
//        if (sx < sy) {   
//            sx = sy;   
//            targetW = (int) (sx * source.getWidth());   
//        } else {   
//            sy = sx;   
//            targetH = (int) (sy * source.getHeight());   
//        }   
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade   
            ColorModel cm = source.getColorModel();   
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,   
                    targetH);   
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();   
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);   
        } else  
            target = new BufferedImage(targetW, targetH, type);   
        Graphics2D g = target.createGraphics();   
        // smoother than exlax:   
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);   
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));   
        g.dispose();   
        return getImageStream(target);
    }   
  public static void main(String[] args) throws IOException{
	  File f = new File("d:/test.jpg");
      InputStream stream = new FileInputStream(f);
      resize(stream,10, 100);
  }
  
}
