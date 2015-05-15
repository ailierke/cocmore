package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicIndustry;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <产业服务接口>. <br>
 * @date:2014年11月25日 下午5:01:45
 * @author beck
 * @version V1.0                             
 */

public interface IndustryServiceI {
	/**
	 * 查询全部并分页
	 * @return
	 */
	public PagingList<YBasicIndustry> findAll(String searchName,Integer start,Integer limit);
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YBasicIndustry getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YBasicIndustry demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(YBasicIndustry demo);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YBasicIndustry demo);
}
