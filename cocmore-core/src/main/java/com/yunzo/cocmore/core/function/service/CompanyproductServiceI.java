package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicMembercompany;
import com.yunzo.cocmore.core.function.model.mysql.YCompanyproduct;

/**
 * 公司主营产品接口
 * @author yunzo
 *
 */
public interface CompanyproductServiceI {
	/**
	 * 新增
	 * @param companyproduct
	 */
	public void save(YCompanyproduct companyproduct);
	
	/**
	 * 修改
	 * @param companyproduct
	 */
	public void update(YCompanyproduct companyproduct);
	
	/**
	 * 根据id查询对象
	 * @param fid
	 * @return
	 */
	public YCompanyproduct getById(String fid);
	
	/**
	 * 删除
	 * @param companyproduct
	 */
	public void delete(YCompanyproduct companyproduct);
	
	/**
	 * 根据hql 语句查询集合
	 * @param hql
	 * @return
	 */
	public List<YCompanyproduct> findByHql(String hql);
	
	/**
	 * 获取公司主营产品集合
	 * @param fid
	 * @param Size
	 * @return
	 */
	public List<Map<String, Object>> findList(String fid,Integer Size,String memberId);
	
}
