package com.yunzo.cocmore.core.function.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunzo.cocmore.core.function.model.mysql.YBasicImaccount;


public interface IMService {
	/**
	 * 获取用户属性（批量）
	 * @param infoMap
	 * @return
	 */
	List<Map<String, Object>> getUserList(String infoMap) throws Exception ;
	/**
	 * 批量获取群组属性
	 * @param infoMap
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	List<Map<String, Object>> getGroupProperty(String infoMap) throws JsonProcessingException, IOException;
	/**
	 * 设置群组属性
	 * @param infoMap
	 * @return
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	void saveGroupProperty(String infoMap) throws JsonProcessingException, IOException;
/**
 * 创建IM群组
 * @param infoMap
 * @throws IOException 
 * @throws JsonProcessingException 
 * @throws Exception 
 * @throws ClientException 
 * @throws OSSException 
 */
	void createGroup(String infoMap) throws JsonProcessingException, IOException, OSSException, ClientException, Exception;
	/**
	 * 群加成员
	 * @param infoMap
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @throws Exception 
	 * @throws ClientException 
	 * @throws OSSException 
	 */
	void addGroupPersons(String infoMap) throws JsonProcessingException, IOException, OSSException, ClientException, Exception;
	/**
	 * 删除群组成员
	 * @param infoMap
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @throws Exception 
	 * @throws ClientException 
	 * @throws OSSException 
	 */
	void delGroupPersons(String infoMap) throws JsonProcessingException, IOException, OSSException, ClientException, Exception;
	/**
	 * 删除群组
	 * @param infoMap
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	void delGroupById(String infoMap) throws JsonProcessingException, IOException;
	
	void sava(YBasicImaccount obj);
	
	boolean findaccount(String tel) throws Exception;
}
