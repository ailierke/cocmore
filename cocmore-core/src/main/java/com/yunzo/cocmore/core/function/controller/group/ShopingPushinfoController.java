package com.yunzo.cocmore.core.function.controller.group;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
import com.yunzo.cocmore.core.function.model.mysql.YBasicShopingPushinfo;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroupsactivity;
import com.yunzo.cocmore.core.function.service.ShopingPushinfoServiceI;

/**
 * @author：jackpeng
 * @date：2015年3月27日下午6:30:04
 * 预约推送信息记录Controller
 */
@Controller
@RequestMapping("/shopingPushinfoController")
public class ShopingPushinfoController {
	
	private static final Logger logger = Logger.getLogger(ShopingPushinfoController.class);
	
	@Resource
	private ShopingPushinfoServiceI shopingPushinfoService;
	
	/**
	 * 根据id查询活动
	 * @param id
	 * @return
	 */
	@RequestMapping("/saveOrder")
	@ResponseBody
	@SystemControllerLog(description = "新增订单未读记录")
	public void saveOrder(String tel,String orderId){
		try {
			YBasicShopingPushinfo obj = new YBasicShopingPushinfo();
			obj.setFtel(tel);
			obj.setFshopingId(orderId);
			obj.setFtype(6);
			obj.setFstatu(22);
			obj.setFupdateTime(new Date());
			shopingPushinfoService.save(obj);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
