package com.yunzo.cocmore.core.function.service;


import java.util.Map;

import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.util.PagingList;
/** 
 *Description: <用户服务接口>. <br>
 *<p>
	<用户服务类测序>
 </p>
 *	Makedate:2014年11月18日 上午4:26:59 
 * @author xiaobo 
 * @version V1.0                             
 */

public interface UserService {
	
	/**
	 * 查询全部
	 * @return
	 */
	public PagingList<YSystemUsers> findAll(String searchName,String type,Integer start,Integer limit);
	
	/**
	 * 根据ID查询
	 * @param fid
	 * @return
	 */
	public YSystemUsers getById(String fid);
	
	/**
	 * 保存
	 * @param demo
	 */
	public void save(YSystemUsers demo);
	
	/**
	 * 删除
	 * @param demo
	 */
	public void delete(YSystemUsers demo);
	
	/**
	 * 根据账号和密码查询
	 * @param name
	 * @param password
	 * @return
	 */
	public YSystemUsers getByNameAndPassWord(String name,String password,String type);
	
	/**
	 * 检查用户账号是否已存在
	 * @param faccount
	 * @return
	 */
	public boolean checkByFaccount(String faccount);
	
	/**
	 * 修改密码时，检查用户原密码是否输入正确
	 * @param faccount
	 * @return
	 */
	public boolean checkUserPassword(String fid,String password);
	
	/**
	 * 修改
	 * @param demo
	 */
	public void update(YSystemUsers demo);
	
	/**
	 * 修改密码
	 * @param password
	 */
	public void updatePassword(String fid,String password);
	
	/**
	 * 通过账号查询
	 * @param faccount
	 * @return
	 */
	public YSystemUsers getByFaccount(String faccount);
	
	/**
	 * 发送验证码到手机
	 * @param faccount
	 */
	public int sendTel(String faccount);
	
	/**
	 * 判断验证码
	 * @param faccount
	 * @param code
	 * @return
	 */
	public Map<String, Object> checkCode(String faccount,String code);
}
