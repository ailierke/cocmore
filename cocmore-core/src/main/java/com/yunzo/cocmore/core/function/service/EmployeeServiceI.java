package com.yunzo.cocmore.core.function.service;

import java.util.List;

import com.yunzo.cocmore.core.function.model.mysql.YBasicEmployee;
import com.yunzo.cocmore.core.function.model.mysql.YBasicentriesEmployeedistribution;
import com.yunzo.cocmore.core.function.util.PagingList;

/** 
 *Description: <职员服务接口>. <br>
 * @date:2014年11月25日 下午4:48:56
 * @author beck
 * @version V1.0                             
 */

public interface EmployeeServiceI {
	public PagingList<YBasicEmployee> findAll(String orgId,String searchName,Integer start,Integer limit);
	
	public YBasicEmployee getById(String fid);
	
	public void save(YBasicEmployee demo);
	
	public void delete(YBasicEmployee demo);
	
	public void update(YBasicEmployee demo);
	
	public void updateStatus(YBasicEmployee demo);
	
	/**
	 * 根据职员ID查询职员职位分配的数据
	 * @return
	 */
	public List<YBasicentriesEmployeedistribution> findEDByPosID(String fid);
	/**
	 * 
	 * @Title: findPosByEdID 
	 * @Description: TODO 根据职位查询职员
	 * @param @param fid
	 * @param @return    
	 * @return List<YBasicEmployee>   
	 * @throws
	 */
	public List<YBasicentriesEmployeedistribution> findPosByEdID(String fid);
}
