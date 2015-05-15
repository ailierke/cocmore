package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicDistrict;


/** 
 *Description: <区域服务接口>. <br>
 * @date:2014年12月2日 下午3:44:18
 * @author beck
 * @version V1.0                             
 */

public interface DistrictServiceI {
	public List<YBasicDistrict> findAll();
}
