package com.yunzo.cocmore.core.function.service;

import java.util.List;
import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YBasicGetadress;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;

/**
 * 收获地址接口
 * @author yunzo
 *
 */
public interface GetadressServiceI {
	/**
	 * 新增
	 * @param getadress
	 */
	public void save(YBasicGetadress getadress);
	/**
	 * 修改
	 * @param getadress
	 */
	public void update(YBasicGetadress getadress);
	/**
	 * 删除
	 * @param getadress
	 */
	public void delete(YBasicGetadress getadress);
	
	/**
	 * 根据hql查询
	 * @param hql
	 * @return
	 */
	public List<YBasicGetadress> findByHql(String hql);
	
	/**
	 * 根据id查询对象
	 * @param fid
	 * @return
	 */
	public YBasicGetadress getById(String fid);
	
	/**
	 * 获取我的收货地址列表
	 * @param memberId
	 * @return
	 */
	public List<Map<String, Object>> findByMemberList(List<YBasicMember> memberlist); 
	
	/**
	 * 根据会员查询会员的所有收货地址
	 * @param memberId
	 * @return
	 */
	public List<YBasicGetadress> findByMember(List<YBasicMember> memberlist);
	
	public Map<String,Object> saveAdress(String memberId,String consigneeAddress,String consignee,String consigneePhone,String provincialId,String cityId,String countryId);
}
