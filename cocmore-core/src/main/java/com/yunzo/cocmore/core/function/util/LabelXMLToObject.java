package com.yunzo.cocmore.core.function.util;

import java.util.ArrayList;
import java.util.List;


import com.yunzo.cocmore.core.function.model.mysql.YBasicLabel;

/**
 * Description: <标签XML文件转换为对象>. <br>
 * @date:2015年1月9日 下午6:20:34
 * @author beck
 * @version V1.0
 */
public class LabelXMLToObject {
	
//	public static List<YBasicLabel> labelXmlToObject(String fuserPhone) throws Exception{
//		YBasicLabel obj = null;
//		List<YBasicLabel> list = new ArrayList<YBasicLabel>();
//		SAXReader reader = new SAXReader();
//		URL url1=LabelXMLToObject.class.getClassLoader().getResource("../../");
//	    String url = url1.getPath().replaceFirst("/", "");
//		System.out.println(url);
//		Document document = reader.read(new File(url+"WEB-INF/classes/label.xml")); 
//
//		//获取根节点
//		Element root = document.getRootElement();
//		for(Iterator it = root.elementIterator();it.hasNext();){
//			Element ele = (Element) it.next();
//			
//			Element e1 = ele.element("forderIndex");
//			Element e2 = ele.element("ftype");
//			Element e3 = ele.element("ftrades");
//			Element e4 = ele.element("fprovincialId");
//			Element e5 = ele.element("fcityId");
//			
//			obj = new YBasicLabel();
//			obj.setForderIndex(new Integer(e1.getTextTrim()));
//			obj.setFtype(new Integer(e2.getTextTrim()));
//			obj.setFtrades(e3.getTextTrim());
//			obj.setFprovincialId(e4.getTextTrim());
//			obj.setFcityId(e5.getTextTrim());
//			obj.setFuserPhone(fuserPhone);
//			
//			list.add(obj);
//		}
//		return list;
//	}
	
	public static List<YBasicLabel> labelObject(String fuserPhone){
		List<YBasicLabel> list = new ArrayList<YBasicLabel>();
		
		YBasicLabel o1 = new YBasicLabel();
		o1.setForderIndex(1);//排序号
		o1.setFtype(1);//类型：0、地域（城市、省份）；1、行业
		o1.setFtrades("7be63598-1706-45f0-acc0-127e64ad49c8");//行业
		o1.setFuserPhone(fuserPhone);//用户电话
		
		YBasicLabel o2 = new YBasicLabel();
		o2.setForderIndex(2);
		o2.setFtype(0);
		o2.setFprovincialId("510000");
		o2.setFuserPhone(fuserPhone);
		
		YBasicLabel o3 = new YBasicLabel();
		o3.setForderIndex(3);
		o3.setFtype(1);
		o3.setFtrades("7f511a6b-7568-462e-915d-29670647475e");
		o3.setFuserPhone(fuserPhone);
		
		YBasicLabel o4 = new YBasicLabel();
		o4.setForderIndex(4);
		o4.setFtype(0);
		o4.setFprovincialId("440000");
		o4.setFuserPhone(fuserPhone);
		
		YBasicLabel o5 = new YBasicLabel();
		o5.setForderIndex(5);
		o5.setFtype(1);
		o5.setFtrades("3c413b01-4092-450f-80b1-edaeb64119c4");
		o5.setFuserPhone(fuserPhone);
		
		YBasicLabel o6 = new YBasicLabel();
		o6.setForderIndex(6);
		o6.setFtype(0);
		o6.setFprovincialId("360000");
		o6.setFuserPhone(fuserPhone);
		
		YBasicLabel o7 = new YBasicLabel();
		o7.setForderIndex(7);
		o7.setFtype(1);
		o7.setFtrades("47436cd6-938c-44e7-ade1-09b555a82c4b");
		o7.setFuserPhone(fuserPhone);
		
		YBasicLabel o8 = new YBasicLabel();
		o8.setForderIndex(8);
		o8.setFtype(0);
		o8.setFprovincialId("110000");
		o8.setFuserPhone(fuserPhone);
		
		YBasicLabel o9 = new YBasicLabel();
		o9.setForderIndex(9);
		o9.setFtype(1);
		o9.setFtrades("b11f8b53-d7d3-41b5-a690-06fc4a0cd281");
		o9.setFuserPhone(fuserPhone);
		
		YBasicLabel o10 = new YBasicLabel();
		o10.setForderIndex(10);
		o10.setFtype(0);
		o10.setFprovincialId("310000");
		o10.setFuserPhone(fuserPhone);
		
		list.add(o1);
		list.add(o2);
		list.add(o3);
		list.add(o4);
		list.add(o5);
		list.add(o6);
		list.add(o7);
		list.add(o8);
		list.add(o9);
		list.add(o10);
		
		return list;
	}
	
//	public static void main(String[] args){
//		try {
//			List<YBasicLabel> list = LabelXMLToObject.labelXmlToObject("13256895623");
//			System.out.println(list.size());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
}
